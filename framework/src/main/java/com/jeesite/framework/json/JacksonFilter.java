package com.jeesite.framework.json;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.jeesite.common.entity.BaseEntity;

import java.lang.reflect.Field;
import java.util.*;


/**
 * @ClassName JacksonFilter
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019/5/10 10:13
 * @Version 1.0
 */
@JsonFilter("JacksonFilter")
public class JacksonFilter extends FilterProvider {

    private Map<Class<?>, Set<String>> includeMap = new HashMap<>();
    private Map<Class<?>, Set<String>> filterMap = new HashMap<>();

    public void include(Class<?> type, String[] fields) {
        addToMap(includeMap, type, fields);
    }

    public void filter(Class<?> type, String[] fields) {
        addToMap(filterMap, type, fields);
    }

    private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] configFields) {
        List<String> configFieldList = Arrays.asList(configFields);
        Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
        fieldSet.addAll(configFieldList);
        map.put(type, fieldSet);
    }

    @Deprecated
    @Override
    public BeanPropertyFilter findFilter(Object filterId) {
        throw new UnsupportedOperationException("Access to deprecated filters not supported");
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {

        return new SimpleBeanPropertyFilter() {

            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer)
                    throws Exception {
                if (apply(pojo.getClass(), writer.getName())) {
                    writer.serializeAsField(pojo, jgen, prov);
                } else if (!jgen.canOmitFields()) {
                    writer.serializeAsOmittedField(pojo, jgen, prov);
                }
            }
        };
    }

    public boolean apply(Class<?> type, String name) {
        Set<String> includeFields = includeMap.get(type);
        Set<String> filterFields = filterMap.get(type);
        if (filterFields == null) {
            filterFields = new HashSet<>(20);
        }

        // 过滤BaseEntity
        filterFields.addAll(filterBaseEntityFields());

        // 处理filter中通配符
        filterWildcardFields(type, filterFields);

        /* 核心逻辑
         * include: 一定只接受 某些字段
         * filter: include 为空时, 一定排除 某些字段
         * 结论: 同时配置了 include 和 filter 时, 只处理 include
         */

        boolean includeMode = false;
        if (includeFields != null && includeFields.size() > 0) {
            includeMode = true;
        }

        if (includeMode) {
            return includeFields.contains(name);
        } else {
            return !filterFields.contains(name);
        }

        // return true: 包含    return false: 不包含
    }

    /**
     * 处理通配符
     */
    private void filterWildcardFields(Class<?> type, Set<String> configFieldSet) {
        Field[] classFields = type.getDeclaredFields();
        // 通配符为* 为过滤所有字段
        for (String configField : configFieldSet){
            if ("*".equals(configField)){
                Set<String> allFieldSet = new HashSet<>();
                for (Field field : classFields){
                    allFieldSet.add(field.getName());
                }
                configFieldSet.addAll(allFieldSet);
                break;
            }
        }
    }

    /**
     * 过滤BaseEntity字段
     */
    private Set<String> filterBaseEntityFields() {
        Set<String> configFieldSet = new HashSet<>(4);
        Field[] baseEntityFields = BaseEntity.class.getDeclaredFields();
        for (Field field : baseEntityFields){
            String name = field.getName();
            configFieldSet.add(name);
        }
        return configFieldSet;
    }

}
