package com.hx.doop.utils;

import com.hx.doop.tx.DefaultTransactionContext;
import com.hx.doop.tx.TransactionContext;
import com.hx.doop.tx.TransactionManagerEntity;
import com.hx.doop.tx.TransactionManagerHolder;
import com.hx.lang.commons.utils.Builder;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.logchain.toolkit.util.HXLogger;

/**
 * { @like TransactionContext }
 * @auth wml
 * @date 2024/3/27
 */
public class DoopTransactionContextUtils {

	/**
	 * 创建一个 TransactionContext, 首先要判断当前线程的datasource 是否已经开启事务，如果开启则直接使用，如果没开启则开启一个新的事务
	 * @param dataSource 数据源
	 * @return
	 */
	public static TransactionContext newTransactionContext(String dataSource) {
		TransactionManagerEntity managerEntity = TransactionManagerHolder.getTransactionManager(dataSource);
		if (ObjectUtils.isEmpty(managerEntity)){
			managerEntity = Builder.of(TransactionManagerEntity::new)
					.with(TransactionManagerEntity::setDataSource,dataSource).build();
		}

		// 没有开启事务
		if (!managerEntity.getTransaction()){
			managerEntity.setDataSource(dataSource);
			TransactionManagerHolder.addTransactionManager(managerEntity);
			HXLogger.build(DoopTransactionContextUtils.class).info("当前线程开启事务，设置事务transaction为true");
			managerEntity.setTransaction(true);
			return null;
		} else {
			DefaultTransactionContext ctx = new DefaultTransactionContext();
			managerEntity.addTransactionContext(ctx);
			managerEntity.setTransaction(true);
			return ctx;
		}
	}
}
