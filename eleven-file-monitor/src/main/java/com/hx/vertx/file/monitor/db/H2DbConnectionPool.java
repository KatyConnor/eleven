package com.hx.vertx.file.monitor.db;

import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.properties.H2DBProperties;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * H2数据库链接池
 * @author wml
 * @Discription
 * @Date 2023-03-11
 */
public class H2DbConnectionPool {

  // 连接池
  private JdbcConnectionPool jdbcCP = null;

  /**
   * 初始化H2数据库和数据库连接池
   */
  private H2DbConnectionPool(){}

  public void initH2Db(){
    H2DBProperties h2DBProperties = ApplicationContextContainer.build().getProperties(H2DBProperties.class);
    jdbcCP = JdbcConnectionPool.create(h2DBProperties.getUrl(), h2DBProperties.getPassword(), h2DBProperties.getPassword());
    jdbcCP.setMaxConnections(h2DBProperties.getMaxConnections());
  }

  /**
   * 创建一个H2DbConnectionPool对象
   * @return
   */
  public static H2DbConnectionPool getInstance() {
    return Single.NEWSTANCE;
  }

  /**
   * 获取数据库链接
   * @return
   * @throws SQLException
   */
  public Connection getConnection(){
    try {
      return jdbcCP.getConnection();
    } catch (SQLException e) {
      HXLogger.build(this).error("获取数据库连接失败，{}",e);
    }
    return null;
  }

  private static final class Single{
    private static final H2DbConnectionPool NEWSTANCE = new H2DbConnectionPool();
  }
}
