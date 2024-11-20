package hx.nine.eleven.msg.code.util;


import hx.nine.eleven.msg.code.code.ApplicationSystemCode;

/**
 * @Author wangml
 * @Date 2019-09-04
 */
public class MessageCodeApplicationMain {

    /**
     * 系统启动之前操作
     */
    public static <T extends ApplicationSystemCode> void beforeStart(Class<T>[] classzz){
        ResponseCodesCheck.check(classzz);
    }

    public static void main(String[] args) {
        Class<ApplicationSystemCode>[] classzz = new Class[1];
        classzz[0] = ApplicationSystemCode.class;
        ResponseCodesCheck.check(classzz);
    }
}
