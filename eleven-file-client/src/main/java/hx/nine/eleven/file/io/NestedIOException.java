package hx.nine.eleven.file.io;

import hx.nine.eleven.file.utils.NestedExceptionUtils;

import java.io.IOException;

public class NestedIOException extends IOException {

    public NestedIOException(String msg) {
        super(msg);
    }

    public NestedIOException( String msg, Throwable cause) {
        super(msg, cause);
    }

    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
    }

    static {
        NestedExceptionUtils.class.getName();
    }
}
