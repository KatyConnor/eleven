package hx.nine.eleven.core.exception;

import hx.nine.eleven.commons.utils.StringUtils;

/**
 * @auth wml
 * @date 2024/11/21
 */
public class UserAuthenticateException  extends RuntimeException{
	public UserAuthenticateException() {
		super();
	}

	public UserAuthenticateException(String message) {
		super(message);
	}

	public UserAuthenticateException(String message, String ... strs) {
		this(StringUtils.format(message,strs));
	}

	public UserAuthenticateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAuthenticateException(Throwable cause) {
		super(cause);
	}

	protected UserAuthenticateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
