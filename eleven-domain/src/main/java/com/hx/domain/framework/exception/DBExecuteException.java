package com.hx.domain.framework.exception;

import com.hx.lang.commons.utils.StringUtils;
import com.hx.sys.message.code.code.MessageCode;

/**
 * 数据库处理异常
 * @author wml
 * @date 2022-11-25
 */
public class DBExecuteException extends RuntimeException{

    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 错误消息
     */
    private String errorMsg;

    public DBExecuteException() {
        super();
    }

    public DBExecuteException(MessageCode messageCode) {
        super();
        this.errorCode = messageCode.getCode();
        this.errorMsg = messageCode.getMessage();
    }

    public DBExecuteException(MessageCode messageCode,String ... obj) {
        super();
        this.errorCode = messageCode.getCode();
        this.errorMsg = StringUtils.format(messageCode.getMessage(),obj);
    }

    public DBExecuteException(String errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public DBExecuteException(String errorCode, String errorMsg,String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public DBExecuteException(String errorCode,String errorMsg,String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public DBExecuteException(String errorCode,String errorMsg,Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    protected DBExecuteException(String errorCode,String errorMsg,String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
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
