package com.hx.domain.framework.utils;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class JdbcUrlUtils {

	public static String jdbcUrl(DataSource dataSource){
		if (dataSource instanceof HikariDataSource){
			return ((HikariDataSource)dataSource).getJdbcUrl();
		}
		return null;
	}
}
