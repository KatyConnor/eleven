package com.hx.nine.eleven.doop.jdbc.executor;

import com.hx.nine.eleven.doop.jdbc.SqlSession;
import org.apache.ibatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库操作实现
 * @author wml
 * @date 2024/3/14
 */
public abstract class BaseExecutor implements Executor{

	private SqlSession session = null;

	@Override
	public boolean executeInsert(String sql,Object[] values) {
		return false;
	}

	@Override
	public int executeUpdate(String sql,Object[] values) {
		return 0;
	}

	@Override
	public void commit(boolean required) throws SQLException {

	}

	@Override
	public void rollback(boolean required) throws SQLException {

	}

	@Override
	public Transaction getTransaction() {
		return null;
	}

	@Override
	public void close() {

	}

	@Override
	public void close(boolean forceRollback) {

	}

	@Override
	public boolean isClosed() {
		return false;
	}

	public Connection getConnection(){
		return this.conn;
	}
}
