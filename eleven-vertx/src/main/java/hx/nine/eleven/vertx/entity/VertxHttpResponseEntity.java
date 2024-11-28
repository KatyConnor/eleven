package hx.nine.eleven.vertx.entity;

import java.io.Serializable;

/**
 * @auth wml
 * @date 2024/11/28
 */
public class VertxHttpResponseEntity<H,B> implements Serializable {

	private H responseHeader;

	/**
	 * 返回报文体（body）
	 */
	private B responseBody;

	public H getResponseHeader() {
		return responseHeader;
	}

	public VertxHttpResponseEntity setResponseHeader(H responseHeader) {
		this.responseHeader = responseHeader;
		return this;
	}

	public B getResponseBody() {
		return responseBody;
	}

	public VertxHttpResponseEntity setResponseBody(B responseBody) {
		this.responseBody = responseBody;
		return this;
	}
}
