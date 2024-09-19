package com.hx.nine.eleven.doop.jdbc.provider;

import com.hx.nine.eleven.doop.exception.DataAccessException;
import com.hx.logchain.toolkit.util.HXLogger;

import java.sql.Connection;
import java.sql.Savepoint;

/**
 * ConnectionProvider 默认实现
 * @auth wml
 * @date 2024/3/26
 */
public class DefaultConnectionProvider implements ConnectionProvider{

	Connection connection;
	final boolean finalize;

	public DefaultConnectionProvider(Connection connection){
		this(connection,false);
	}

	DefaultConnectionProvider(Connection connection,boolean finalize){
		this.connection = connection;
		this.finalize = finalize;
	}

	@Override
	public final Connection acquire() throws DataAccessException {
		return this.connection;
	}

	@Override
	public final void release(Connection connection) throws DataAccessException {
		if (connection != null) {
			try {
				connection.close();
			}
			catch (Exception ignore) {
				HXLogger.build(this).error("error close connection");
			}
		}
	}

	@Override
	public void release() throws DataAccessException {
		if (this.connection != null) {
			try {
				this.connection.close();
			}
			catch (Exception ignore) {
				HXLogger.build(this).error("error close connection");
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (connection != null) {
			try {
				connection.close();
			}
			catch (Exception ignore) {
				HXLogger.build(this).error("error close connection");
			}
		}
		super.finalize();
	}


	/**
	 * Convenience method to access {@link Connection#commit()}.
	 */
	public final void commit() throws DataAccessException {
		try {
			HXLogger.build(this).debug("commit");
			connection.commit();
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot commit transaction", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#rollback()}.
	 */
	public final void rollback() throws DataAccessException {
		try {
			HXLogger.build(this).debug("rollback");
			connection.rollback();
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot rollback transaction", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#rollback(Savepoint)}.
	 */
	public final void rollback(Savepoint savepoint) throws DataAccessException {
		try {
			HXLogger.build(this).debug("rollback to savepoint");
			connection.rollback(savepoint);
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot rollback transaction", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#setSavepoint()}.
	 */
	public final Savepoint setSavepoint() throws DataAccessException {
		try {
			HXLogger.build(this).debug("set savepoint");
			return connection.setSavepoint();
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot set savepoint", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#setSavepoint(String)}.
	 */
	public final Savepoint setSavepoint(String name) throws DataAccessException {
		try {
			HXLogger.build(this).debug("set savepoint", name);
			return connection.setSavepoint(name);
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot set savepoint", e);
		}
	}

	/**
	 * Convenience method to access
	 * {@link Connection#releaseSavepoint(Savepoint)}.
	 */
	public final void releaseSavepoint(Savepoint savepoint) throws DataAccessException {
		try {
			HXLogger.build(this).debug("release savepoint");
			connection.releaseSavepoint(savepoint);
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot release savepoint", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#setReadOnly(boolean)}.
	 */
	public final void setReadOnly(boolean readOnly) throws DataAccessException {
		try {
			HXLogger.build(this).debug("setting read only", readOnly);
			connection.setReadOnly(readOnly);
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot set readOnly", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#isReadOnly()}.
	 */
	public final boolean isReadOnly() throws DataAccessException {
		try {
			return connection.isReadOnly();
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot get readOnly", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#setAutoCommit(boolean)}.
	 */
	public final void setAutoCommit(boolean autoCommit) throws DataAccessException {
		try {
			HXLogger.build(this).debug("setting auto commit", autoCommit);
			connection.setAutoCommit(autoCommit);
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot set autoCommit", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#getAutoCommit()}.
	 */
	public final boolean getAutoCommit() throws DataAccessException {
		try {
			return connection.getAutoCommit();
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot get autoCommit", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#setHoldability(int)}.
	 */
	public final void setHoldability(int holdability) throws DataAccessException {
		try {
			HXLogger.build(this).debug("setting holdability", holdability);
			connection.setHoldability(holdability);
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot set holdability", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#getHoldability()}.
	 */
	public final int getHoldability() throws DataAccessException {
		try {
			return connection.getHoldability();
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot get holdability", e);
		}
	}

	/**
	 * Convenience method to access
	 * {@link Connection#setTransactionIsolation(int)}.
	 */
	public final void setTransactionIsolation(int level) throws DataAccessException {
		try {
			HXLogger.build(this).debug("setting tx isolation", level);
			connection.setTransactionIsolation(level);
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot set transactionIsolation", e);
		}
	}

	/**
	 * Convenience method to access {@link Connection#getTransactionIsolation()}.
	 */
	public final int getTransactionIsolation() throws DataAccessException {
		try {
			return connection.getTransactionIsolation();
		}
		catch (Exception e) {
			throw new DataAccessException("Cannot get transactionIsolation", e);
		}
	}
}
