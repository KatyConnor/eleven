package com.cqrcb.framework.messagecode.util;


import com.cqrcb.framework.messagecode.code.SystemCode;

/**
 * @Author wangml
 * @Date 2019-09-04
 */
public class MessageCodeApplicationMain {

    /**
     * 系统启动之前操作
     */
    public static <T extends SystemCode> void beforeStart(Class<T>[] classzz){
        ResponseCodesCheck.check(classzz);
    }

    public static void main(String[] args) {
        Class<SystemCode>[] classzz = new Class[1];
        classzz[0] = SystemCode.class;
        ResponseCodesCheck.check(classzz);
    }
}
