package hx.nine.eleven.file.utils;

public abstract class NestedExceptionUtils {

    public static String buildMessage( String message, Throwable cause) {
        if (cause == null) {
            return message;
        } else {
            StringBuilder sb = new StringBuilder(64);
            if (message != null) {
                sb.append(message).append("; ");
            }
            sb.append("nested exception is ").append(cause);
            return sb.toString();
        }
    }

    public static Throwable getRootCause(Throwable original) {
        if (original == null) {
            return null;
        } else {
            Throwable rootCause = null;
            for(Throwable cause = original.getCause(); cause != null && cause != rootCause; cause = cause.getCause()) {
                rootCause = cause;
            }
            return rootCause;
        }
    }

    public static Throwable getMostSpecificCause(Throwable original) {
        Throwable rootCause = getRootCause(original);
        return rootCause != null ? rootCause : original;
    }
}
