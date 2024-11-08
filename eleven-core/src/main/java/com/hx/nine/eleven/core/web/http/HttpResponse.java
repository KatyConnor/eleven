package com.hx.nine.eleven.core.web.http;

import java.io.Serializable;

/**
 * HTTP response 实体结构
 * @auth wml
 * @date 2024/11/6
 */
public class HttpResponse implements Serializable {

	private Boolean success = true;

	private String code;

	private String message;

	private Object body;

	public static HttpResponse build(){
		return new HttpResponse();
	}

	public Boolean getSuccess() {
		return success;
	}

	public HttpResponse setSuccess(Boolean success) {
		this.success = success;
		return this;
	}

	public String getCode() {
		return code;
	}

	public HttpResponse setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public HttpResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public Object getBody() {
		return body;
	}

	public HttpResponse setBody(Object body) {
		this.body = body;
		return this;
	}
}
