package com.hx.doop.jdbc;

import com.hx.doop.tx.TransactionManagerEntity;
import com.hx.doop.tx.TransactionManagerHolder;
import com.hx.lang.commons.utils.Builder;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManagerEntity;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManagerHolder;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 数据源,支持多数据源
 *
 * @author wml
 * @date 2024-03-13
 */
public abstract class AbstractRoutingDataSource implements DataSource {

	/**
	 * 所有数据源
	 */
	private Map<Object, DataSource> targetDataSources;
	/**
	 * 默认数据源
	 */
	private DataSource masterDataSource;

	/**
	 * 默认数据源名称
	 */
	private String masterDataSourceName;

	/**
	 * 获取当前线程使用的数据源名称
	 *
	 * @return
	 */
	public abstract String determineCurrentDataSourceName();

	public Map<Object, DataSource> getTargetDataSources() {
		return targetDataSources;
	}

	public void setTargetDataSources(Map<Object, DataSource> targetDataSources) {
		this.targetDataSources = targetDataSources;
	}

	public DataSource getMasterDataSource() {
		return masterDataSource;
	}

	public void setMasterDataSource(DataSource masterDataSource) {
		this.masterDataSource = masterDataSource;
	}

	public String getMasterDataSourceName() {
		return masterDataSourceName;
	}

	public void setMasterDataSourceName(String masterDataSourceName) {
		this.masterDataSourceName = masterDataSourceName;
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
		String dataSourceName = determineCurrentDataSourceName();
		return StringUtils.isBlank(dataSourceName)?this.masterDataSourceName:dataSourceName;
	}

	/**
	 * 获取当前数据源
	 *
	 * @return
	 */
	public DataSource currentDataSource() {
		String dataSourceName = determineCurrentDataSourceName();
		DataSource dataSource = targetDataSources.get(dataSourceName);
		return dataSource == null ? (DataSource) this.masterDataSource : dataSource;
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
		// 获取缓存
		TransactionManagerEntity entity = TransactionManagerHolder.getTransactionManager(dataSourceName);
		DataSource dataSource = targetDataSources.get(dataSourceName);
		Connection connection = null;
		if (ObjectUtils.isEmpty(entity)) {
			connection = dataSource == null ? this.masterDataSource.getConnection() : dataSource.getConnection();
			entity = Builder.of(TransactionManagerEntity::new)
					.with(TransactionManagerEntity::setConnection, connection).
					with(TransactionManagerEntity::setDataSource,dataSourceName).build();
			TransactionManagerHolder.addTransactionManager(entity);
			return connection;
		}
		connection = entity.getConnection();
		if (ObjectUtils.isEmpty(connection) || connection.isClosed()){
			connection = dataSource == null ? this.masterDataSource.getConnection() : dataSource.getConnection();
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
		String dataSourceName = determineCurrentDataSourceName();
		// 获取缓存
		JooqTransactionManagerEntity entity = JooqTransactionManagerHolder.getTransactionManager(dataSourceName);
		DataSource dataSource = targetDataSources.get(dataSourceName);
		Connection connection = null;
		if (ObjectUtils.isEmpty(entity)) {
			connection = dataSource == null ? this.masterDataSource.getConnection(username, password) :
					dataSource.getConnection(username, password);
			entity = Builder.of(JooqTransactionManagerEntity::new).
					with(JooqTransactionManagerEntity::setConnection, connection).
					with(JooqTransactionManagerEntity::setKey,dataSourceName).build();
			JooqTransactionManagerHolder.addTransactionManager(entity);
			return connection;
		}
		if (ObjectUtils.isEmpty(connection)){
			connection = dataSource == null ? this.masterDataSource.getConnection(username, password) :
					dataSource.getConnection(username, password);
			entity.setConnection(connection);
			return connection;
		}
		return entity.getConnection();
	}
}
