package com.hx.vertx.jooq.jdbc;

import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManagerEntity;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManagerHolder;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.tools.JooqLogger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接信息
 * @author wml
 * @date 2023-05-15
 */
public class HXDataSourceConnectionProvider extends DataSourceConnectionProvider {

	private static final JooqLogger LOGGER = JooqLogger.getLogger(HXDataSourceConnectionProvider.class);

	public HXDataSourceConnectionProvider(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void release(Connection connection) {
		try {
			if (this.dataSource() instanceof AbstractRoutingDataSource){
				String dataSource = ((AbstractRoutingDataSource)this.dataSource()).currentDataSourceName();
				JooqTransactionManagerEntity transactionManagerEntity = JooqTransactionManagerHolder.
						getTransactionManager(dataSource);
				if (ObjectUtils.isEmpty(transactionManagerEntity)){
					connection.close();
					LOGGER.warn("没有开启事务管理器，默认关闭连接释放资源");
					return;
				}
				if (transactionManagerEntity.isRelease()){
					connection.close();
					LOGGER.info("提交事务，关闭数据库连接释放资源");
					return;
				}
				LOGGER.warn("关闭数据库连接失败");
			}else {
				throw new DataAccessException(StringUtils.format("不支持的数[{}]据源类型,数据源需要继承com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource",
						this.dataSource().getClass().getName()) + connection);
			}
		}
		catch (SQLException e) {
			throw new DataAccessException("Error closing connection " + connection, e);
		}
	}
}
