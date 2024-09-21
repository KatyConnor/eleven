package com.hx.nine.eleven.core.exception;

import com.hx.nine.eleven.commons.utils.StringUtils;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-19
 */
public class VertxApplicationRunException extends RuntimeException{
    public VertxApplicationRunException() {
        super();
    }

    public VertxApplicationRunException(String message) {
        super(message);
    }

    public VertxApplicationRunException(String message, String ... strs) {
        this(StringUtils.format(message,strs));
    }

    public VertxApplicationRunException(String message, Throwable cause) {
        super(message, cause);
    }

    public VertxApplicationRunException(Throwable cause) {
        super(cause);
    }

    protected VertxApplicationRunException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
