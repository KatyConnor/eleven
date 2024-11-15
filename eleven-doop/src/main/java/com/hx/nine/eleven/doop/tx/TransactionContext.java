package com.hx.nine.eleven.doop.tx;

import com.hx.nine.eleven.doop.constant.TransactionPropagationLevel;
import org.apache.ibatis.cursor.Cursor;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事务管理上下文对象，管理向TransactionProvider传递的参数
 * @auth wml
 * @date 2024/3/22
 */
public class TransactionContext {

	/**
	 * 默认事务穿传播机制级别
	 */
	int transactionPropagationLevel = TransactionPropagationLevel.TRANSACTION_PROPAGATION_REQUIRED;

	private static final ThreadLocal<String> XID_HOLDER = new ThreadLocal<>();
	private static final ThreadLocal<Cursor<?>> CURSOR_HOLDER = new ThreadLocal<>();
	private static final ThreadLocal<ConcurrentHashMap<Object, Object>> data =  new ThreadLocal<>();

	public static Object data(Object key) {
		return data.get(key);
	}

	public static Object data(Object key, Object value) {
		return data.put(key, value);
	}

	public static Map<Object, Object> data() {
		return this.data;
	}

	public static String getXID() {
		return XID_HOLDER.get();
	}

	public static void release() {
		XID_HOLDER.remove();
		closeCursor();
	}

	private static void closeCursor() {
		Cursor<?> cursor = CURSOR_HOLDER.get();
		if (cursor != null) {
			try {
				cursor.close();
			} catch (IOException e) {
				//ignore
			} finally {
				CURSOR_HOLDER.remove();
			}
		}
	}

	public static void holdXID(String xid) {
		XID_HOLDER.set(xid);
	}

	public static void holdCursor(Cursor<?> cursor) {
		CURSOR_HOLDER.set(cursor);
	}
}
