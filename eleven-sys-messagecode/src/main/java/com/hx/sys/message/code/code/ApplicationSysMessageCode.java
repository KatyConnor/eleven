package com.hx.sys.message.code.code;

/**
 * <p>
 * 系统响应码规范，响应码总长度为7位，由1位大写字母和6位数字组成，其格式如下；
 * [A-Z][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]
 * 其中，第1位表示错误归类，第2-3位表示操作方式，第4-5位表示操作内容，第6-7位表示原因
 * </br>
 * 示例：
 * 01、系统报错原因[客户账号不存在]，NCBS错误码[A030502]
 * 02、系统报错原因[机构级别无此操作权限】，NCBS错误码[A070407]
 * 03、系统报错原因[账户余额不足】， NCBS错误码[A080406]
 * 04、系统报错原因[报文格式错误]，NCBS错误码[C100608]
 * 05、系统报错原因[支付密码不能为空]，NCBS错误码[A080605]
 * 06、系统报错原因[对公账户通子账户主文件中无相应记录]，NCBS错误码【A030502】
 * 07、系统报错原因[此笔交易已冲正]，NCBS错误码[A070501]
 * 08、系统报错原因[收款人账户户名不相同]，NCBS错误码[A070404]
 * 09、系统报错原因[数据库连接失败]，NCBS错误码[D010102]
 * </br>
 * @author wml
 * @Description
 * @data 2022-06-02
 */
public interface ApplicationSysMessageCode {

    /**
     * 应用编码：应用框架，自定义框架等操作提示码
     */
    MessageCode A0000000000 = new MessageCode(MessageCodeType.APPLICAION,"A0000000000", "应用系统启动成功");
    MessageCode A0000010000 = new MessageCode(MessageCodeType.APPLICAION,"A0000010000", "应用系统启动失败");
    MessageCode A0001000000 = new MessageCode(MessageCodeType.APPLICAION,"A0001000000", "应用初始化模块加载成功");
    MessageCode A0001010000 = new MessageCode(MessageCodeType.APPLICAION,"A0001010000", "应用初始化模块加载失败");

    /**
     * 业务编码: 系统业务操作消息提示码
     */
    MessageCode B0000000000 = new MessageCode(MessageCodeType.BUSINESS, "B0000000000", "交易处理成功");
    MessageCode B0000000001 = new MessageCode(MessageCodeType.BUSINESS, "B0000000001", "交易处理失败");
    /**
     *
     */
    MessageCode C0000000000 = new MessageCode(MessageCodeType.BUSINESS,"C0000000000", "成功");
    /**
     * 数据库：数据操作提示编码
     */
    MessageCode D0000000000 = new MessageCode(MessageCodeType.DATABASE,"D0000000000","数据连接成功");
    MessageCode D0000010000 = new MessageCode(MessageCodeType.DATABASE,"D0000010000","数据库连失败，用户密码不匹配");
    MessageCode D0000010001 = new MessageCode(MessageCodeType.DATABASE,"D0000010001","数据库不存在");
    MessageCode D0100000000 = new MessageCode(MessageCodeType.DATABASE,"D0100000000","数据库写入数据成功");
    MessageCode D0100010000 = new MessageCode(MessageCodeType.DATABASE,"D0100010000","数据库写入数据失败");
    MessageCode D0100010001 = new MessageCode(MessageCodeType.DATABASE,"D0100010001","数据表死锁，写入数据失败");
    MessageCode D0100010002 = new MessageCode(MessageCodeType.DATABASE,"D0100010002","主键冲突，写入数据失败");
    /**
     *
     */
    MessageCode E0000000000 = new MessageCode(MessageCodeType.NETWORK,"E0000000000","成功");
    /**
     * 网络： 网络操作提示编码
     */
    MessageCode N0000000000 = new MessageCode(MessageCodeType.NETWORK,"N0000000000","网络请求连接成功");
    MessageCode N0000010000 = new MessageCode(MessageCodeType.NETWORK,"N0000010000", "网络请求连接失败");
    MessageCode N0000010001 = new MessageCode(MessageCodeType.NETWORK,"N0000010001", "网络请求连接超时");
    /**
     * 系统编码，对操作系统进行操作（cpu、内存、磁盘、文件、IO等）
     */
    MessageCode S0000000000 = new MessageCode(MessageCodeType.SYSTEM,"S0000000000","系统错误");
    /**
     * 验证类编码,如：参数非空验证，权限验证，是否登录验证等
     */
    MessageCode V0000000000 = new MessageCode(MessageCodeType.VERIFICATION,"V0000000000","必填参数校验通过");
    MessageCode V0000010000 = new MessageCode(MessageCodeType.VERIFICATION,"V0000010000","必填参数不能为空（空对象）");
    MessageCode V0001000000 = new MessageCode(MessageCodeType.VERIFICATION,"V0001000000","数据格式校验通过");
    MessageCode V0001010000 = new MessageCode(MessageCodeType.VERIFICATION,"V0001010000","数据格式校验不通过");
    MessageCode V0001010001 = new MessageCode(MessageCodeType.VERIFICATION,"V0001010001","身份证非法（身份证格式不对）");
    MessageCode V0001010002 = new MessageCode(MessageCodeType.VERIFICATION,"V0001010002","身份证长度不对");
    MessageCode V0001010003 = new MessageCode(MessageCodeType.VERIFICATION,"V0001010003","手机号非法（手机号格式不对）");
    MessageCode V0001010004 = new MessageCode(MessageCodeType.VERIFICATION,"V0001010004","手机号长度不对");

}
