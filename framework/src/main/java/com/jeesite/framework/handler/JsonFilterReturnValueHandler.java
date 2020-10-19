package com.jeesite.framework.handler;

import com.jeesite.framework.json.CustomerJsonSerializer;
import com.jeesite.framework.json.JsonResponseFilter;
import com.jeesite.framework.json.JsonResponseFilters;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName JsonFilterReturnValueHandler
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019/5/10 15:05
 * @Version 1.0
 */
@Component
public class JsonFilterReturnValueHandler implements HandlerMethodReturnValueHandler {

    /**
     * 支持方法上单个 多个JsonResponseFilter
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(JsonResponseFilter.class)
                || methodParameter.hasMethodAnnotation(JsonResponseFilters.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        // 设置这个就是最终的处理类了，处理完不再去找下一个类进行处理
        mavContainer.setRequestHandled(true);

        // 获得注解并执行filter方法 最后返回
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        CustomerJsonSerializer jsonSerializer = CustomerJsonSerializer.getResponseFilterJsonSerializer(returnType);

        assert response != null;
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = jsonSerializer.toJson(returnValue);
        response.getWriter().write(json);
    }

}
