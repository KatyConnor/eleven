package com.hx.nine.eleven.doop.exception;

import java.sql.SQLException;

/**
 * 数据权限异常
 * @author wml
 * @date   2024/03/14
 */
public class DataAccessException extends RuntimeException{

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}

}
