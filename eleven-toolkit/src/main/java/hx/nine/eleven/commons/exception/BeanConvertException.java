package hx.nine.eleven.commons.exception;

import hx.nine.eleven.commons.utils.StringUtils;

public class BeanConvertException extends RuntimeException{

    public BeanConvertException() {
        super();
    }

    public BeanConvertException(String message) {
        super(message);
    }

    public BeanConvertException(String message, String ... strs) {
        this(StringUtils.format(message,strs));
    }

    public BeanConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanConvertException(Throwable cause) {
        super(cause);
    }

    protected BeanConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
