package com.hx.nine.eleven.mybatisflex;

import com.hx.nine.eleven.jdbc.ElevenDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @auth wml
 * @date 2024/11/18
 */
public class ElevenFlexDataSource implements ElevenDataSource {

	/**
	 * 获取当前数据源连接
	 * 	1、首先从缓存池中获取当前线程的数据库连接池
	 * 	2、如果没获取到，重新重数据库连接池中获取，便于使用完毕之后好关闭连接释放资源
	 * @param dataSource
	 * @param dataSourceName
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Connection currentDataSourceConnection(DataSource dataSource, String dataSourceName) throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * 获取当前数据源连接
	 * 1、首先从缓存池中获取当前线程的数据库连接池
	 * 	2、如果没获取到，重新重数据库连接池中获取，便于使用完毕之后好关闭连接释放资源
	 * @param dataSource
	 * @param dataSourceName
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Connection currentDataSourceConnection(DataSource dataSource, String dataSourceName, String username, String password) throws SQLException {
		return dataSource.getConnection(username,password);
	}

	@Override
	public <T> T unwrap(Class<T> aClass) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> aClass) throws SQLException {
		return false;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter printWriter) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int i) throws SQLException {

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
