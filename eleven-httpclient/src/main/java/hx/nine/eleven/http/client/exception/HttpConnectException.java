package hx.nine.eleven.http.client.exception;

/**
 * @Author mingliang
 * @Date 2018-08-06 18:16
 */
public class HttpConnectException extends RuntimeException {

    public HttpConnectException() {
        super();
    }

    public HttpConnectException(String message) {
        super(message);
    }

    public HttpConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpConnectException(Throwable cause) {
        super(cause);
    }

    protected HttpConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
