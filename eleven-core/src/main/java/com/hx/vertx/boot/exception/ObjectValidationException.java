package com.hx.vertx.boot.exception;

import com.hx.lang.commons.utils.StringUtils;

public class ObjectValidationException extends RuntimeException{

	public ObjectValidationException() {
		super();
	}

	public ObjectValidationException(String message) {
		super(message);
	}

	public ObjectValidationException(String message, String ... strs) {
		this(StringUtils.format(message,strs));
	}

	public ObjectValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectValidationException(Throwable cause) {
		super(cause);
	}

	protected ObjectValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
