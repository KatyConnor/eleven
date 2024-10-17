package com.hx.eleven.ftpserver.db;

import com.hx.nine.eleven.core.annotations.Component;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源
 * @auth wml
 * @date 2024/10/16
 */
public class HSQLDatasource {

	private static final Logger LOGGER = LoggerFactory.getLogger(HSQLDatasource.class);

	private HikariDataSource dataSource;
	private String jdbcUrl = "jdbc:hsqldb:127.0.0.1:ftpDB";
	private String userName = "sa";
	private String password = "";
	private int maximumPoolSize = 50;

	public void createDataSource(){
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:hsqldb:mydatabase");
		config.setUsername("sa");
		config.setPassword("");
		config.setMaximumPoolSize(50); // 设置最大连接数为10
		this.dataSource = new HikariDataSource(config);
	}

	public Connection getConnection(){
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			LOGGER.error("获取 Connection 失败",e);
		}
		return null;
	}

	public HikariDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(HikariDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}
}
