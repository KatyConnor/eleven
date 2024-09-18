package com.hx.vertx.file.monitor.exception;

/**
 * 线程处理异常类
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
