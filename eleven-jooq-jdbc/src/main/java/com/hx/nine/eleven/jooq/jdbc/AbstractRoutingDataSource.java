package com.hx.nine.eleven.jooq.jdbc;

import  com.hx.nine.eleven.commons.utils.Builder;
import  com.hx.nine.eleven.commons.utils.ObjectUtils;
import  com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerEntity;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerHolder;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 数据源
 *
 * @author wml
 * @date 2023-04-23
 */
public abstract class AbstractRoutingDataSource implements DataSource {

	/**
	 * 所有数据源
	 */
	private Map<Object, DataSource> targetDataSources;
	/**
	 * 默认数据源
	 */
	private Object defaultTargetDataSource;

	/**
	 * 默认数据源名称
	 */
	private String masterDataSource;

	/**
	 * 获取当前使用的数据源
	 *
	 * @return
	 */
	protected abstract Object determineCurrentLookupKey();

	public void setTargetDataSources(Map<Object, DataSource> targetDataSources) {
		this.targetDataSources = targetDataSources;
	}

	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		this.defaultTargetDataSource = defaultTargetDataSource;
	}

	public DataSource getResolvedDefaultDataSource() {
		return (DataSource) this.defaultTargetDataSource;
	}

	public Map<Object, DataSource> getResolvedDataSources() {
		return this.targetDataSources;
	}

	public String getMasterDataSource() {
		return masterDataSource;
	}

	public void setMasterDataSource(String masterDataSource) {
		this.masterDataSource = masterDataSource;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.currentDataSourceConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return this.currentDataSourceConnection(username, password);
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

	/**
	 * 获取当前线程数据源名称，没指定数据源返回默认主数据源
	 * @return
	 */
	public String currentDataSourceName(){
		String dataSourceName = StringUtils.valueOf(determineCurrentLookupKey());
		return StringUtils.isBlank(dataSourceName)?this.masterDataSource:dataSourceName;
	}

	/**
	 * 获取当前数据源
	 *
	 * @return
	 */
	public DataSource currentDataSource() {
		String dataSourceName = StringUtils.valueOf(determineCurrentLookupKey());
		DataSource dataSource = targetDataSources.get(dataSourceName);
		return dataSource == null ? (DataSource) this.defaultTargetDataSource : dataSource;
	}

	/**
	 * 获取当前连接，
	 * 1、首先从缓存池中获取当前线程的数据库连接池
	 * 2、如果没获取到，重新重数据库连接池中获取，便于使用完毕之后好关闭连接释放资源
	 *
	 * @return
	 * @throws SQLException
	 */
	private Connection currentDataSourceConnection() throws SQLException {
		String dataSourceName = this.currentDataSourceName();
		DataSource dataSource = targetDataSources.get(dataSourceName);
		// 获取缓存
		JooqTransactionManagerEntity entity = JooqTransactionManagerHolder.getTransactionManager(dataSourceName);
		Connection connection = null;
		if (ObjectUtils.isEmpty(entity)) {
			connection = dataSource == null ? ((DataSource) this.defaultTargetDataSource).getConnection() : dataSource.getConnection();
			entity = Builder.of(JooqTransactionManagerEntity::new).
					with(JooqTransactionManagerEntity::setConnection, connection).
					with(JooqTransactionManagerEntity::setKey,dataSourceName).build();
			JooqTransactionManagerHolder.addTransactionManager(entity);
			return connection;
		}
		 connection = entity.getConnection();
		if (ObjectUtils.isEmpty(connection) || connection.isClosed()){
			connection = dataSource == null ? ((DataSource) this.defaultTargetDataSource).getConnection() : dataSource.getConnection();
			entity.setConnection(connection);
			return connection;
		}
		return entity.getConnection();
	}

	/**
	 * 获取连接
	 *
	 * @param username 用户名
	 * @param password 连接秘密
	 * @return
	 * @throws SQLException
	 */
	private Connection currentDataSourceConnection(String username, String password) throws SQLException {
		String dataSourceName = String.valueOf(determineCurrentLookupKey());
		DataSource dataSource = targetDataSources.get(dataSourceName);

		// 获取缓存
		JooqTransactionManagerEntity entity = JooqTransactionManagerHolder.getTransactionManager(dataSourceName);
		Connection connection = null;
		if (ObjectUtils.isEmpty(entity)) {
			connection = dataSource == null ? ((DataSource) this.defaultTargetDataSource).getConnection(username, password) :
					dataSource.getConnection(username, password);
			entity = Builder.of(JooqTransactionManagerEntity::new).
					with(JooqTransactionManagerEntity::setConnection, connection).
					with(JooqTransactionManagerEntity::setKey,dataSourceName).build();
			JooqTransactionManagerHolder.addTransactionManager(entity);
			return connection;
		}
		if (ObjectUtils.isEmpty(connection)){
			connection = dataSource == null ? ((DataSource) this.defaultTargetDataSource).getConnection(username, password) :
					dataSource.getConnection(username, password);
			entity.setConnection(connection);
			return connection;
		}
		return entity.getConnection();
	}
}
