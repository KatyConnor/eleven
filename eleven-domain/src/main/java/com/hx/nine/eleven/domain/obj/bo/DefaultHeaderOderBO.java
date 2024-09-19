package com.hx.nine.eleven.domain.obj.bo;

import javax.validation.constraints.NotBlank;

/**
 * 应用内部仿API链路调用
 */
public class DefaultHeaderOderBO extends HeaderOrderBO{

	@NotBlank(message = "交易请求流水号不能为空")
	private String reqNo;
	@NotBlank(message = "交易请求时间不能为空")
	private String tradeTime;
	@NotBlank(message = "渠道编号不能为空")
	private String channelNo;
	/**
	 * 交易跟踪ID
	 */
	private String traceId;

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
}
