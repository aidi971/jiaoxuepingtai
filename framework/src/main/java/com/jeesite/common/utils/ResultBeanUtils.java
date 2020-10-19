package com.jeesite.common.utils;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseApiController;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Method;

/**
 * @ClassName ResultBeanUtils
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-01 17:14
 * @Version 1.0
 */
public class ResultBeanUtils {

    private static final String RESULT_BEAN_MSG = "result_bean_msg";

    /**
     * 根据全类名获取，返回提示词
     * @param clazz
     * @param methodName
     */
    public static String getMsg(Class clazz,String methodName){
        String msg = CacheUtils.get(ResultBeanUtils.RESULT_BEAN_MSG,clazz.getName() + methodName);
        if (msg == null){
            try{
                if (BaseApiController.class.isAssignableFrom(clazz)){
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods){
                        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                        if (apiOperation != null && StringUtils.isNotBlank(apiOperation.value())){
                            CacheUtils.put(ResultBeanUtils.RESULT_BEAN_MSG,clazz.getName() + method.getName(),apiOperation.value());
                        }else {
                            CacheUtils.put(ResultBeanUtils.RESULT_BEAN_MSG,clazz.getName() + method.getName(),StringUtils.EMPTY);
                        }
                    }
                    msg = CacheUtils.get(ResultBeanUtils.RESULT_BEAN_MSG,clazz.getName() + methodName);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return msg;
    }
}
