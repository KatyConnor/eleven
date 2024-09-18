package com.cqrcb.framework.messagecode.exception;

/**
 * @author wangml
 * @Date 2019-03-18
 */
public class SubMessageCodeException extends RuntimeException {

    protected String message;
    protected Throwable e;

    public SubMessageCodeException(String message) {
        super(message);
    }

    public SubMessageCodeException(String message, Throwable cause) {
        this.message = message;
        this.e = cause;
    }

    public SubMessageCodeException(Throwable e) {
        this.e = e;
    }

    public SubMessageCodeException( String message, Object... objects ) {
        if ( null != objects && objects.length > 0 ) {
            message = String.format( message, objects );
        }
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }
}
