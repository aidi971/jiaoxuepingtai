package com.jeesite.framework.json;

import java.lang.annotation.*;

/**
 * Controller中过滤返回值字段,默认过滤BaseEntity中日期和用户字段, 仅可放在方法上
 * include: 一定只返回 某些字段
 * filter: include 为空时, 一定排除 某些字段
 * 结论: 同时配置了 include 和 filter 时, 只处理 include
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = JsonResponseFilters.class)
public @interface JsonResponseFilter {
    Class<?> type();
    String include() default "";
    String filter() default "";
}
