package com.hx.nine.eleven.bytebuddy.aop.exception;

public class ProxyCreatorException extends Exception{
	public ProxyCreatorException() {
		super();
	}

	public ProxyCreatorException(String message) {
		super(message);
	}

	public ProxyCreatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProxyCreatorException(Throwable cause) {
		super(cause);
	}

	protected ProxyCreatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
