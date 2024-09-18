package com.hx.sys.message.code.exception;

/**
 * @author wangml
 * @Date 2019-03-18
 */
public class SubMessageCodeException extends RuntimeException {

    private String errorMsg;

    public SubMessageCodeException(String message) {
        super(message);
    }

    public SubMessageCodeException(String message, Throwable cause) {
      super(message,cause);
    }

    public SubMessageCodeException(Throwable e) {
        super(e);
    }

    public SubMessageCodeException(String message, Object... objects ) {
        if ( null != objects && objects.length > 0 ) {
            message = String.format( message, objects );
        }
        this.errorMsg = message;
    }

    @Override
    public String getMessage() {
        return errorMsg;
    }

}
