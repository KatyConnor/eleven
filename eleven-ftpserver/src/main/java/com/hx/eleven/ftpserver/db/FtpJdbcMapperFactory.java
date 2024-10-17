package com.hx.eleven.ftpserver.db;

import com.hx.eleven.ftpserver.context.DefaultFtpServerContext;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作
 * @auth wml
 * @date 2024/10/17
 */
public class FtpJdbcMapperFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpJdbcMapperFactory.class);

	// 默认一次插入10万条sql
	private final static int defaultInsertCount = 100000;

	private DefaultFtpServerContext serverContext;

	private FtpJdbcMapperFactory(){
		this.serverContext = ElevenApplicationContextAware.getBean(DefaultFtpServerContext.class);
	}

	public static FtpJdbcMapperFactory build(){
		return new FtpJdbcMapperFactory();
	}

	/**
	 * 执行查询sql
	 * @param sql            执行sql
	 * @param values         where 参数
	 * @param resultMap      返回结果集
	 * @return
	 */
	public List<Map<String, Object>> executeQuery(String sql, Object[] values, Map<String, Class<?>> resultMap) {
		HikariDataSource dataSource = this.serverContext.getHsqlDatasource().getDataSource();
		Connection conn = null;
		PreparedStatement pstm = null;
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			conn.setTransactionIsolation(1);//设置事务级别
			pstm = conn.prepareStatement(sql);
			if (values != null && values.length >0){
				for (int i  = 1; i <= values.length; i++) {
					pstm.setObject(i, values[i-1]);
				}
			}
			ResultSet resultSet = pstm.executeQuery();
			while (resultSet.next()){
				Map<String, Object> resMap = new HashMap<>();
				resultMap.entrySet().forEach((entry)->{
					try {
						resMap.put(entry.getKey(),resultSet.getObject(entry.getKey(), entry.getValue()));
					} catch (SQLException e) {
						LOGGER.error("获取数据库查询结果集异常！", e);
					}
				});
				list.add(resMap);
			}
		} catch (SQLException e) {
			LOGGER.error("执行数据库操作异常！", e);
		} finally {
			close(conn,pstm);
		}
		return list;
	}

	/**
	 *
	 * @param sql       执行SQL
	 * @param values    入参值
	 * @param resultMap key: 数据库实体对象字段；value：对应数据库字段
	 * @return
	 */
	public List<Map<String, Object>> executeQueryByColumn(String sql, Object[] values, Map<String, String> resultMap) {
		HikariDataSource dataSource = this.serverContext.getHsqlDatasource().getDataSource();
		Connection conn = null;
		PreparedStatement pstm = null;
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			conn.setTransactionIsolation(1);//设置事务级别
			pstm = conn.prepareStatement(sql);
			if (values != null && values.length >0){
				for (int i  = 1; i <= values.length; i++) {
					pstm.setObject(i, values[i-1]);
				}
			}
			ResultSet resultSet = pstm.executeQuery();
			while (resultSet.next()){
				Map<String, Object> resMap = new HashMap<>();
				resultMap.entrySet().forEach((entry)->{
					try {
						resMap.put(entry.getKey(),resultSet.getObject(entry.getValue()));
					} catch (SQLException e) {
						LOGGER.error("获取数据库查询结果集异常！", e);
					}
				});
				list.add(resMap);
			}
		} catch (SQLException e) {
			LOGGER.error("执行数据库操作异常！", e);
		} finally {
			close(conn,pstm);
		}
		return list;
	}

	public boolean execute(String sql, Object[] values){
		HikariDataSource dataSource = this.serverContext.getHsqlDatasource().getDataSource();
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = dataSource.getConnection();
			conn.setTransactionIsolation(1);//设置事务级别
			pstm = conn.prepareStatement(sql);
			if (values != null && values.length >0){
				for (int i  = 1; i <= values.length; i++) {
					pstm.setObject(i, values[i-1]);
				}
			}
			return pstm.execute();
		} catch (SQLException e) {
			LOGGER.error("执行数据库操作异常！", e);
		} finally {
			close(conn,pstm);
		}
		return false;
	}

	public int executeBatchInsert(String sql, List<Object[]> valueList) {
		if (valueList == null || valueList.size() <= 0){
			return 0;
		}
		HikariDataSource dataSource = this.serverContext.getHsqlDatasource().getDataSource();
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = dataSource.getConnection();
			pstm = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			int size = valueList.size();
			if (size <= defaultInsertCount) {
				for (int i = 0; i < size; i++) {
					Object[] values = valueList.get(i);
					int length = values.length;
					for (int j = 1; j <= length; j++) {
						pstm.setObject(j, values[i]);
					}
				}
				// 执行批处理
				pstm.addBatch();
				pstm.executeBatch();
				conn.commit();
				return size;
			}

			int batchCount = defaultInsertCount;
			for (int i = 0; i < size; i++) {
				Object[] values = valueList.get(i);
				int length = values.length;
				for (int j = 1; j <= length; j++) {
					pstm.setObject(j, values[i]);
				}
				if (i == batchCount) {
					pstm.addBatch();
					pstm.executeBatch();
					batchCount = batchCount + defaultInsertCount;
					conn.commit();
				}
			}
			return batchCount;
		} catch (SQLException e) {
			LOGGER.error("执行数据库操作异常！", e);
		} finally {
			close(conn, pstm);
		}
		return 0;
	}

	public int executeUpdate(String sql, Object[] values){
		HikariDataSource dataSource = this.serverContext.getHsqlDatasource().getDataSource();
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = dataSource.getConnection();
			conn.setTransactionIsolation(1);//设置事务级别
			pstm = conn.prepareStatement(sql);
			if (values != null && values.length >0){
				for (int i  = 1; i <= values.length; i++) {
					pstm.setObject(i, values[i-1]);
				}
			}
			return pstm.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("执行数据库操作异常！", e);
		} finally {
			close(conn,pstm);
		}
		return 0;
	}

	private static void close(Connection conn,PreparedStatement pstm){
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			LOGGER.error("关闭数据库连接异常!",e);
		}
		try {
			if (pstm != null) {
				pstm.close();
			}
		} catch (SQLException e) {
			LOGGER.error("关闭数据库连接异常!",e);
		}
	}
}
