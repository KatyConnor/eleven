package com.hx.doop.jdbc.provider;

import com.hx.doop.tx.TransactionManagerEntity;
import com.hx.doop.tx.TransactionManagerHolder;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.logchain.toolkit.util.HXLogger;
import com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource;
import org.jooq.exception.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 针对 ConnectionProvider 的数据源默认实现，该类包装了一个DataSource数据源，doop根据
 * 当前数据源进行数据库初始化连接和操作
 *
 * @auth wml
 * @date 2024/3/26
 */
public class DataSourceConnectionProvider implements ConnectionProvider {

	private final DataSource dataSource;

	public DataSourceConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource dataSource() {
		return this.dataSource;
	}

	@Override
	public Connection acquire() throws DataAccessException {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new DataAccessException("Error getting connection from data source " + dataSource, e);
		}
	}

	@Override
	public void release(Connection connection) throws DataAccessException {
		try {
			if (connection == null) {
				HXLogger.build(this).warn("Error closing connection , connection is null, " + connection);
				return;
			}
			connection.close();
		} catch (SQLException e) {
			throw new DataAccessException("Error closing connection " + connection, e);
		}
	}

	@Override
	public void release() throws DataAccessException {
		try {
			if (this.dataSource() instanceof AbstractRoutingDataSource) {
				String dataSource = ((AbstractRoutingDataSource) this.dataSource()).currentDataSourceName();
				TransactionManagerEntity transactionManagerEntity = TransactionManagerHolder.getTransactionManager(dataSource);
				if (ObjectUtils.isEmpty(transactionManagerEntity)) {
					HXLogger.build(this).warn("没有开启事务管理器，无默认关闭连接释放资源");
					return;
				}

				if (transactionManagerEntity.isRelease()) {
					Connection connection = transactionManagerEntity.getConnection();
					if (connection != null) {
						connection.close();
					}
					HXLogger.build(this).info("关闭数据库连接,释放资源");
				} else {
					HXLogger.build(this).warn("关闭数据库连接失败");
				}
			} else {
				throw new DataAccessException(StringUtils.format("不支持的数[{}] 据源类型, 数据源需要继承" +
						"com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource 实现",
						new String[]{this.dataSource().getClass().getName()}));
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error closing connection：", e);
		}
	}
}
