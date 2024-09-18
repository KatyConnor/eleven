package com.hx.vertx.jooq.jdbc;

/**
 * 数据库类型
 * @author wml
 * @date 2023-04-23
 */
public interface SQLDialect {

	String DERBY = "Derby";
	String FIREBIRD = "FIREBIRD";
	String H2 = "H2";
	String HSQLDB = "HSQLDB";
	String MARIADB = "MariaDB";
	String MYSQL = "MySQL";
	String POSTGRES = "Postgres";
	String SQLITE = "SQLite";
	String ORACLE = "Oracle";
}
