package com.hx.doop.jdbc.executor;

import org.apache.ibatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * connection 执行器
 * @author wml
 * @date 2024/3/14
 */
public interface Executor {

	/**
	 * 操作执行 insert SQL
	 * @param sql     编译被执行SQL
	 * @param values  参数
	 * @return
	 */
	boolean executeInsert(String sql,Object[] values);

	/**
	 * 操作执行批量 insert SQL
	 * @param sql     编译被执行SQL
	 * @param values  参数
	 * @return
	 */
	int executeBatchInsert(String sql, List<Object[]> values);

	/**
	 *  执行 update sql
	 * @param sql     编译被执行SQL
	 * @param values  参数
	 * @return
	 */
	int executeUpdate(String sql,Object[] values);

	/**
	 * 执行批量 update sql
	 * @param sql    编译被执行SQL
	 * @param values 参数
	 * @return
	 */
	int executeBatchUpdate(String sql,List<Object[]> values);

	/**
	 * 提交事务
	 * @param required
	 * @throws SQLException
	 */
	void commit(boolean required) throws SQLException;

	/**
	 * 回滚事务
	 * @param required
	 * @throws SQLException
	 */
	void rollback(boolean required) throws SQLException;

	Transaction getTransaction();


	void close();

	void close(boolean forceRollback);

	/**
	 * 判断是否关闭
	 * @return
	 */
	boolean isClosed();
}
