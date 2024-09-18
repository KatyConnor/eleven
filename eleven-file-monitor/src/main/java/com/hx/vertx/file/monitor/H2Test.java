package com.hx.vertx.file.monitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-11
 */
public class H2Test {
  /**
   * 以嵌入式(本地)连接方式连接H2数据库
   * // 内存模式："jdbc:h2:mem:h2DB";
   * // 文件模式: "jdbc:h2:file:h2DB";
   */
  private static final String JDBC_URL = "jdbc:h2:file:./db/h2DB";
  private static final String DRIVER_CLASS = "org.h2.Driver";
  private static final String USER = "root";
  private static final String PASSWORD = "root";

  public static void main(String[] args) throws Exception {
    //与数据库建立连接
    Class.forName(DRIVER_CLASS);
    Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    Statement statement = conn.createStatement();
    //删除表
    statement.execute("DROP TABLE IF EXISTS USER_INF");
    //创建表
    statement.execute("CREATE TABLE USER_INF(id VARCHAR(50) PRIMARY KEY, name VARCHAR(50) NOT NULL, sex VARCHAR(50) NOT NULL)");
    //插入数据
    statement.executeUpdate("INSERT INTO USER_INF VALUES('1', '程咬金', '男') ");
    statement.executeUpdate("INSERT INTO USER_INF VALUES('2', '孙尚香', '女') ");
    statement.executeUpdate("INSERT INTO USER_INF VALUES('3', '猴子', '男') ");
    //查询数据
    ResultSet resultSet = statement.executeQuery("select * from USER_INF");
    while (resultSet.next()) {
      System.out.println(resultSet.getInt("id") + ", " + resultSet.getString("name") + ", " + resultSet.getString("sex"));
    }
    //关闭连接
    statement.close();
    conn.close();
  }
}
