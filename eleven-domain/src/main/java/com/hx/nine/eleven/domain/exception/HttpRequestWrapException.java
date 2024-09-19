package com.hx.nine.eleven.domain.exception;

import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.sys.message.code.code.MessageCode;

/**
 * @author wangml
 * @Date 2019-08-27
 */
public class HttpRequestWrapException extends RuntimeException {

    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 错误消息
     */
    private String errorMsg;

    public HttpRequestWrapException() {
        super();
    }

    public HttpRequestWrapException(MessageCode messageCode) {
        super();
        this.errorCode = messageCode.getCode();
        this.errorMsg = messageCode.getMessage();
    }

    public HttpRequestWrapException(MessageCode messageCode, String ... obj) {
        super();
        this.errorCode = messageCode.getCode();
        this.errorMsg = StringUtils.format(messageCode.getMessage(),obj);
    }

    public HttpRequestWrapException(String errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpRequestWrapException(String errorCode, String errorMsg, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpRequestWrapException(String errorCode, String errorMsg, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpRequestWrapException(String errorCode, String errorMsg, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    protected HttpRequestWrapException(String errorCode, String errorMsg, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String getMessage() {
        return "["+this.errorCode+":"+this.errorMsg+"]"+super.getMessage();
    }

}
