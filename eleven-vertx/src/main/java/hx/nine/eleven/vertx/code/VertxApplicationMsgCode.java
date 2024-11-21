package hx.nine.eleven.vertx.code;

import hx.nine.eleven.msg.code.code.ApplicationSystemCode;
import hx.nine.eleven.msg.code.code.MessageCode;
import hx.nine.eleven.msg.code.code.SystemCodeType;

/**
 * @auth wml
 * @date 2024/11/21
 */
public interface VertxApplicationMsgCode extends ApplicationSystemCode {

	MessageCode B0000000001 = new MessageCode(SystemCodeType.BUSINESS,"B0000000001", "[用户：{}]授权认证失败，无访问权限，请检查登录");
	MessageCode B0000000002 = new MessageCode(SystemCodeType.BUSINESS,"B0000000002", "认证失败,请检查用户名和密码是否正确");
	MessageCode B0001010000 = new MessageCode(SystemCodeType.BUSINESS,"B0001010000", "[tradeCode]交易码没有匹配到对应交易，不支持此交易");

}
