package com.cqrcb.file.io;

import com.cqrcb.file.utils.NestedExceptionUtils;

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
