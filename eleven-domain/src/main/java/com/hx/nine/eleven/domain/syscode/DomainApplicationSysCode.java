package com.hx.nine.eleven.domain.syscode;


import com.hx.sys.message.code.code.ApplicationSysMessageCode;
import com.hx.sys.message.code.code.MessageCode;
import com.hx.sys.message.code.code.MessageCodeType;

/**
 * @Author wml
 * @Date 2022-11-22
 */
public interface DomainApplicationSysCode extends ApplicationSysMessageCode {

    MessageCode A0100000000 = new MessageCode(MessageCodeType.APPLICAION,"A0100000000", "传入[domain]领域对象不能为空");
    MessageCode A0100000001 = new MessageCode(MessageCodeType.APPLICAION,"A0100000001", "[domain]领域对象[{}]必须继承[com.hx.nine.eleven.domain.obj.ddo.DomainDO]");
    MessageCode A030000000 = new MessageCode(MessageCodeType.APPLICAION,"A030000000", "数据库数据查询返回[com.hx.nine.eleven.domain.entity.DataMapperEntity<P extends BasePO>]不能null");
    MessageCode A0400000001 = new MessageCode(MessageCodeType.APPLICAION,"A0400000001", "[{}]注解配置交易码对象重复");
    MessageCode A0102010000 = new MessageCode(MessageCodeType.APPLICAION,"A0102010000", "未找到交易匹配的DTO传输对象");
    MessageCode A0200000000 = new MessageCode(MessageCodeType.APPLICAION,"A0200000000", "[{}]输入输出流关闭成功");
    MessageCode A0200000001 = new MessageCode(MessageCodeType.APPLICAION,"A0200000001", "[{}]输入输出流关闭失败/异常,{}");
    MessageCode A0200000002 = new MessageCode(MessageCodeType.APPLICAION,"A0200000002", "[{}]输入流为null");
    MessageCode A0200000003 = new MessageCode(MessageCodeType.APPLICAION,"A0200000003", "[{}]输出流为null");
    MessageCode A0300000001 = new MessageCode(MessageCodeType.APPLICAION,"A0300000001", "[{}]领域上下文为null");
    MessageCode A0300000002 = new MessageCode(MessageCodeType.APPLICAION,"A0300000002", "[{}]领域上下文传递参数为null");
    MessageCode A0300000003 = new MessageCode(MessageCodeType.APPLICAION,"A0300000003", "[{}]请求报文报文头[requestHeaderDTO]不能为null");
    MessageCode A0300000004 = new MessageCode(MessageCodeType.APPLICAION,"A0300000004", "[{}]请求报文报文[WebHttpRequest]不能为null");// @TODO 需要重新定义错误码
    MessageCode A0300000005 = new MessageCode(MessageCodeType.APPLICAION,"A0300000005", "不支持 [{}] 交易");// @TODO 需要重新定义错误码

    MessageCode B0001000000 = new MessageCode(MessageCodeType.BUSINESS,"B0001000000", "[tradeCode：{}]交易码为空");
    MessageCode B0001010000 = new MessageCode(MessageCodeType.BUSINESS,"B0001010000", "[tradeCode]交易码没有匹配到对应交易，不支持此交易");

    MessageCode B0100000000 = new MessageCode(MessageCodeType.BUSINESS,"B0100000000", "报文中读取requestBody成功");
    MessageCode B0100000001 = new MessageCode(MessageCodeType.BUSINESS,"B0100000001", "报文中读取requestBody失败/异常{}");
    MessageCode B0100000002 = new MessageCode(MessageCodeType.BUSINESS,"B0100000002", "报文中requestBody为空");
    MessageCode B0100000003 = new MessageCode(MessageCodeType.BUSINESS,"B0100000003", "报文中requestBody数据格式错误");
    MessageCode B0100000004 = new MessageCode(MessageCodeType.BUSINESS,"B0100000004", "业务交易处理异常"); // @TODO 需要重新定义错误码


    MessageCode N0300010000 = new MessageCode(MessageCodeType.NETWORK,"N0300010000", "HTTP请求HttpServletRequest中body数据为null");
    MessageCode N0300010001 = new MessageCode(MessageCodeType.BUSINESS,"N0300010001", "读取HTTP请求HttpServletRequest中body数据成功");
    MessageCode N0300010002 = new MessageCode(MessageCodeType.BUSINESS,"N0300010002", "读取HTTP请求HttpServletRequest中body数据失败/异常{}");

    // 数据库操作
    MessageCode D0300010001 = new MessageCode(MessageCodeType.DATABASE,"D0300010001", "[{}]主键ID为null");
    MessageCode D0300010002 = new MessageCode(MessageCodeType.DATABASE,"D0300010002", "[{}]version版本号不能为null");

    // 参数校验错误
    MessageCode V0000010001 = new MessageCode("V", "V0000010001", "参数校验不合法,校验结果:[{}]"); //@TODO 需要重新定义


}
