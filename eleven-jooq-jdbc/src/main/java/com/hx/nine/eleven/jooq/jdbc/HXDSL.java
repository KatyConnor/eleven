package com.hx.nine.eleven.jooq.jdbc;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.fibers.jdbc.FiberDataSource;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import com.hx.nine.eleven.jdbc.AbstractRoutingDataSource;
import com.hx.nine.eleven.sync.fiber.FiberThreadPoolScheduler;
import  com.hx.nine.eleven.commons.utils.Builder;
import  com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.jooq.jdbc.enums.SQLDialectEnums;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerHolder;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManager;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerEntity;
import com.hx.nine.eleven.jooq.jdbc.utils.JdbcUrlUtils;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDSLContext;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wml
 * @date 2023-04-21
 */
public class HXDSL extends DSL {

	/**
	 * 获取DSLContext
	 * @param  datasource 数据源
	 * @return
	 */
	public static DSLContext using(DataSource datasource) {
		try {
			return getDSLContext(datasource);
		} catch (SQLException e) {
			throw new RuntimeException("获取 DSLContext 失败！");
		}
	}

	public static DSLContext using(Configuration configuration){
		return DSL.using(configuration);
	}

	/**
	 * 获取DSLContext
	 * 首先从缓存中获取JooqTransactionManagerEntity实例，然后从实例中获取Configuration
	 * 如果JooqTransactionManagerEntity为null或者Configuration为null。则创建新实例放入缓存
	 * @return DSLContext
	 */
	@Suspendable
	public static DSLContext getDSLContext(DataSource datasource) throws SQLException {
		if (!(datasource instanceof AbstractRoutingDataSource)){
			ElevenLoggerFactory.build(HXDSL.class).error(StringUtils.format("不支持的数[{}]据源类型,数据源需要继承com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource",
					datasource.getClass().getName()));
			throw new DataAccessException(StringUtils.format("不支持的数[{}]据源类型,数据源需要继承com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource",
					datasource.getClass().getName()));
		}
		String dataSourceName = ((AbstractRoutingDataSource)datasource).currentDataSourceName();
		JooqTransactionManagerEntity managerEntity = JooqTransactionManagerHolder.getTransactionManager(dataSourceName);
		managerEntity = managerEntity == null? Builder.of(JooqTransactionManagerEntity::new).build():managerEntity;
		if (!Optional.ofNullable(managerEntity.getConfiguration()).isPresent()){
			// 判断当前任务是否在协程中进行，如果是则直接运行，如果不是，则开启协程
			Connection connection = null;
			if(Fiber.currentFiber() == null){
				ThreadPoolExecutor executor = FiberThreadPoolScheduler.build().getThreadPoolExecutor();
				connection = FiberDataSource.wrap(datasource,executor).getConnection();
				ElevenLoggerFactory.build(HXDSL.class).info("--------开启fiber模式执行connection---------");
			}else {
				connection = datasource.getConnection();
			}
			String jdbcUrl = JdbcUrlUtils.jdbcUrl(datasource);//JDBCUtils.dialect(connection)
			DefaultDSLContext dslContext = new DefaultDSLContext(connection, SQLDialectEnums.containsByCode(jdbcUrl).getDialect(), (Settings) null);
			managerEntity.setConfiguration(dslContext.configuration());
			managerEntity.setKey(dataSourceName);
			managerEntity.setConnection(connection);
			JooqTransactionManagerHolder.addTransactionManager(managerEntity);
			// 开启事务
			if (JooqTransactionManagerHolder.getTransaction(dataSourceName)){
				JooqTransactionManager jooqTransactionManager = ElevenApplicationContextAware.getBean(JooqTransactionManager.class);
				jooqTransactionManager.begin();
			}
			return dslContext;
		}
		return DSL.using(managerEntity.getConnection());
	}
}
