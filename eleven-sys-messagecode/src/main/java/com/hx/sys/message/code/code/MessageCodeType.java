package com.hx.sys.message.code.code;

/**
 * @author wml
 * @Description
 * @data 2022-06-02
 */
public interface MessageCodeType {

    /**
     * 应用编码：应用框架，自定义框架等操作提示码
     */
    String APPLICAION = "A";
    /**
     * 业务编码: 系统业务操作消息提示码
     */
    String BUSINESS = "B";
    /**
     *
     */
    String CONNECTION = "C";
    /**
     * 数据库：数据操作提示编码
     */
    String DATABASE = "D";
    /**
     *
     */
    String EXCEPTION = "E";
    /**
     * 网络： 网络操作提示编码
     */
    String NETWORK = "N";
    /**
     * 系统编码，对操作系统进行操作（cpu、内存、磁盘、文件、IO等）
     */
    String SYSTEM = "S";
    /**
     * 验证类编码,如：参数非空验证，权限验证，是否登录验证等
     */
    String VERIFICATION = "V";
}
