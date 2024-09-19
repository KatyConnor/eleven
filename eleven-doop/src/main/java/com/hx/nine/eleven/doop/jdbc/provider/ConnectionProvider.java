package com.hx.nine.eleven.doop.jdbc.provider;

import org.jooq.exception.DataAccessException;

import java.sql.Connection;

/**
 * JDBC Connection 连接的生命周期管理
 * TransactionProvider的实现可以选择影响ConnectionProvider的行为，
 * 例如，通过在TransactionProvider.begin(TransactionContext) 时获取连接，
 * 并仅在
 * TransactionProvider.commit(TransactionContext)
 * 或
 * TransactionProvider.rollback(TransactionContext)
 * 时释放连接。
 * @auth wml
 * @date 2024/3/26
 */
public interface ConnectionProvider {

	/**
	 * Connection 生命周期中获取一个 Connection 连接，
	 * return： 返回同一个 Connection 对象
	 * return： 线程中返回同一个 Connection 对象
	 * return： 同一个transaction 事务对象返回同一个 Connection 对象
	 * return： 每次返回一个新的 Connection 对象
	 * @return
	 * @throws DataAccessException
	 */
	Connection acquire() throws DataAccessException;

	/**
	 * Connection 生命周期结束 释放 Connection 连接
	 * Connection 生命周期结束，释放 Connection 连接
	 * @param connection
	 * @throws DataAccessException
	 */
	void release(Connection connection) throws DataAccessException;

	/**
	 * Connection 生命周期结束 释放当前dataSource中的 Connection 连接
	 * @throws DataAccessException
	 */
	void release() throws DataAccessException;
}
