package com.hx.vertx.file.monitor.properties;

import com.hx.vertx.file.monitor.annotations.ConfigurationPropertisBind;

/**
 * H2数据库链接配置
 * @author wml
 * @Discription
 * @Date 2023-03-12
 */
@ConfigurationPropertisBind(prefix = "h2.db")
public class H2DBProperties {

//  private String driverClass = "org.h2.Driver";
  private String url = "jdbc:h2:file:./db/h2DB";
  private String username = "root";
  private String password = "root";
  private Integer maxConnections = 10;
  private Integer timeout = 30;
  private String schemaSql; // 初始化脚本
  private String dataSql; // 初始化数据脚本

//  public String getDriverClass() {
//    return driverClass;
//  }
//
//  public void setDriverClass(String driverClass) {
//    this.driverClass = driverClass;
//  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getMaxConnections() {
    return maxConnections;
  }

  public void setMaxConnections(Integer maxConnections) {
    this.maxConnections = maxConnections;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  public String getSchemaSql() {
    return schemaSql;
  }

  public void setSchemaSql(String schemaSql) {
    this.schemaSql = schemaSql;
  }

  public String getDataSql() {
    return dataSql;
  }

  public void setDataSql(String dataSql) {
    this.dataSql = dataSql;
  }
}
