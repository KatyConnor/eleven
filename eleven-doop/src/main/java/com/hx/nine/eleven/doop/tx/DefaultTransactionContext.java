package com.hx.nine.eleven.doop.tx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事务管理上下文传递，针对每一个事务一个上下文，如果是嵌套事务只有最上层事务一个统一管理一个上下文
 * @auth wml
 * @date 2024/3/26
 */
public class DefaultTransactionContext implements TransactionContext{

	private transient ConcurrentHashMap<Object, Object> data;

	public DefaultTransactionContext(){
		this.data = new ConcurrentHashMap<>();
	}

	@Override
	public Object data(Object key) {
		return data.get(key);
	}

	@Override
	public Object data(Object key, Object value) {
		return data.put(key, value);
	}

	@Override
	public Map<Object, Object> data() {
		return this.data;
	}


}
