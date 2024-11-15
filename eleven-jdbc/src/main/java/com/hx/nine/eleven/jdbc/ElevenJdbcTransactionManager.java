package com.hx.nine.eleven.jdbc;

/**
 * @auth wml
 * @date 2024/11/12
 */
public interface ElevenJdbcTransactionManager {

	/**
	 * 开启事务
	 */
	void begin();

	/**
	 * 提交事务
	 */
	void commit();

	/**
	 * 回滚事务
	 */
	void rollback();
}
