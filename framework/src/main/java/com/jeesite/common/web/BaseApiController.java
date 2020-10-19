package com.jeesite.common.web;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.ResultBeanUtils;
import com.jeesite.common.web.response.GlobalStatusCode;
import com.jeesite.common.web.utils.JsonResultUtils;
import org.springframework.web.bind.WebDataBinder;

/**
 * @ClassName BaseApiController
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-16 9:20
 * @Version 1.0
 */
public class BaseApiController {

    public static final String WEB_DATA_BINDER_SOURCE = WebDataBinder.class.getName() + ".SOURCE";
    public static final String WEB_DATA_BINDER_TARGET = WebDataBinder.class.getName() + ".T+RGET";

    protected ResultBean success() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String msg = ResultBeanUtils.getMsg(this.getClass(),methodName);
        if (StringUtils.isNotBlank(msg)){
            return success(null,msg + "成功！");
        }
        return JsonResultUtils.successResult();
    }

    protected <T> ResultBean<T> success(T data) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String msg = ResultBeanUtils.getMsg(this.getClass(),methodName);
        if (StringUtils.isNotBlank(msg)){
            return success(data,msg + "成功！");
        }
        return JsonResultUtils.successResult(data);
    }

    protected <T> ResultBean<T> success(T data, String msg) {
        return new ResultBean(msg,data,GlobalStatusCode.CODE_SUCCESS.code(),true,null);
    }

    protected <T> ResultBean<T> success(T data, String msg,Object other) {
        return  new ResultBean(msg,data,GlobalStatusCode.CODE_SUCCESS.code(),true,other);
    }

    protected <T> ResultBean<T> error(String errDesc) {
        return JsonResultUtils.errorResult(errDesc);
    }

    protected <T> ResultBean<T> error(GlobalStatusCode globalStatusCode) {
        return JsonResultUtils.errorResult(globalStatusCode);
    }

    protected <T> ResultBean<T> error(String errDesc, Integer code) {
        return JsonResultUtils.errorResult(errDesc, code);
    }
}