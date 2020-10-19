package com.jeesite.common.exception;

/**
 * @ClassName AuthorizationException
 * @Description TODO
 * @Author xiaoxie
 * @Date 2020-03-26 12:01
 * @Version 1.0
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    protected AuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
