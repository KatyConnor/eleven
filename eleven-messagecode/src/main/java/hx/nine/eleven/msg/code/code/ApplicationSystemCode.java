package hx.nine.eleven.msg.code.code;

/**
 * <p>
 * 系统响应码规范，响应码总长度为7位，由1位大写字母和6位数字组成，其格式如下；
 * [A-Z][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]
 * 其中，第1位表示错误归类，第2-3位表示操作方式，第4-5位表示操作内容，第6-7位表示原因
 * 例如： </br>
 * 错误归类（第1位）	操作方式（第2-3位）	操作内容（第4-5位）	操作结果（第6-7位）           结果描述（第8-11位）
 * A - 应用          01 - 连接           01 - 执行             01 - 成功                       0001
                                                              02 - 失败
                                        02 - 数据库（连接池）   03 - 存在（重复）
 * B - 业务           02 - 关闭           03 - 用户             04 - 不存在
 * D - 数据库         03 - 读取           04 - 表               05 - 匹配（相同）
 * N - 网络通讯        04 - 写入           05 - 字段             06 - 不匹配
 * S - 系统           05 - 更新           06 - 记录             07 - 零（空值）
 *                   06 - 删除           07 - 数据             08 - 超限（数值越界）
 *                   07 - 比较           08 - 端口             09 - 权限错误
 *                   08 - 计算           09 - 硬盘             10 - 格式错误
 *                   09 - 校验           10 - 内存
 *                   10 - 转换
 * C -
 *
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
 *
 *
 * @author wangml
 * @date 2019-08-14
 */
public interface ApplicationSystemCode {

    /**
     * 应用编码：应用框架，自定义框架等操作提示码
     */
    MessageCode A0000000000 = new MessageCode(SystemCodeType.APPLICAION,"A0000000000", "应用系统启动成功");
    MessageCode A0000010000 = new MessageCode(SystemCodeType.APPLICAION,"A0000010000", "应用系统启动失败");
    MessageCode A0001000000 = new MessageCode(SystemCodeType.APPLICAION,"A0001000000", "应用初始化模块加载成功");
    MessageCode A0001010000 = new MessageCode(SystemCodeType.APPLICAION,"A0001010000", "应用初始化模块加载失败");
    MessageCode A0101010001 = new MessageCode(SystemCodeType.APPLICAION,"A0101010001", "应用处理成功");
    MessageCode A0101010002 = new MessageCode(SystemCodeType.APPLICAION,"A0101020001", "应用处理失败");

    /**
     * 业务编码: 系统业务操作消息提示码
     */
    MessageCode B0000000000 = new MessageCode(SystemCodeType.BUSINESS, "B0000000000", "交易处理成功");
    MessageCode B0000000001 = new MessageCode(SystemCodeType.BUSINESS, "B0000000001", "交易处理失败");

    /**
     *
     */
    MessageCode C0000000000 = new MessageCode(SystemCodeType.BUSINESS,"C0000000000", "成功");
    MessageCode C0101010002 = new MessageCode(SystemCodeType.BUSINESS,"C0101020001", "必填字段不能为空");

    /**
     * 数据库：数据操作提示编码
     */
    MessageCode D0000000000 = new MessageCode(SystemCodeType.DATABASE,"D0000000000","数据连接成功");
    MessageCode D0000010000 = new MessageCode(SystemCodeType.DATABASE,"D0000010000","数据库连失败，用户密码不匹配");
    MessageCode D0000010001 = new MessageCode(SystemCodeType.DATABASE,"D0000010001","数据库不存在");
    MessageCode D0100000000 = new MessageCode(SystemCodeType.DATABASE,"D0100000000","数据库写入数据成功");
    MessageCode D0100010000 = new MessageCode(SystemCodeType.DATABASE,"D0100010000","数据库写入数据失败");
    MessageCode D0100010001 = new MessageCode(SystemCodeType.DATABASE,"D0100010001","数据表死锁，写入数据失败");
    MessageCode D0100010002 = new MessageCode(SystemCodeType.DATABASE,"D0100010002","主键冲突，写入数据失败");
    MessageCode D0306040001 = new MessageCode(SystemCodeType.DATABASE,"D0306040001","数据不存在");
    MessageCode D0306040002 = new MessageCode(SystemCodeType.DATABASE,"D0306040002","数据库连接异常");
    MessageCode D0306040003 = new MessageCode(SystemCodeType.DATABASE,"D0306040003","数据库主键冲突");
    MessageCode D0306040004 = new MessageCode(SystemCodeType.DATABASE,"D0306040004","数据库数据插入失败");
    MessageCode D0306040005 = new MessageCode(SystemCodeType.DATABASE,"D0306040005","数据库数据更新失败");

    /**
     *
     */
    MessageCode E0000000000 = new MessageCode(SystemCodeType.NETWORK,"E0000000000","成功");

    /**
     * 网络： 网络操作提示编码
     */
    MessageCode N0000000000 = new MessageCode(SystemCodeType.NETWORK,"N0000000000","网络请求连接成功");
    MessageCode N0000010000 = new MessageCode(SystemCodeType.NETWORK,"N0000010000", "网络请求连接失败");
    MessageCode N0000010001 = new MessageCode(SystemCodeType.NETWORK,"N0000010001", "网络请求连接超时");
    MessageCode N0101010001 = new MessageCode(SystemCodeType.NETWORK,"N0101010001","网络连接失败");
    MessageCode N0101010002 = new MessageCode(SystemCodeType.NETWORK,"N0101010002", "通讯连接成功");
    MessageCode N0101010003 = new MessageCode(SystemCodeType.NETWORK,"N0101010003", "通讯连接失败");
    MessageCode N0101010004 = new MessageCode(SystemCodeType.NETWORK,"N0101010004", "通讯连接超时");

    /**
     * 系统编码，对操作系统进行操作（cpu、内存、磁盘、文件、IO等）
     */
    MessageCode S0000000000 = new MessageCode(SystemCodeType.SYSTEM,"S0000000000","系统错误");
    MessageCode S0101010001 = new MessageCode(SystemCodeType.SYSTEM,"S0101010001","系统错误");

    /**
     * 验证类编码,如：参数非空验证，权限验证，是否登录验证等
     */
    MessageCode V0000000000 = new MessageCode(SystemCodeType.VERIFICATION,"V0000000000","必填参数校验通过");
    MessageCode V0000010000 = new MessageCode(SystemCodeType.VERIFICATION,"V0000010000","必填参数不能为空（空对象）");
    MessageCode V0001000000 = new MessageCode(SystemCodeType.VERIFICATION,"V0001000000","数据格式校验通过");
    MessageCode V0001010000 = new MessageCode(SystemCodeType.VERIFICATION,"V0001010000","数据格式校验不通过");
    MessageCode V0001010001 = new MessageCode(SystemCodeType.VERIFICATION,"V0001010001","身份证非法（身份证格式不对）");
    MessageCode V0001010002 = new MessageCode(SystemCodeType.VERIFICATION,"V0001010002","身份证长度不对");
    MessageCode V0001010003 = new MessageCode(SystemCodeType.VERIFICATION,"V0001010003","手机号非法（手机号格式不对）");
    MessageCode V0001010004 = new MessageCode(SystemCodeType.VERIFICATION,"V0001010004","手机号长度不对");


}
