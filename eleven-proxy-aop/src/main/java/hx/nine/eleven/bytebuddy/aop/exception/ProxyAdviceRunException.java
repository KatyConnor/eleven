package hx.nine.eleven.bytebuddy.aop.exception;

public class ProxyAdviceRunException extends Exception {

	public ProxyAdviceRunException() {
		super();
	}

	public ProxyAdviceRunException(String message) {
		super(message);
	}

	public ProxyAdviceRunException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProxyAdviceRunException(Throwable cause) {
		super(cause);
	}

	protected ProxyAdviceRunException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
