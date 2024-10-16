package com.hx.eleven.ftpserver.db;

/**
 * 数据源
 * @auth wml
 * @date 2024/10/16
 */
public class HSQLDatasource {

	public static void main(String[] args) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:hsqldb:mydatabase");
		config.setUsername("sa");
		config.setPassword("");
		config.setMaximumPoolSize(10); // 设置最大连接数为10
		HikariDataSource ds = new HikariDataSource(config);
		// 使用连接池获取连接
		try (var conn = ds.getConnection()) {
			System.out.println("Connected to the database!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		// ... 启动HSQLDB服务器代码

		try {
			// 连接数据库
			Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/mydb", "SA", "");

			// 创建一个表
			Statement s = c.createStatement();
			s.execute("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(255))");

			// 关闭连接
			s.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
