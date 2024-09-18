package com.hx.logchain.toolkit;

/**
 * @auth wml
 * @date 2024/3/26
 */
public interface Logger {

	/**
	 * Check if <code>TRACE</code> level logging is enabled.
	 */
	boolean isTraceEnabled();

	/**
	 * Log a message in <code>TRACE</code> level.
	 *
	 * @param message The log message
	 */
	void trace(Object message);

	/**
	 * Log a message in <code>TRACE</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 */
	void trace(Object message, Object details);

	/**
	 * Log a message in <code>TRACE</code> level.
	 *
	 * @param message The log message
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void trace(Object message, Throwable throwable);

	/**
	 * Log a message in <code>TRACE</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void trace(Object message, Object details, Throwable throwable);

	/**
	 * Check if <code>DEBUG</code> level logging is enabled.
	 */
	boolean isDebugEnabled();

	/**
	 * Log a message in <code>DEBUG</code> level.
	 *
	 * @param message The log message
	 */
	void debug(Object message);

	/**
	 * Log a message in <code>DEBUG</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 */
	void debug(Object message, Object details);

	/**
	 * Log a message in <code>DEBUG</code> level.
	 *
	 * @param message The log message
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void debug(Object message, Throwable throwable);

	/**
	 * Log a message in <code>DEBUG</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void debug(Object message, Object details, Throwable throwable);

	/**
	 * Check if <code>INFO</code> level logging is enabled.
	 */
	boolean isInfoEnabled();

	/**
	 * Log a message in <code>INFO</code> level.
	 *
	 * @param message The log message
	 */
	void info(Object message);

	/**
	 * Log a message in <code>INFO</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 */
	void info(Object message, Object details);

	/**
	 * Log a message in <code>INFO</code> level.
	 *
	 * @param message The log message
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void info(Object message, Throwable throwable);

	/**
	 * Log a message in <code>INFO</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void info(Object message, Object details, Throwable throwable);

	/**
	 * Log a message in <code>WARN</code> level.
	 *
	 * @param message The log message
	 */
	void warn(Object message);

	/**
	 * Log a message in <code>WARN</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 */
	void warn(Object message, Object details);

	/**
	 * Log a message in <code>WARN</code> level.
	 *
	 * @param message The log message
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void warn(Object message, Throwable throwable);

	/**
	 * Log a message in <code>WARN</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void warn(Object message, Object details, Throwable throwable);

	/**
	 * Log a message in <code>ERROR</code> level.
	 *
	 * @param message The log message
	 */
	void error(Object message);

	/**
	 * Log a message in <code>ERROR</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 */
	void error(Object message, Object details);

	/**
	 * Log a message in <code>ERROR</code> level.
	 *
	 * @param message The log message
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void error(Object message, Throwable throwable);

	/**
	 * Log a message in <code>ERROR</code> level.
	 *
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void error(Object message, Object details, Throwable throwable);

	/**
	 * Log a message in a given log level.
	 *
	 * @param level The log level
	 * @param message The log message
	 */
	void log(Level level, Object message);

	/**
	 * Log a message in a given log level.
	 *
	 * @param level The log level
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 */
	void log(Level level, Object message, Object details);

	/**
	 * Log a message in a given log level.
	 *
	 * @param level The log level
	 * @param message The log message
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void log(Level level, Object message, Throwable throwable);

	/**
	 * Log a message in a given log level.
	 *
	 * @param level The log level
	 * @param message The log message
	 * @param details The message details (padded to a constant-width message);
	 * @param throwable An exception whose stacktrace is logged along with the
	 *            message
	 */
	void log(Level level, Object message, Object details, Throwable throwable);

	/**
	 * The log level.
	 */
	public static enum Level {

		TRACE,
		DEBUG,
		INFO,
		WARN,
		ERROR,
		FATAL;

		public boolean supports(Level level) {
			return ordinal() <= level.ordinal();
		}
	}
}
