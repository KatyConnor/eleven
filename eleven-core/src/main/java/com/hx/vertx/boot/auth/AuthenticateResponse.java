package com.hx.vertx.boot.auth;

import java.io.Serializable;

public class AuthenticateResponse implements Serializable {

	/**
	 * 业务编码
	 */
	private String code;
	/**
	 * 业务提示信息
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
