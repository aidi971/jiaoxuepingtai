package com.jeesite.framework.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeesite.common.utils.JsonUtils;
import com.jeesite.framework.json.CustomerJsonSerializer;
import com.jeesite.framework.json.JsonRequestFilter;
import com.jeesite.framework.json.JsonRequestFilters;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author wq
 * @date 2019-05-15
 * @description qing-yuan-api
 */
@ControllerAdvice
public class JsonFilterRequestBodyAdvice implements RequestBodyAdvice {
    /**
     * 支持: 方法上单个,多个 JsonRequestFilter, 参数上支持 单个 JsonRequestFilter
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(JsonRequestFilter.class)
                || methodParameter.hasMethodAnnotation(JsonRequestFilters.class)
                || methodParameter.hasParameterAnnotation(JsonRequestFilter.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object value, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        try {
            CustomerJsonSerializer jsonSerializer = CustomerJsonSerializer.getRequestFilterJsonSerializer(methodParameter);
            String json = jsonSerializer.toJson(value);
            return JsonUtils.getEntity(json, methodParameter.getParameterType());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return null;
    }
}
