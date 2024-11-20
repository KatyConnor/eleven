package hx.nine.eleven.vertx.handler;

import java.io.Serializable;

public class AsyncFileResponse implements Serializable {

	/**
	 * 交易状态码
	 */
	private String statusCode;
	/**
	 * 业务编码
	 */
	private String code;
	/**
	 * 业务提示信息
	 */
	private String message;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

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

	public AsyncFileResponse successful(){
		this.statusCode = "200";
		this.code = "1";
		this.message = "文件同步成功";
		return this;
	}

	public AsyncFileResponse failure(){
		this.statusCode = "500";
		this.code = "0";
		this.message = "文件同步失败";
		return this;
	}

	public static AsyncFileResponse build(){
		return new AsyncFileResponse();
	}

}
