package com.jeesite.common.web.utils;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.web.response.GlobalStatusCode;

/**
 * @ClassName JsonResultUtil
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-08-30 11:02
 * @Version 1.0
 */
public class JsonResultUtils {
    public JsonResultUtils() {
    }

    public static ResultBean successResult() {
        return successResult(GlobalStatusCode.CODE_SUCCESS);
    }

    public static ResultBean<String> successResult(GlobalStatusCode GlobalStatusCode) {
        return new ResultBean(GlobalStatusCode.value(), GlobalStatusCode.value(), GlobalStatusCode.code(), true);
    }

    public static ResultBean<String> successResult(Integer code, String msg) {
        return new ResultBean(msg, msg, code, true);
    }

    public static <T> ResultBean<T> successResult(T data) {
        return new ResultBean(GlobalStatusCode.CODE_SUCCESS.value(), data, GlobalStatusCode.CODE_SUCCESS.code(), true);
    }

    public static <T> ResultBean<T> successResult(T data, String token) {
        return new ResultBean(GlobalStatusCode.CODE_SUCCESS.value(), data, GlobalStatusCode.CODE_SUCCESS.code(), true, token);
    }

    public static <T> ResultBean<T> errorResult(String errDesc) {
        return new ResultBean(GlobalStatusCode.CODE_SUCCESS.value(), GlobalStatusCode.CODE_SUCCESS.code(), errDesc, false);
    }

    public static <T> ResultBean<T> errorResult(GlobalStatusCode globalStatusCode) {
        return new ResultBean(globalStatusCode.value(), globalStatusCode.code(), globalStatusCode.value(), false);
    }

    public static <T> ResultBean<T> errorResult(String errDesc, Integer code) {
        return new ResultBean(GlobalStatusCode.CODE_ERROR.value(), code, errDesc, false);
    }


}