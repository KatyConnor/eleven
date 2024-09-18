package com.hx.doop.mapper;

import com.hx.doop.PageParam;
import com.hx.doop.jdbc.provider.ConnectionProvider;
import com.hx.doop.jdbc.SqlSession;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 操作数据库
 * @auth wml
 * @date 2024/3/22
 */
public class SqlSessionFactroy implements SqlSession {

	private ConnectionProvider conn;

	public SqlSessionFactroy() {
	}

	public SqlSessionFactroy(ConnectionProvider conn) {
		this.conn = conn;
	}

	@Override
	public <R> R selectOne() {
		return null;
	}

	@Override
	public <R, P> R selectOne(P param) {
		return null;
	}

	@Override
	public <R, P> List<R> selectList(P param) {
		return null;
	}

	@Override
	public <R, P extends PageParam> List<R> selectPage(P param) {
		return null;
	}

	@Override
	public <R, P> Cursor<R> selectCursor(P param) throws SQLException {
		return null;
	}

	@Override
	public <T> int insertOne(T po) throws SQLException {
		return 0;
	}

	@Override
	public <T> int batchInsert(List<T> poList) throws SQLException {
		return 0;
	}

	@Override
	public <T> int update(T po) throws SQLException {
		return 0;
	}

	@Override
	public <T> int update(List<T> poList) throws SQLException {
		return 0;
	}

	@Override
	public <T, P> int executeUpdate(T po, P param) throws SQLException {
		return 0;
	}

	@Override
	public <T, P> boolean merge(List<T> poList, P param) {
		return false;
	}

	@Override
	public int delete() {
		return 0;
	}

	@Override
	public <P> int delete(P param) {
		return 0;
	}

	@Override
	public boolean drop(String name) {
		return false;
	}

	@Override
	public <T> Cursor<T> selectCursor(String statement) {
		return null;
	}

	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter) {
		return null;
	}

	@Override
	public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
		return null;
	}

	@Override
	public void commit() {

	}

	@Override
	public void commit(boolean force) {

	}

	@Override
	public void rollback() {

	}

	@Override
	public void rollback(boolean force) {

	}

	@Override
	public Connection getConnection() {
		return this.conn.acquire();
	}

	public void setConn(ConnectionProvider conn) {
		this.conn = conn;
	}

	@Override
	public void close() {
		if (this.conn != null){
			try {
				this.conn.acquire().close();
			} catch (SQLException e) {

			}
		}
	}
}
