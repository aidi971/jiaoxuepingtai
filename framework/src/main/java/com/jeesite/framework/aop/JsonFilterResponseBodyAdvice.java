package com.jeesite.framework.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeesite.common.utils.JsonUtils;
import com.jeesite.framework.json.CustomerJsonSerializer;
import com.jeesite.framework.json.JsonResponseFilter;
import com.jeesite.framework.json.JsonResponseFilters;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author wq
 * @date 2019-05-14
 * @description qing-yuan-api
 */
@ControllerAdvice
public class JsonFilterResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 支持 方法上 单个多个 JsonResponseFilter
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(JsonResponseFilter.class)
                || methodParameter.hasMethodAnnotation(JsonResponseFilters.class);
    }

    @Override
    public Object beforeBodyWrite(Object value, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        try {
            CustomerJsonSerializer jsonSerializer = CustomerJsonSerializer.getResponseFilterJsonSerializer(returnType);
            String json = jsonSerializer.toJson(value);
            return JsonUtils.getMap(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

}
