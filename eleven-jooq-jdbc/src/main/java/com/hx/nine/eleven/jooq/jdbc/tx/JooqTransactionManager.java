package com.hx.nine.eleven.jooq.jdbc.tx;

import  com.hx.nine.eleven.commons.utils.ObjectUtils;
import com.hx.nine.eleven.jdbc.AbstractRoutingDataSource;
import com.hx.nine.eleven.jdbc.ElevenJdbcTransactionManager;
import com.hx.nine.eleven.jooq.jdbc.utils.TransactionUtils;
import org.jooq.TransactionContext;
import org.jooq.impl.HXJooqTransactionContext;
import org.jooq.impl.ThreadLocalTransactionProvider;
import org.jooq.tools.JooqLogger;

/**
 * 事务管理
 * @author wml
 * @date 2023-04-21
 */
public class JooqTransactionManager implements ElevenJdbcTransactionManager {
	private final static JooqLogger LOGGER = JooqLogger.getLogger(HXJooqTransactionContext.class);

	/** 自动提交事务 */
	private boolean autoCommit;
	/** 事务管理 */
	private ThreadLocalTransactionProvider transactionProvider;
	/** 数据源 */
	private AbstractRoutingDataSource dataSource;

	public JooqTransactionManager(AbstractRoutingDataSource dataSource) {
		this(false,dataSource);
	}

	public JooqTransactionManager(boolean autoCommit, AbstractRoutingDataSource dataSource) {
		LOGGER.info("初始化事务管理");
		this.autoCommit = autoCommit;
		this.dataSource = dataSource;
		this.transactionProvider = TransactionUtils.createTransactionProvider(dataSource);
	}

	/**
	 * 开启事务
	 */
	@Override
	public void begin() {
		TransactionContext ctx = HXJooqTransactionContext.newTransactionContext(this.dataSource.currentDataSourceName());
		if (!ObjectUtils.isEmpty(ctx)){
			this.transactionProvider.begin(ctx);
			JooqTransactionManagerEntity managerEntity = JooqTransactionManagerHolder.getTransactionManager(this.dataSource.currentDataSourceName());
			managerEntity.setTransactionContext(ctx);
			managerEntity.setTransaction(false);
			managerEntity.setRelease(false);
			JooqTransactionManagerHolder.addTransactionManager(managerEntity);
			LOGGER.info("当前[{}]线程开启事务",Thread.currentThread().getName());
		}
	}

	/**
	 * 提交事务
	 */
	@Override
	public void commit() {
		JooqTransactionManagerEntity transactionManagerEntity = JooqTransactionManagerHolder.getTransactionManager(this.dataSource.currentDataSourceName());
		if (ObjectUtils.isEmpty(transactionManagerEntity) || ObjectUtils.isEmpty(transactionManagerEntity.getTransactionContext())){
			LOGGER.warn("当前线程没有开启事务,不提交事务");
			return;
		}
		TransactionContext ctx = transactionManagerEntity.getTransactionContext();
		transactionManagerEntity.setRelease(true);
		if (ObjectUtils.isEmpty(ctx)){
			LOGGER.warn("当前线程没有开启事务,不提交事务");
			return;
		}
		this.transactionProvider.commit(ctx);
		JooqTransactionManagerHolder.clearTransactionManager();
		LOGGER.info("当前线程提交事务:",Thread.currentThread().getName());
	}

	/**
	 * 回滚事务
	 */
	@Override
	public void rollback() {
		JooqTransactionManagerEntity transactionManagerEntity = JooqTransactionManagerHolder.getTransactionManager(this.dataSource.currentDataSourceName());
		if (ObjectUtils.isEmpty(transactionManagerEntity) || ObjectUtils.isEmpty(transactionManagerEntity.getTransactionContext())){
			LOGGER.warn("当前线程没有开启事务,无需事务回滚");
			return;
		}
		TransactionContext ctx =transactionManagerEntity.getTransactionContext();
		transactionManagerEntity.setRelease(true);
		if (ObjectUtils.isEmpty(ctx)){
			LOGGER.warn("当前线程没有开启事务,无需事务回滚");
			return;
		}
		this.transactionProvider.rollback(ctx);
		JooqTransactionManagerHolder.clearTransactionManager();
		LOGGER.info("当前[{}]线程回滚事务",Thread.currentThread().getName());
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public ThreadLocalTransactionProvider getTransactionProvider() {
		return transactionProvider;
	}

	public void setTransactionProvider(ThreadLocalTransactionProvider transactionProvider) {
		this.transactionProvider = transactionProvider;
	}

	public AbstractRoutingDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(AbstractRoutingDataSource dataSource) {
		this.dataSource = dataSource;
	}
}
