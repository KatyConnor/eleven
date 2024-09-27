//package com.hx.db.datasources.interceptor;
//
//import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
//import com.hx.db.datasources.exception.CheckUpdateSqlException;
//import com.hx.nine.eleven.commons.utils.StringUtils;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//
//import java.sql.SQLException;
//
///**
// * 数据库更新操作全局拦截器，必须更新 version、updateTime 字段
// * 参数必须传 version、updateTime 属性
// *
// * @author wml
// * @date 2022-11-29
// */
//public class CheckUpdateSqlDateInterceptor implements InnerInterceptor {
//
//    public void throwArgExcepiton(String message, String... str) {
//        throw new CheckUpdateSqlException(StringUtils.format(message, str));
//    }
//
//    @Override
//    public boolean willDoUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
//        SqlCommandType sqlCommandType = ms.getSqlCommandType();
//        // update操作，更新version、update_time字段
//        if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
//            BoundSql boundSql = ms.getBoundSql(parameter);
//            String sql = boundSql.getSql();
//            if (sql == null) {
//                throwArgExcepiton("[{}]sql为null，请检查",ms.getId());
//            }
//            // 必须有where条件
//            sql = sql.toUpperCase();
//            int whereIndex = sql.lastIndexOf("WHERE");
//            if (whereIndex < 0) {
//                throwArgExcepiton("[{}]更新操作必须有WHERE关键字", ms.getId());
//            }
//
//            // 必须有 VERSION UPDATE_TIME 字段
//            int versionIndex = sql.indexOf("VERSION");
//            if (versionIndex < 0) {
//                throwArgExcepiton("[{}]更新操作必须更新 VERSION 字段", ms.getId());
////                sql = StringUtils.changeCharInString(sql,sql.indexOf("SET"),2,"SET VERSION = VERSION+1 ,");
//            }
//
//            int updateTimeIndex = sql.indexOf("UPDATE_TIME");
//            if (updateTimeIndex < 0) {
//                throwArgExcepiton("[{}]更新操作必须更新 UPDATE_TIME 字段", ms.getId());
////                sql = StringUtils.changeCharInString(sql,sql.indexOf("SET"),2,"SET UPDATE_TIME = sysdate ,");
//            }
//
//            int lastVersionIndex = sql.lastIndexOf("VERSION");
//            if (whereIndex > lastVersionIndex) {
//                throwArgExcepiton("[{}]更新操作WHERE 条件中必须有 VERSION = ? ", ms.getId());
//            }
//        }
//        return true;
//    }
//}
