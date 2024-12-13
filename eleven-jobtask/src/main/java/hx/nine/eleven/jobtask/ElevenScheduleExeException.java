package hx.nine.eleven.jobtask;

/**
 * @auth wml
 * @date 2024/12/13
 */
public class ElevenScheduleExeException extends RuntimeException{

	public ElevenScheduleExeException() {
		super();
	}

	public ElevenScheduleExeException(String message) {
		super(message);
	}

	public ElevenScheduleExeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ElevenScheduleExeException(Throwable cause) {
		super(cause);
	}

	protected ElevenScheduleExeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
