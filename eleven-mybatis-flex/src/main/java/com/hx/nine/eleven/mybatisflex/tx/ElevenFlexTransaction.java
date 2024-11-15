package com.hx.nine.eleven.mybatisflex.tx;

import com.mybatisflex.core.datasource.FlexDataSource;
import com.mybatisflex.core.transaction.TransactionContext;
import com.mybatisflex.core.util.StringUtil;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * eleven 中对 flex 事务支持
 * @auth wml
 * @date 2024/11/13
 */
public class ElevenFlexTransaction implements Transaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElevenFlexTransaction.class);

	/**
	 * 数据源
	 */
	private final FlexDataSource dataSource;
	/**
	 * 当前 connection 是否已经在事务中
	 */
	private Boolean isConnectionTransactional;
	/**
	 * 是否自动提交
	 */
	private Boolean autoCommit;
	/**
	 * 数据库连接
	 */
	private Connection connection;
	/**
	 *  事务隔离级别
	 */
	protected TransactionIsolationLevel level;

	public ElevenFlexTransaction(Connection connection) {
		this.connection = connection;
		this.dataSource = null;
	}

	public ElevenFlexTransaction(FlexDataSource dataSource,TransactionIsolationLevel desiredLevel, boolean desiredAutoCommit) {
		this.dataSource = dataSource;
		this.level = desiredLevel;
		this.autoCommit = desiredAutoCommit;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (this.isConnectionTransactional == null && this.connection == null) {
			connection = dataSource.getConnection();
			if (this.level != null) {
				this.connection.setTransactionIsolation(this.level.getLevel());
			}
			if (this.autoCommit != null){
				this.setDesiredAutoCommit(this.autoCommit);
			}
			autoCommit = connection.getAutoCommit();
			isConnectionTransactional = StringUtil.hasText(TransactionContext.getXID());
			return connection;
		}
		// 在事务中，通过 FlexDataSource 去获取 connection
		// FlexDataSource 内部会进行 connection 缓存以及多数据源下的 key 判断
		else if (isConnectionTransactional) {
			return dataSource.getConnection();
		}
		// 非事务，返回当前链接
		return connection;
	}

	@Override
	public void commit() throws SQLException {
		if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("commit JDBC Connection [" + this.connection + "]");
			}
			this.connection.commit();
		}
	}

	@Override
	public void rollback() throws SQLException {
		if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Rolling back JDBC Connection [" + this.connection + "]");
			}
			this.connection.rollback();
		}
	}

	@Override
	public void close() throws SQLException {
		if (this.connection != null && !this.isConnectionTransactional) {
			connection.close();
		}
	}

	@Override
	public Integer getTimeout() throws SQLException {
		return null;
	}

	protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
		try {
			if (this.connection.getAutoCommit() != desiredAutoCommit) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Setting autocommit to " + desiredAutoCommit + " on JDBC Connection [" + this.connection + "]");
				}
				this.connection.setAutoCommit(desiredAutoCommit);
			}

		} catch (SQLException var3) {
			throw new TransactionException("Error configuring AutoCommit.  Your driver may not support getAutoCommit() or setAutoCommit(). Requested setting: " + desiredAutoCommit + ".  Cause: " + var3, var3);
		}
	}
}
