package com.hx.doop.tx;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.Deque;

public class TransactionManagerEntity {

	/**
	 *  数据源
	 */
	private String dataSource;
	/**
	 * 数据库连接
	 */
	private Connection connection;
	/**
	 * 事务管理上下文,使用队列进行存储，便于嵌套事务和非嵌套事务的管理
	 */
	private Deque<TransactionContext> transactionContext;
	/**
	 * 事务结束，是否关闭数据库连接释放资源,
	 * m默认值false,当事务提交或者回滚之后设置值为true,关闭连接释放资源
	 */
	private boolean release;
	/**
	 * 是否已经开启事务,默认未开启事务
	 */
	private boolean transaction;

	public TransactionManagerEntity(){
		this.transactionContext = new ArrayDeque<>();
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public TransactionContext getTransactionContext() {
		return this.transactionContext.peekFirst();
	}

	public void addTransactionContext(TransactionContext transactionContext) {
		this.transactionContext.push(transactionContext);
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
