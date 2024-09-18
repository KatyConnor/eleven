package com.hx.domain.framework.context;

import java.util.List;

public class MethodAccessEntity {


    /**
     * 需要执行的子交易码，用于标记方法
     */
    private String childTradeCode;
    /**
     * 被执行类
     */
    private Class ClassAccess;
    /**
     * 执行方法参数
     */
    private List<Object> args;

    public MethodAccessEntity(String childTradeCode, Class classAccess) {
        this.childTradeCode = childTradeCode;
        ClassAccess = classAccess;
    }

    public String getChildTradeCode() {
        return childTradeCode;
    }

    public void setChildTradeCode(String childTradeCode) {
        this.childTradeCode = childTradeCode;
    }

    public Class getClassAccess() {
        return ClassAccess;
    }

    public void setClassAccess(Class classAccess) {
        ClassAccess = classAccess;
    }

    public Object[] getArgs() {
        return args.toArray();
    }

    public void setArgs(Object arg) {
        this.args.add(args);
    }
}
