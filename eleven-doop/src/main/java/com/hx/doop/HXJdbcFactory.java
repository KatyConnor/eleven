package com.hx.dodm;

import com.hx.logchain.toolkit.util.HXLogger;
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
 * @author lc
 * @Discription
 * @Date 2023-09-21
 */
public class HXJdbcFactory {

    /**
     * 执行查询sql
     * @param dataSourceName 使用的数据源
     * @param sql            执行sql
     * @param values         where 参数
     * @param resultMap      返回结果集
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String dataSourceName, String sql, Object[] values, Map<String, Class<?>> resultMap) {
        AbstractRoutingDataSource routingDataSource = VertxApplicationContextAware.getBean("dataSource");
        DataSource dataSource = routingDataSource.getResolvedDataSources().get(dataSourceName);
        Connection conn = null;
        PreparedStatement pstm = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            conn.setTransactionIsolation(1);//设置事务级别
            pstm = conn.prepareStatement(sql);
            for (int i  = 1; i <= values.length; i++) {
                pstm.setObject(i, values[i-1]);
            }
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                Map<String, Object> resMap = new HashMap<>();
                resultMap.entrySet().forEach((entry)->{
                    try {
                        resMap.put(entry.getKey(),resultSet.getObject(entry.getKey(), entry.getValue()));
                    } catch (SQLException e) {
                        HXLogger.build(HXJdbcFactory.class).error("获取数据库查询结果集异常！", e);
                    }
                });
                list.add(resMap);
            }
        } catch (SQLException e) {
            HXLogger.build(HXJdbcFactory.class).error("执行数据库操作异常！", e);
        } finally {
            close(conn,pstm);
        }
        return list;
    }

    private static void close(Connection conn,PreparedStatement pstm){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            HXLogger.build(HXJdbcFactory.class).error("关闭数据库连接异常!",e);
        }
        try {
            if (pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
            HXLogger.build(HXJdbcFactory.class).error("关闭数据库连接异常!",e);
        }
    }
}
