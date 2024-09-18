package com.hx.vertx.file.monitor.exception;

import com.hx.vertx.file.monitor.utils.StringUtils;

/**
 * @author wml
 * 2022-05-09
 */
public class ApplicationInitialzerException extends RuntimeException{

    public ApplicationInitialzerException() {
        super();
    }

    public ApplicationInitialzerException(String message) {
        super(message);
    }

    public ApplicationInitialzerException(String message, String ... strs) {
        this(StringUtils.format(message,strs));
    }

    public ApplicationInitialzerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationInitialzerException(Throwable cause) {
        super(cause);
    }

    protected ApplicationInitialzerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
