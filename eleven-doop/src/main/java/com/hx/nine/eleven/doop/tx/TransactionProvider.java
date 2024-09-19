package com.hx.nine.eleven.doop.tx;


import com.hx.nine.eleven.doop.exception.DataAccessException;

/**
 * 自定义事务管理接口
 * @auth wml
 * @date 2024/3/22
 */
public interface TransactionProvider {

	/**
	 * 开启一个事务
	 * @param ctx
	 * @throws DataAccessException
	 */
	void begin(TransactionContext ctx) throws DataAccessException;

	/**
	 *  提交事务
	 * @param ctx
	 * @throws DataAccessException
	 */
	void commit(TransactionContext ctx) throws DataAccessException;

	/**
	 *  回滚事务
	 * @param ctx
	 * @throws DataAccessException
	 */
	void rollback(TransactionContext ctx) throws DataAccessException;
}
