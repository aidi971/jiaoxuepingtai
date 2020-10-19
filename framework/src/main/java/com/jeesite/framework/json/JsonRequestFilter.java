package com.jeesite.framework.json;


import java.lang.annotation.*;

/**
 * Controller中过滤接受值字段,默认过滤BaseEntity中日期和用户字段, 仅可放在参数上
 * include: 一定只接受 某些字段
 * filter: include 为空时, 一定排除 某些字段
 * 结论: 同时配置了 include 和 filter 时, 只处理 include
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = JsonRequestFilters.class)
public @interface JsonRequestFilter {
    Class type() default JsonRequestFilter.class;
    String include() default "";
    String filter() default "";
}
