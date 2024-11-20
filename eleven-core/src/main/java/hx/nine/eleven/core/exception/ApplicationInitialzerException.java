package hx.nine.eleven.core.exception;


import hx.nine.eleven.commons.utils.StringUtils;

public class ApplicationInitialzerException extends RuntimeException{

  public ApplicationInitialzerException() {
	super();
  }

  public ApplicationInitialzerException(String message) {
	super(message);
  }

  public ApplicationInitialzerException(String message, String ... strs) {
	this(StringUtils.format(message,strs));
  }

  public ApplicationInitialzerException(String message, Throwable cause) {
	super(message, cause);
  }

  public ApplicationInitialzerException(Throwable cause) {
	super(cause);
  }

  protected ApplicationInitialzerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
  }
}
