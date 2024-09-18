package com.hx.sys.message.code.util;


import com.hx.sys.message.code.code.ApplicationSysMessageCode;

/**
 * @Author wangml
 * @Date 2019-09-04
 */
public class MessageCodeApplicationMain {

    /**
     * 系统启动之前操作
     */
    public static <T extends ApplicationSysMessageCode> void beforeStart(Class<T>[] classzz){
        ResponseCodesCheck.check(classzz);
    }
}
