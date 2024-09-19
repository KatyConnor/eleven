package com.hx.nine.eleven.commons.json;

public enum ConfusedFieldTypeEnum {
    NOT_CONFUSED(-1),
    EMAIL(0),
    LOGIN_NAME(1),
    PHONE(2);

    private int code;

    private ConfusedFieldTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
