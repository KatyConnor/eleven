package com.hx.thread.pool.executor.exception;

/**
 * @Author wml
 * @Date 2018-06-08 10:45
 */
public class ThreadExecuteException extends RuntimeException {

    public ThreadExecuteException() {
        super();
    }

    public ThreadExecuteException(String message) {
        super(message);
    }

    public ThreadExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThreadExecuteException(Throwable cause) {
        super(cause);
    }

    protected ThreadExecuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
