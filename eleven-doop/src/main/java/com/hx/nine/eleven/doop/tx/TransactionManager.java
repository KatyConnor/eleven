package com.hx.nine.eleven.doop.tx;

import com.hx.nine.eleven.doop.TransactionUtils;
import com.hx.nine.eleven.doop.jdbc.AbstractRoutingDataSource;
import com.hx.nine.eleven.doop.utils.DoopTransactionContextUtils;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.nine.eleven.core.utils.HXLogger;

/**
 * 事务管理
 * @author wml
 * @date 2023-04-21
 */
public class TransactionManager {

	/** 自动提交事务, 默认false，不自动提交事务 */
	private boolean autoCommit = false;
	/** 事务管理  */
	private ThreadLocalTransactionProvider transactionProvider;
	/** 数据源  */
	private AbstractRoutingDataSource dataSource;

	/**
	 * 默认关闭自动提交事务
	 * @param dataSource 数据源
	 */
	public TransactionManager(AbstractRoutingDataSource dataSource) {
		this(false,dataSource);
	}

	public TransactionManager(boolean autoCommit, AbstractRoutingDataSource dataSource) {
		HXLogger.build(this).info("初始化事务管理");
		this.autoCommit = autoCommit;
		this.dataSource = dataSource;
		this.transactionProvider = TransactionUtils.createTransactionProvider(dataSource);
	}

	/**
	 *  如果 ctx 为空则说明
	 * 开启事务，首先
	 */
	public void begin() {
		TransactionContext ctx = DoopTransactionContextUtils.newTransactionContext(this.dataSource.currentDataSourceName());
		if (!ObjectUtils.isEmpty(ctx)){
			this.transactionProvider.begin(ctx);
			TransactionManagerEntity managerEntity = TransactionManagerHolder.getTransactionManager(this.dataSource.currentDataSourceName());
			managerEntity.addTransactionContext(ctx);
			managerEntity.setTransaction(true);
			managerEntity.setRelease(false);
			TransactionManagerHolder.addTransactionManager(managerEntity);
			HXLogger.build(this).info("当前[{}]线程开启事务",Thread.currentThread().getName());
		}
	}

	/**
	 * 提交事务
	 */
	public void commit() {
		TransactionManagerEntity transactionManagerEntity = TransactionManagerHolder.getTransactionManager(this.dataSource.currentDataSourceName());
		if (ObjectUtils.isEmpty(transactionManagerEntity) || ObjectUtils.isEmpty(transactionManagerEntity.getTransactionContext())){
			HXLogger.build(this).warn("当前线程没有开启事务,不提交事务");
			return;
		}
		TransactionContext ctx = transactionManagerEntity.getTransactionContext();
		transactionManagerEntity.setRelease(true);
		if (ObjectUtils.isEmpty(ctx)){
			HXLogger.build(this).warn("当前线程没有开启事务,不提交事务");
			return;
		}
		this.transactionProvider.commit(ctx);
		TransactionManagerHolder.clearTransactionManager();
		HXLogger.build(this).info("当前线程提交事务:",Thread.currentThread().getName());
	}

	/**
	 * 回滚事务
	 */
	public void rollback() {
		TransactionManagerEntity transactionManagerEntity = TransactionManagerHolder.getTransactionManager(this.dataSource.currentDataSourceName());
		if (ObjectUtils.isEmpty(transactionManagerEntity) || ObjectUtils.isEmpty(transactionManagerEntity.getTransactionContext())){
			HXLogger.build(this).warn("当前线程没有开启事务,无需事务回滚");
			return;
		}
		TransactionContext ctx =transactionManagerEntity.getTransactionContext();
		transactionManagerEntity.setRelease(true);
		if (ObjectUtils.isEmpty(ctx)){
			HXLogger.build(this).warn("当前线程没有开启事务,无需事务回滚");
			return;
		}
		this.transactionProvider.rollback(ctx);
		TransactionManagerHolder.clearTransactionManager();
		HXLogger.build(this).info("当前[{}]线程回滚事务",Thread.currentThread().getName());
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
