package com.hx.nine.eleven.jdbc;

import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 动态数据源
 *
 * @auth wml
 * @date 2024/9/26
 */
public abstract class AbstractRoutingDataSource implements DataSource {

	/**
	 * 所有数据源
	 */
	private Map<Object, DataSource> targetDataSources;
	/**
	 * 默认数据源
	 */
	private DataSource defaultTargetDataSource;

	/**
	 * 默认数据源名称
	 */
	private String masterDataSource;

	/**
	 * 获取当前使用的数据源名称
	 *
	 * @return
	 */
	protected abstract String determineCurrentLookupKey();

	public void setTargetDataSources(Map<Object, DataSource> targetDataSources) {
		this.targetDataSources = targetDataSources;
	}

	public void setDefaultTargetDataSource(DataSource defaultTargetDataSource) {
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
		String dataSourceName = this.currentDataSourceName();
		DataSource dataSource = targetDataSources.get(dataSourceName);
		ElevenDataSource elevenDataSource = ElevenApplicationContextAware.getBean(ElevenDataSource.class);
		return elevenDataSource.currentDataSourceConnection(dataSource == null ? this.defaultTargetDataSource : dataSource, dataSourceName);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		String dataSourceName = this.currentDataSourceName();
		DataSource dataSource = targetDataSources.get(dataSourceName);
		ElevenDataSource elevenDataSource = ElevenApplicationContextAware.getBean(ElevenDataSource.class);
		return elevenDataSource.currentDataSourceConnection(dataSource == null ? this.defaultTargetDataSource : dataSource, dataSourceName, username, password);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		ElevenDataSource elevenDataSource = null;
		return elevenDataSource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		ElevenDataSource elevenDataSource = null;
		return elevenDataSource.isWrapperFor(iface);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		ElevenDataSource elevenDataSource = null;
		return elevenDataSource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		ElevenDataSource elevenDataSource = null;
		elevenDataSource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		ElevenDataSource elevenDataSource = null;
		elevenDataSource.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		ElevenDataSource elevenDataSource = null;
		return elevenDataSource.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		ElevenDataSource elevenDataSource = null;
		return elevenDataSource.getParentLogger();
	}

	/**
	 * 获取当前线程数据源名称，没指定数据源返回默认主数据源
	 *
	 * @return
	 */
	public String currentDataSourceName() {
		String dataSourceName = this.determineCurrentLookupKey();
		return StringUtils.isBlank(dataSourceName) ? this.masterDataSource : dataSourceName;
	}

	/**
	 * 获取当前数据源
	 *
	 * @return
	 */
	public DataSource currentDataSource() {
		String dataSourceName = this.determineCurrentLookupKey();
		DataSource dataSource = targetDataSources.get(dataSourceName);
		return dataSource == null ? (DataSource) this.defaultTargetDataSource : dataSource;
	}
}
