package com.langheng.modules.base.config;

import com.jeesite.common.entity.ResultBean;
import com.jeesite.common.exception.AuthorizationException;
import com.jeesite.common.exception.LicenseException;
import com.jeesite.common.exception.PermissionException;
import com.jeesite.common.web.response.GlobalStatusCode;
import com.jeesite.common.web.utils.JsonResultUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-04-01 15:53
 * @Version 1.0
 */
@ControllerAdvice(basePackages = "com.langheng.modules")
@ResponseBody
public class BaseExceptionHandler {

    @ExceptionHandler(LicenseException.class)
    public ResultBean licenseErrorHandler(Exception exception) {
        exception.printStackTrace();
        return JsonResultUtils.errorResult(exception.getMessage(),GlobalStatusCode.CODE_LICENSE_ERROR.code());
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResultBean authorizationErrorHandler(Exception exception) {
        exception.printStackTrace();
        return JsonResultUtils.errorResult(exception.getMessage(),GlobalStatusCode.CODE_AUTH_ERROR.code());
    }

    @ExceptionHandler(PermissionException.class)
    public ResultBean defaultPermissionErrorHandler(Exception exception) {
        exception.printStackTrace();
        return JsonResultUtils.errorResult(exception.getMessage(),GlobalStatusCode.CODE_PERMISSION_ERROR.code());
    }

    @ExceptionHandler(ValidationException.class)
    public ResultBean defaultValidationErrorHandler(Exception exception) {
        exception.printStackTrace();
        return JsonResultUtils.errorResult(exception.getMessage(),GlobalStatusCode.CODE_INVALID_PARAMETER.code());
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResultBean defaultSqlErrorHandler(BadSqlGrammarException exception) {
        exception.printStackTrace();
        return JsonResultUtils.errorResult(exception.getMessage(),GlobalStatusCode.CODE_SQL_ERROR.code());
    }

    @ExceptionHandler(Exception.class)
    public ResultBean defaultErrorHandler(Exception exception) {
        exception.printStackTrace();
        return JsonResultUtils.errorResult(exception.getMessage(),GlobalStatusCode.CODE_ERROR.code());
    }
}
