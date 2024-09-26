package com.hx.nine.eleven.core.exception;

import com.hx.nine.eleven.commons.utils.StringUtils;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-19
 */
public class ElevenApplicationRunException extends RuntimeException{
    public ElevenApplicationRunException() {
        super();
    }

    public ElevenApplicationRunException(String message) {
        super(message);
    }

    public ElevenApplicationRunException(String message, String ... strs) {
        this(StringUtils.format(message,strs));
    }

    public ElevenApplicationRunException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElevenApplicationRunException(Throwable cause) {
        super(cause);
    }

    protected ElevenApplicationRunException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
