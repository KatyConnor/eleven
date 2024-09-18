package com.cqrcb.cims.tools.mybatis.generator.types;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class CIMSJavaTypeResolver extends JavaTypeResolverDefaultImpl {
	public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
		int jdbcType = introspectedColumn.getJdbcType();
		FullyQualifiedJavaType answer;
//    FullyQualifiedJavaType answer;
		if (jdbcType == 1111) {
			answer = new FullyQualifiedJavaType(String.class.getName());
		} else {
//      FullyQualifiedJavaType answer;
			if (jdbcType == -101) {
				answer = new FullyQualifiedJavaType(Timestamp.class.getName());
			} else {
				answer = super.calculateJavaType(introspectedColumn);
			}
		}
		return answer;
	}

	private String getFieldOracleDataTypes(IntrospectedColumn introspectedColumn) {
		String fieldName = introspectedColumn.getActualColumnName();
		String tableName = "PMSBA_TEMP";

		JDBCConnectionConfiguration jdbc = introspectedColumn.getContext().getJdbcConnectionConfiguration();
		String sql = "SELECT COLUMN_NAME,DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = ?";
		Map<String, String> resultMap = executeQuery(jdbc.getDriverClass(), jdbc.getConnectionURL(), jdbc.getUserId(),
				jdbc.getPassword(), sql, new String[] { tableName });
		if (resultMap == null) {
			throw new RuntimeException("getFieldOracleDataTypes resultMap is null.");
		}
		if (resultMap.get(fieldName) == null) {
			String errFmt = "Table [%s] not contains column [%s],failed.";
			String errMsg = String.format(errFmt, new Object[] { tableName, fieldName });
			throw new RuntimeException(errMsg);
		}
		return (String) resultMap.get(fieldName);
	}

	private Map<String, String> executeQuery(String driverClass, String connectionURL, String userId, String password,
			String sql, String... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Map resultMap = null;
		try {
			Class.forName(driverClass);
			connection = DriverManager.getConnection(connectionURL, userId, password);
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setString(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if (resultMap == null) {
					resultMap = new HashMap();
				}
				resultMap.put(resultSet.getString("COLUMN_NAME"), resultSet.getString("DATA_TYPE"));
			}
			return resultMap;
		} catch (Exception e) {
			throw new RuntimeException("executeQuery failed." + e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					throw new RuntimeException("Close resultSet failed.", e);
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Close preparedStatement failed.", e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Close connection failed.", e);
				}
			}
		}
	}
}
