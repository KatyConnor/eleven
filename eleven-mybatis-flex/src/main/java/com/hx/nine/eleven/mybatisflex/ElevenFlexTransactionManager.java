package com.hx.nine.eleven.mybatisflex;

import com.github.f4b6a3.ulid.UlidCreator;
import com.hx.nine.eleven.jdbc.ElevenJdbcTransactionManager;
import com.mybatisflex.core.transaction.TransactionContext;
import com.mybatisflex.core.transaction.TransactionalManager;
import com.mybatisflex.core.util.StringUtil;

/**
 * 事务管理
 * @auth wml
 * @date 2024/11/12
 */
public class ElevenFlexTransactionManager implements ElevenJdbcTransactionManager {

	@Override
	public void begin() {
		// 获取事务ID
		String currentXID = TransactionContext.getXID();
		// 已经开启事务,则不做处理，未开启事务,开启事务生成一个currentXID放入 TransactionContext 中，标志当前线程处理逻辑已经开启事务
		if (!StringUtil.hasText(currentXID)){
			currentXID = UlidCreator.getUlid().toString();
			TransactionContext.holdXID(currentXID);
		}
	}

	@Override
	public void commit() {
		// 获取事务ID
		String currentXID = TransactionContext.getXID();
		// 已经开启事务,则不做处理，未开启事务,开启事务生成一个currentXID放入 TransactionContext 中，标志当前线程处理逻辑已经开启事务
		if (!StringUtil.hasText(currentXID)){
			throw new RuntimeException("没开启事务, 提交失败");
		}
		TransactionalManager.commit(currentXID);
	}

	@Override
	public void rollback() {
// 获取事务ID
		String currentXID = TransactionContext.getXID();
		// 已经开启事务,则不做处理，未开启事务,开启事务生成一个currentXID放入 TransactionContext 中，标志当前线程处理逻辑已经开启事务
		if (!StringUtil.hasText(currentXID)){
			throw new RuntimeException("没开启事务, 事务回滚失败");
		}
		TransactionalManager.rollback(currentXID);
	}
}
