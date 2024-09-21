package org.jooq.impl;

import  com.hx.nine.eleven.commons.utils.Builder;
import  com.hx.nine.eleven.commons.utils.ObjectUtils;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerEntity;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManagerHolder;
import org.jooq.TransactionContext;
import org.jooq.tools.JooqLogger;

/**
 *
 */
public class HXJooqTransactionContext {

	private final static JooqLogger LOGGER = JooqLogger.getLogger(HXJooqTransactionContext.class);

	/**
	 * 如果没获取到Configuration，标注开启当前事务，当第一次获取dslContext时开启事务,初始化TransactionContext
	 *
	 * @return
	 */
	public static TransactionContext newTransactionContext(String dataSource) {
		JooqTransactionManagerEntity managerEntity = JooqTransactionManagerHolder.getTransactionManager(dataSource);
		if ((managerEntity = ObjectUtils.isEmpty(managerEntity)?Builder.of(JooqTransactionManagerEntity::new).build():
				managerEntity) != null && ObjectUtils.isEmpty(managerEntity.getConfiguration())) {
			managerEntity.setKey(dataSource);
			managerEntity.setTransaction(true);
			JooqTransactionManagerHolder.addTransactionManager(managerEntity);
			LOGGER.info("当前线程没有Configuration，设置事务transaction为true");
			return null;
		}
		DefaultTransactionContext ctx = new DefaultTransactionContext(managerEntity.getConfiguration().derive());
		return ctx;
	}
}
