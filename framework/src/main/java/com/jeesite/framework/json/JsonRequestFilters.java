package com.jeesite.framework.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wq
 * @date 2019-05-16
 * @description qing-yuan-api
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRequestFilters {
    JsonRequestFilter[] value();
}
