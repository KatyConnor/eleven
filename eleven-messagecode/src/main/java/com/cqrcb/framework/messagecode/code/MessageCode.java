package com.cqrcb.framework.messagecode.code;

import java.io.Serializable;

/**
 * @author wangml
 * @Date 2019-03-18
 */
public class MessageCode implements Serializable {

    private String codeType;
    private String code;
    private String message;
    private int level;

    public MessageCode(String codeType,String code, String message) {
        this.codeType = codeType;
        this.code = code;
        this.message = message;
    }

    public MessageCode(String code, String message, int level) {
        this.code = code;
        this.message = message;
        this.level = level;
    }

    public String getCodeType() {
        return codeType;
    }

    public MessageCode setCodeType(String codeType) {
        this.codeType = codeType;
        return this;
    }

    public String getCode() {
        return code;
    }

    public MessageCode setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MessageCode setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public MessageCode setLevel(int level) {
        this.level = level;
        return this;
    }

}
