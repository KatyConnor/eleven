package hx.nine.eleven.file.client;

import java.io.Serializable;

public class HttpResponse implements Serializable {

	private String statusCode;
	private String code;
	private String message;

	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return JSONObjectMapper.toJsonString(this);
	}
}
