package hx.nine.eleven.core.exception;

import hx.nine.eleven.commons.utils.StringUtils;

/**
 * @auth wml
 * @date 2024/11/6
 */
public class ServletException  extends RuntimeException{

	public ServletException() {
		super();
	}

	public ServletException(String message) {
		super(message);
	}

	public ServletException(String message, String ... strs) {
		this(StringUtils.format(message,strs));
	}

	public ServletException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServletException(Throwable cause) {
		super(cause);
	}

	protected ServletException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
