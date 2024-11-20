package com.hx.nine.eleven.jobtask.mapperfactory;

import com.hx.lang.commons.utils.JSONObjectMapper;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.nine.eleven.logchain.toolkit.util.HXLogger;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库直连处理
 * @author wml
 * @Discription
 * @Date 2023-09-21
 */
public class DomainOOPMapperFactory {

    /**
     * 执行查询sql
     * @param sql            执行sql
     * @param values         where 参数
     * @param resultMap      返回结果集
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object[] values, Map<String, Class<?>> resultMap){
        return executeQuery(null,sql,values,resultMap);
    }

    /**
     * 执行insert 操作
     * @param sql
     * @param values
     * @return
     */
    public static boolean execute(String sql, Object[] values) {
        return execute(null,sql,values);
    }

    /**
     * 执行insert 操作
     * @param sql
     * @param values
     * @return
     */
    public static int executeUpdate(String sql, Object[] values) {
        return executeUpdate(null,sql,values);
    }

    /**
     * 执行查询sql
     * @param dataSourceName 使用的数据源
     * @param sql            执行sql
     * @param values         where 参数
     * @param resultMap      返回结果集
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String dataSourceName, String sql, Object[] values, Map<String, Class<?>> resultMap) {
        DataSource dataSource = StringUtils.isEmpty(dataSourceName)?getDefaultDataSource():getDataSource(dataSourceName);
        Connection conn = null;
        PreparedStatement pstm = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            pstm = conn.prepareStatement(sql);
            for (int i  = 1; i <= values.length; i++) {
                pstm.setObject(i, values[i-1]);
            }
            HXLogger.build(DomainOOPMapperFactory.class).debug("执行SQL入参: {}", JSONObjectMapper.toJsonString(values));
            HXLogger.build(DomainOOPMapperFactory.class).debug("执行SQL: {}",sql);
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                Map<String, Object> resMap = new HashMap<>();
                resultMap.entrySet().forEach((entry)->{
                    try {
                        resMap.put(StringUtils.convertUnderlineToHump(entry.getKey()),resultSet.getObject(entry.getKey(), entry.getValue()));
                    } catch (SQLException e) {
                        HXLogger.build(DomainOOPMapperFactory.class).error("获取数据库查询结果集异常！", e);
                    }
                });
                list.add(resMap);
            }
        } catch (SQLException e) {
            HXLogger.build(DomainOOPMapperFactory.class).error("执行数据库操作异常！", e);
        } finally {
            close(conn,pstm);
        }
        return list;
    }

    /**
     * 执行insert 操作
     * @param dataSourceName
     * @param sql
     * @param values
     * @return
     */
    public static boolean execute(String dataSourceName, String sql, Object[] values) {
        DataSource dataSource = StringUtils.isEmpty(dataSourceName)?getDefaultDataSource():getDataSource(dataSourceName);
        Connection conn = null;
        PreparedStatement pstm = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            pstm = conn.prepareStatement(sql);
            if (ObjectUtils.isNotEmpty(values)){
                for (int i  = 1; i <= values.length; i++) {
                    pstm.setObject(i, values[i-1]);
                }
            }
            HXLogger.build(DomainOOPMapperFactory.class).debug("执行SQL入参: {}", JSONObjectMapper.toJsonString(values));
            HXLogger.build(DomainOOPMapperFactory.class).debug("执行SQL: {}",sql);
            return pstm.execute();
        } catch (SQLException e) {
            HXLogger.build(DomainOOPMapperFactory.class).error("执行数据库操作异常！", e);
        } finally {
            close(conn,pstm);
        }
        return false;
    }

    /**
     * 执行insert 操作
     * @param dataSourceName
     * @param sql
     * @param values
     * @return
     */
    public static int executeUpdate(String dataSourceName, String sql, Object[] values) {
        DataSource dataSource = StringUtils.isEmpty(dataSourceName)?getDefaultDataSource():getDataSource(dataSourceName);
        Connection conn = null;
        PreparedStatement pstm = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            pstm = conn.prepareStatement(sql);
            if (ObjectUtils.isNotEmpty(values)){
                for (int i  = 1; i <= values.length; i++) {
                    pstm.setObject(i, values[i-1]);
                }
            }
            HXLogger.build(DomainOOPMapperFactory.class).debug("执行SQL入参: {}", JSONObjectMapper.toJsonString(values));
            HXLogger.build(DomainOOPMapperFactory.class).debug("执行SQL: {}",sql);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            HXLogger.build(DomainOOPMapperFactory.class).error("执行数据库操作异常！", e);
        } finally {
            close(conn,pstm);
        }
        return 0;
    }

    private static DataSource getDataSource(String dataSourceName){
        AbstractRoutingDataSource routingDataSource = VertxApplicationContextAware.getBean("dataSource");
        return routingDataSource.getResolvedDataSources().get(dataSourceName);
    }

    private static DataSource getDefaultDataSource(){
        AbstractRoutingDataSource routingDataSource = VertxApplicationContextAware.getBean("dataSource");
        return routingDataSource.getResolvedDefaultDataSource();
    }

    private static void close(Connection conn,PreparedStatement pstm){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            HXLogger.build(DomainOOPMapperFactory.class).error("关闭数据库连接异常!",e);
        }
        try {
            if (pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
            HXLogger.build(DomainOOPMapperFactory.class).error("关闭数据库连接异常!",e);
        }
    }
}
