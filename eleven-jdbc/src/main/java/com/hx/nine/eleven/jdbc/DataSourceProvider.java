package com.hx.nine.eleven.jdbc;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * @auth wml
 * @date 2024/11/18
 */
public interface DataSourceProvider {

	int maximumPoolSize(DataSource var1, Map<String,Object> config) throws SQLException;

	DataSource getDataSource(Map<String,Object> config) throws SQLException;

	void close(DataSource var1) throws SQLException;
}
