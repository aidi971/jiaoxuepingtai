package com.jeesite.framework.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;

/**
 * @ClassName CustomerJsonSerializer
 * @Description 自定义json序列化
 * @Author xiaoxie
 * @Date 2019/5/10 10:43
 * @Version 1.0
 */

public class CustomerJsonSerializer {

    private ObjectMapper mapper = new ObjectMapper();
    private JacksonFilter jacksonFilter = new JacksonFilter();

    public static CustomerJsonSerializer getRequestFilterJsonSerializer(MethodParameter methodParameter) {
        CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
        if (methodParameter.hasMethodAnnotation(JsonRequestFilter.class)) {
            JsonRequestFilter jsonFilterReturn = methodParameter.getMethodAnnotation(JsonRequestFilter.class);
            jsonSerializer.filter(jsonFilterReturn);
        } else if (methodParameter.hasMethodAnnotation(JsonRequestFilters.class)) {
            JsonRequestFilters jsonFilterReturns = methodParameter.getMethodAnnotation(JsonRequestFilters.class);
            assert jsonFilterReturns != null;
            JsonRequestFilter[] jsonResponseFilters = jsonFilterReturns.value();
            for (JsonRequestFilter jsonRequestFilter : jsonResponseFilters) {
                jsonSerializer.filter(jsonRequestFilter);
            }
        }
        if (methodParameter.hasParameterAnnotation(JsonRequestFilter.class)) {
            JsonRequestFilter jsonFilterReturn = methodParameter.getParameterAnnotation(JsonRequestFilter.class);
            jsonSerializer.filter(methodParameter.getParameterType(), jsonFilterReturn);
        }
        return jsonSerializer;
    }

    public static CustomerJsonSerializer getResponseFilterJsonSerializer(MethodParameter returnType) {
        CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
        if (returnType.hasMethodAnnotation(JsonResponseFilter.class)) {
            JsonResponseFilter jsonFilterReturn = returnType.getMethodAnnotation(JsonResponseFilter.class);
            jsonSerializer.filter(jsonFilterReturn);
        } else if (returnType.hasMethodAnnotation(JsonResponseFilters.class)) {
            JsonResponseFilters jsonFilterReturns = returnType.getMethodAnnotation(JsonResponseFilters.class);
            assert jsonFilterReturns != null;
            JsonResponseFilter[] jsonResponseFilters = jsonFilterReturns.value();
            for (JsonResponseFilter jsonResponseFilter : jsonResponseFilters) {
                jsonSerializer.filter(jsonResponseFilter);
            }
        }
        return jsonSerializer;
    }

    /**
     * @param clazz   target type
     * @param include include fields
     * @param filter  filter fields
     */
    public void filter(Class<?> clazz, String include, String filter) {
        if (clazz == null) {
            return;
        }
        if (com.jeesite.common.lang.StringUtils.isNotBlank(include)) {
            jacksonFilter.include(clazz, include.split(","));
        }
        if (com.jeesite.common.lang.StringUtils.isNotBlank(filter)) {
            jacksonFilter.filter(clazz, filter.split(","));
        }
        mapper.addMixIn(clazz, jacksonFilter.getClass());
    }

    public String toJson(Object object) throws JsonProcessingException {
        mapper.setFilterProvider(jacksonFilter);
        return mapper.writeValueAsString(object);
    }

    public void filter(Class clazz, JsonRequestFilter jsonFilterRequest) {
        this.filter(clazz, jsonFilterRequest.include(), jsonFilterRequest.filter());
    }

    public void filter(JsonResponseFilter jsonFilterReturn) {
        this.filter(jsonFilterReturn.type(), jsonFilterReturn.include(), jsonFilterReturn.filter());
    }

    public void filter(JsonRequestFilter jsonRequestFilter) {
        this.filter(jsonRequestFilter.type(), jsonRequestFilter.include(), jsonRequestFilter.filter());
    }
}
