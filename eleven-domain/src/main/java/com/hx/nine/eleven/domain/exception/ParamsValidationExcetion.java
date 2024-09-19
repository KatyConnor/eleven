package com.hx.nine.eleven.domain.exception;

import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.sys.message.code.code.MessageCode;

/**
 * @author wangml
 * @Date 2019-09-11
 */
public class ParamsValidationExcetion extends RuntimeException{

    /**
     * 错误码
     */
    private String errorCode;
    /**
     * 错误消息
     */
    private String errorMsg;

    public ParamsValidationExcetion() {
        super();
    }

    public ParamsValidationExcetion(MessageCode messageCode) {
        super();
        this.errorCode = messageCode.getCode();
        this.errorMsg = messageCode.getMessage();
    }

    public ParamsValidationExcetion(MessageCode messageCode,String ... obj) {
        super();
        this.errorCode = messageCode.getCode();
        this.errorMsg = StringUtils.format(messageCode.getMessage(),obj);
    }

    public ParamsValidationExcetion(String message) {
        super(message);
    }

    public ParamsValidationExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamsValidationExcetion(Throwable cause) {
        super(cause);
    }

    protected ParamsValidationExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
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
