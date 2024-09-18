package com.hx.vertx.file.monitor.db;

import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import com.hx.vertx.file.monitor.mapper.TableMapper;
import com.hx.vertx.file.monitor.properties.H2DBProperties;
import com.hx.vertx.file.monitor.utils.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 数据库初始化脚本
 *
 * @author wml
 * @Discription
 * @Date 2023-03-12
 */
public class D2DbTableInitializer {

  public void initH2DbTable() {
    H2DBProperties properties = ApplicationContextContainer.build().getProperties(H2DBProperties.class);
    String schemaSql = properties.getSchemaSql(); // 执行建表语句
    String dataSql = properties.getDataSql();// 插入数据
    //初始化表
    Map<String, String> tableExist = null;
    if (StringUtils.isNotEmpty(schemaSql)) {
      tableExist = createTable(schemaSql);
    }
    // 初始化数据
    if (StringUtils.isNotEmpty(dataSql)) {
      insertData(dataSql, tableExist);
    }
  }

  /**
   * 初始化表
   *
   * @param schemaSql
   */
  private Map<String, String> createTable(String schemaSql) {
    Map<String, String> tableExist = new HashMap<>();
    // 初始化脚本不为空，检查初始化表
    Connection connection = DataMapperFactory.getH2DbConnectionPool().getConnection();
    Statement statement = null;
    DatabaseMetaData meta = null;
    ResultSet rsTables = null;
    try {
      statement = connection.createStatement();
      meta = connection.getMetaData();
      String schemaSqlPath = null;
      if (schemaSql.startsWith("classpath:") || schemaSql.startsWith("classpath*:")) {
        int start = schemaSql.startsWith("classpath:") ? 10 : 11;
        String schema = schemaSql.substring(start);
        schemaSqlPath = Thread.currentThread().getContextClassLoader().getResource(schema.startsWith("/") ? schema.substring(1) : schema).getPath();
      } else {
        schemaSqlPath = schemaSql;
      }
      int osNum = osName();
      List<String> strList = Files.readAllLines(Paths.get(osNum>0 && osNum == 1?schemaSqlPath.substring(1):schemaSqlPath), StandardCharsets.UTF_8);
      StringBuilder table = new StringBuilder();
      StringBuilder createTable = new StringBuilder();
      boolean end = false;
      int index = 1;
      for (String str : strList) {
        if (index == 1) {
          if (str.startsWith("@start")) {
            index += 1;
            continue;
          } else {
            index += 1;
            throw new RuntimeException("");
          }
        }
        if (str.startsWith("@table:")) {
          int first = str.indexOf("@|@");
          table.append(str.substring(7, first));
          if (!str.endsWith("@|@")) {
            createTable.append(str.substring(first + 3)).append("\n");
          } else {
            createTable.append(str.substring(first + 3, str.length() - 3));
            end = true;
          }
        } else {
          if (!str.endsWith("@|@")) {
            createTable.append(str).append("\n");
          } else {
            createTable.append(str.substring(0, str.length() - 3));
            end = true;
          }
        }

        String tableName = table.toString().trim();
        if (end) {
          rsTables = meta.getTables(null, null, tableName, new String[]{"TABLE"});
          if (!rsTables.next()) {
            statement.execute(createTable.toString());
          } else {
            tableExist.put(tableName, tableName);
          }
          end = false;
          table.delete(0, table.length());
          createTable.delete(0, createTable.length());
          rsTables.close();
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        releaseConnection(connection, statement, null);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return tableExist;
  }

  private void insertData(String dataSql, Map<String, String> tableExist) {
    // 初始化脚本不为空，检查初始化表
    Connection connection = null;
    Statement statement = null;
    try {
      connection = DataMapperFactory.getH2DbConnectionPool().getConnection();
      statement = connection.createStatement();
      String dataSqlPath = null;
      if (dataSql.startsWith("classpath:") || dataSql.startsWith("classpath*:")) {
        int start = dataSql.startsWith("classpath:") ? 10 : 11;
        String data = dataSql.substring(start);
        dataSqlPath = Thread.currentThread().getContextClassLoader().getResource(data.startsWith("/") ? data.substring(1) : data).getPath();
      } else {
        dataSqlPath = dataSql;
      }
      int osNum = osName();
      List<String> strList = Files.readAllLines(Paths.get(osNum>0 && osNum == 1?dataSqlPath.substring(1):dataSqlPath), StandardCharsets.UTF_8);
      StringBuilder sql = new StringBuilder();
      boolean end = false;
      boolean reexe = false;
      int index = 1;
      for (String str : strList) {
        // 检查第一行，是否有@start标志
        if (index == 1) {
          if (str.startsWith("@start")) {
            index += 1;
            continue;
          } else {
            index += 1;
            throw new RuntimeException("");
          }
        }

        // 第二行开始解析SQL语句，检查时候指定表
        if (str.startsWith("@table_bigen")){
          // 检查表，检查时候覆盖刷新数据
          String[] startSql = str.split(":");
          if (tableExist.containsKey(startSql[1])) {
            // 当前表已经存在，并且不重复执行，需要检查表中数据，如果数据大于0，表中已经初始化过不在进行初始化，其他情况执行初始化语句
            reexe = !Boolean.valueOf(startSql[2])? TableMapper.build().selectCount(startSql[1]) > 0?false:true:true;
          }
        }else {
          // 检查时候读取结束
          if (str.startsWith("@table_end")){
            reexe = false;
           continue;
          }else if (str.endsWith(";")){
            end = true;
            sql.append(str);
          }else {
            sql.append(str).append("\n");
          }
        }

        if (end && reexe) {
          statement.executeUpdate(sql.toString());
          end = false;
          sql.delete(0, sql.length());
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        releaseConnection(connection, statement, null);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private void releaseConnection(Connection conn, Statement stmt, ResultSet rs) throws SQLException {
    if (rs != null) {
      rs.close();
    }
    if (stmt != null) {
      stmt.close();
    }
    if (conn != null) {
      conn.close();
    }
  }

  private int osName(){
    int result = -1;
    String osName = System.getProperty("os.name");
    String osNameLowerCase = osName.toLowerCase(Locale.ROOT);
    if (osNameLowerCase.startsWith("mac") || osNameLowerCase.startsWith("darwin")) {
      result = 0;
    } else if (osNameLowerCase.startsWith("wind")) {
      result = 1;
    } else if (osNameLowerCase.startsWith("linux")) {
      result = 2;
    }
    return result;
  }
}
