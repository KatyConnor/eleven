package com.hx.nine.eleven.doop.tx;

import com.hx.nine.eleven.doop.constant.TransactionPropagationLevel;

import java.util.Map;
/**
 * 事务管理上下文对象，管理向TransactionProvider传递的参数
 * @auth wml
 * @date 2024/3/22
 */
public interface TransactionContext {
	/**
	 * 默认事务穿传播机制级别
	 */
	int transactionPropagationLevel = TransactionPropagationLevel.TRANSACTION_PROPAGATION_REQUIRED;

	Object data(Object key);

	Object data(Object key, Object value);

	Map<Object, Object> data();
}
