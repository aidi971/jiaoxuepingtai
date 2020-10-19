package com.jeesite.common.exception;

/**
 * @ClassName AuthorizationException
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-26 12:01
 * @Version 1.0
 */
public class VerifyCodeException extends RuntimeException {
    public VerifyCodeException() {
        super();
    }

    public VerifyCodeException(String message) {
        super(message);
    }

    public VerifyCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerifyCodeException(Throwable cause) {
        super(cause);
    }

    protected VerifyCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
