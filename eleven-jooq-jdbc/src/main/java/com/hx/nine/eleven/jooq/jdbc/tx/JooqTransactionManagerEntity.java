package com.hx.nine.eleven.jooq.jdbc.tx;

import org.jooq.Configuration;
import org.jooq.TransactionContext;

import java.sql.Connection;

public class JooqTransactionManagerEntity {

	private String key;
	private Configuration configuration;
	private Connection connection;
	private TransactionContext transactionContext;
	/**
	 * 事务结束，是否关闭数据库连接释放资源,
	 * m默认值false,当事务提交或者回滚之后设置值为true,关闭连接释放资源
	 */
	private boolean release;
	/**
	 * 是否开启事务,默认是需要开启事务
	 */
	private boolean transaction = true;


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public TransactionContext getTransactionContext() {
		return transactionContext;
	}

	public void setTransactionContext(TransactionContext transactionContext) {
		this.transactionContext = transactionContext;
	}

	public Boolean getTransaction() {
		return transaction;
	}

	public void setTransaction(Boolean transaction) {
		this.transaction = transaction;
	}

	public boolean isRelease() {
		return release;
	}

	public void setRelease(boolean release) {
		this.release = release;
	}
}
