package com.hx.nine.eleven.jooq.jdbc;

import com.hx.nine.eleven.commons.utils.Builder;
import com.hx.nine.eleven.commons.utils.ObjectUtils;
import com.hx.nine.eleven.jdbc.ElevenDataSource;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerEntity;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerHolder;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 数据源
 *
 * @author wml
 * @date 2023-04-23
 */
public class ElevenJooqDataSource implements ElevenDataSource {

	/**
	 *  获取当前连接，
	 * 	1、首先从缓存池中获取当前线程的数据库连接池
	 * 	2、如果没获取到，重新重数据库连接池中获取，便于使用完毕之后好关闭连接释放资源
	 * @param dataSource
	 * @return
	 */
	@Override
	public Connection currentDataSourceConnection(DataSource dataSource ,String dataSourceName) throws SQLException {
		// 获取缓存
		JooqTransactionManagerEntity entity = JooqTransactionManagerHolder.getTransactionManager(dataSourceName);
		Connection connection = null;
		if (ObjectUtils.isEmpty(entity)) {
			connection = dataSource.getConnection();
			entity = Builder.of(JooqTransactionManagerEntity::new).
					with(JooqTransactionManagerEntity::setConnection, connection).
					with(JooqTransactionManagerEntity::setKey,dataSourceName).build();
			JooqTransactionManagerHolder.addTransactionManager(entity);
			return connection;
		}

		connection = entity.getConnection();
		if (ObjectUtils.isEmpty(connection) || connection.isClosed()){
			connection = dataSource.getConnection();
			entity.setConnection(connection);
			return connection;
		}
		return entity.getConnection();
	}

	@Override
	public Connection currentDataSourceConnection(DataSource dataSource,String dataSourceName, String username, String password) throws SQLException {
		// 获取缓存
		JooqTransactionManagerEntity entity = JooqTransactionManagerHolder.getTransactionManager(dataSourceName);
		Connection connection = null;
		if (ObjectUtils.isEmpty(entity)) {
			connection = dataSource.getConnection(username, password);
			entity = Builder.of(JooqTransactionManagerEntity::new).
					with(JooqTransactionManagerEntity::setConnection, connection).
					with(JooqTransactionManagerEntity::setKey,dataSourceName).build();
			JooqTransactionManagerHolder.addTransactionManager(entity);
			return connection;
		}
		if (ObjectUtils.isEmpty(connection)){
			connection = dataSource.getConnection(username, password);
			entity.setConnection(connection);
			return connection;
		}
		return entity.getConnection();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {

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
