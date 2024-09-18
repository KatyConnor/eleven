package com.hx.vertx.jooq.jdbc.utils;

import com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class JdbcUrlUtils {

	public static String jdbcUrl(DataSource dataSource){
		DataSource currentDataSource = null;
		if (dataSource instanceof AbstractRoutingDataSource){
			currentDataSource = ((AbstractRoutingDataSource)dataSource).currentDataSource();
		}
		currentDataSource = currentDataSource!=null?currentDataSource:dataSource;
		if (currentDataSource instanceof HikariDataSource){
			return ((HikariDataSource)currentDataSource).getJdbcUrl();
		}
		return null;
	}
}
