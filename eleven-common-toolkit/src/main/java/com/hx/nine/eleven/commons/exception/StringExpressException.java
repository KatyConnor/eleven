package com.hx.nine.eleven.commons.exception;

import com.hx.nine.eleven.commons.utils.StringUtils;

/**
 * @author wml
 * 2022-05-09
 */
public class StringExpressException extends RuntimeException{

    public StringExpressException() {
        super();
    }

    public StringExpressException(String message) {
        super(message);
    }

    public StringExpressException(String message, String ... strs) {
        this(StringUtils.format(message,strs));
    }

    public StringExpressException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringExpressException(Throwable cause) {
        super(cause);
    }

    protected StringExpressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
