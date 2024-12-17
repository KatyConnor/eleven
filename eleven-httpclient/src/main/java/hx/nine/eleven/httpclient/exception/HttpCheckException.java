package hx.nine.eleven.httpclient.exception;

import hx.nine.eleven.commons.utils.StringUtils;

/**
 * @auth wml
 * @date 2024/12/16
 */
public class HttpCheckException extends RuntimeException{
	public HttpCheckException() {
		super();
	}

	public HttpCheckException(String message,String ... values) {
		super(StringUtils.format(message,values));
	}

	public HttpCheckException(String message, Throwable cause,String ... values) {
		super(StringUtils.format(message,values), cause);
	}

	public HttpCheckException(Throwable cause) {
		super(cause);
	}

	protected HttpCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
