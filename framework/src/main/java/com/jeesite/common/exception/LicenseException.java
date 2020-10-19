package com.jeesite.common.exception;

public class LicenseException extends RuntimeException {
    public LicenseException() {
        super();
    }

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LicenseException(Throwable cause) {
        super(cause);
    }

    protected LicenseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
