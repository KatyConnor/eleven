package hx.nine.eleven.file.client;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RequestHeaderDTO implements Serializable {

	private String tradeCode;
	private String subTradeCode;
	private String headerCode = "FSSM_HEADER";
	private String reqNo;
	private String tradeTime = LocalDateTime.now().toString();

	public String getTradeCode() {
		return this.tradeCode;
	}

	public RequestHeaderDTO setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
		return this;
	}

	public String getHeaderCode() {
		return this.headerCode;
	}

	public RequestHeaderDTO setHeaderCode(String headerCode) {
		this.headerCode = headerCode;
		return this;
	}

	public String getSubTradeCode() {
		return this.subTradeCode;
	}

	public RequestHeaderDTO setSubTradeCode(String subTradeCode) {
		this.subTradeCode = subTradeCode;
		return this;
	}

	public String getReqNo() {
		return reqNo;
	}

	public RequestHeaderDTO setReqNo(String reqNo) {
		this.reqNo = reqNo;
		return this;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public RequestHeaderDTO setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
		return this;
	}

	public static RequestHeaderDTO build(){
		return new RequestHeaderDTO();
	}

	@Override
	public String toString() {
		return JSONObjectMapper.toJsonString(this);
	}
}
