package com.cqrcb.framework.messagecode.enums;

/**
 * @author wangml
 * @Date 2019-03-18
 */
public enum LevelEnum {
    LEVEL_0(0,"0级消息，可以忽略，不需要展示给用户"),
    LEVEL_1(1,"1级消息，需要展示给用户看的消息"),
    LEVEL_2(2,"2级消息，出现错误，且不需要技术人员排查，允许的异常错误"),
    LEVEL_3(3,"3级消息，出现错误，且需要技术人员排查，不允许重复出现的异常错误情况"),
    LEVEL_4(4,"4级消息，出现错误，且非常严重的错误，需要立即处理"),
    LEVEL_5(5,"5级消息，出现错误，不可忽略的严重系统异常"),
    ;

    private int level;
    private String desc;

    LevelEnum(int level, String desc) {
        this.level = level;
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }}
