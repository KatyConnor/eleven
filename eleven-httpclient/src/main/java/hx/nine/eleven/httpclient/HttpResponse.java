package hx.nine.eleven.httpclient;

import org.apache.hc.core5.http.HttpStatus;
import java.io.Serializable;

/**
 * @auth wml
 * @date 2024/12/16
 */
public class HttpResponse<T> implements Serializable {

	/** 状态码 */
	private int code;
	/** 返回描述 */
	private String message;
	/** 业务状态码 */
	private String statusCode;
	/** 授权token */
	private String authorizedToken;
	/** 返回数据 */
	private T body;

	public static HttpResponse build(){
		return new HttpResponse();
	}

	public int getCode() {
		return code;
	}

	public HttpResponse setCode(int code) {
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

	public T getBody() {
		return body;
	}

	public HttpResponse setBody(T body) {
		this.body = body;
		return this;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public HttpResponse setStatusCode(String statusCode) {
		this.statusCode = statusCode;
		return this;
	}

	public String getAuthorizedToken() {
		return authorizedToken;
	}

	public HttpResponse setAuthorizedToken(String authorizedToken) {
		this.authorizedToken = authorizedToken;
		return this;
	}

	public void successful(){
		this.code = HttpStatus.SC_OK;
		this.message = "请求处理成功";
	}

	public void failure(){
		this.code = HttpStatus.SC_OK;
		this.message = "请求处理失败";
	}
}
