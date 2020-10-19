package com.jeesite.framework.handler;

import com.jeesite.common.utils.JsonUtils;
import com.jeesite.framework.json.CustomerJsonSerializer;
import com.jeesite.framework.json.JsonRequestFilter;
import com.jeesite.framework.json.JsonRequestFilters;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JsonFilterArgumentResolver
 * @Description spring mvc 参数处理器, 处理接受值过滤
 * @Author xiaoxie
 * @Date 2019/5/10 10:54
 * @Version 1.0
 */
@Component
public class JsonFilterArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 支持 方法上单个多个 JsonRequestFilter, 与参数上 单个 JsonRequestFilter
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return matchParameterClass(methodParameter);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 当前 controller 形参类型, 多个形参, 多次进入
        Class nowClazz = methodParameter.getParameterType();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;

        Map<String, String> objectMap = new HashMap<>(10);
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String formKey = entry.getKey();
            Field declaredField = ReflectionUtils.findField(nowClazz, formKey);
            if (declaredField != null) {
                objectMap.put(formKey, String.join(",", entry.getValue()));
            }
        }
        mavContainer.setRequestHandled(true);


        Object entity = JsonUtils.getEntity(objectMap, nowClazz);

        CustomerJsonSerializer jsonSerializer = CustomerJsonSerializer.getRequestFilterJsonSerializer(methodParameter);
        String jsonStr = jsonSerializer.toJson(entity);

        Object arg = JsonUtils.getEntity(jsonStr, nowClazz);

        doValid(methodParameter, mavContainer, webRequest, binderFactory, arg);

        return arg;
    }

    private void doValid(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory, Object arg) throws Exception {
        String name = Conventions.getVariableNameForParameter(methodParameter);
        if (binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
            if (arg != null) {
                this.validateIfApplicable(binder, methodParameter);
                if (binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, methodParameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }

            mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        }
    }

    /**
     * 根据参数实际类型, 加上其携带的注解判断是否 需要过滤
     */
    private boolean matchParameterClass(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(JsonRequestFilter.class)) {
            return true;
        }
        Class<?> parameterClass = methodParameter.getParameterType();
        boolean methodMatch = false;
        if (methodParameter.hasMethodAnnotation(JsonRequestFilter.class)) {
            JsonRequestFilter jsonRequestFilter = methodParameter.getMethodAnnotation(JsonRequestFilter.class);
            assert jsonRequestFilter != null;
            methodMatch = jsonRequestFilter.type().equals(parameterClass);
        } else if (methodParameter.hasMethodAnnotation(JsonRequestFilters.class)) {
            JsonRequestFilters jsonFilterReturns = methodParameter.getMethodAnnotation(JsonRequestFilters.class);
            assert jsonFilterReturns != null;
            JsonRequestFilter[] jsonResponseFilters = jsonFilterReturns.value();
            for (JsonRequestFilter jsonRequestFilter : jsonResponseFilters) {
                boolean flag = jsonRequestFilter.type().equals(parameterClass);
                if (flag) {
                    methodMatch = true;
                    break;
                }
            }
        }
        return methodMatch;
    }

    private boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        boolean hasBindingResult = paramTypes.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]);
        return !hasBindingResult;
    }

    private void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();

        for (Annotation ann : annotations) {
            Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann);
                Object[] validationHints = hints instanceof Object[] ? (Object[]) hints : new Object[]{hints};
                binder.validate(validationHints);
                break;
            }
        }

    }

}