package com.hx.doop.tx;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberThreadLocal;
import com.hx.vertx.boot.core.NamedThreadLocal;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManagerEntity;
import org.jooq.Configuration;
import org.jooq.TransactionContext;

import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 事务
 * @auth wml
 * @date 2024/3/27
 */
public class TransactionManagerHolder {

	/**
	 * 使用用链表存储(准确的是栈)
	 * <pre>
	 * 为了支持嵌套切换，如ABC三个service都是不同的数据源
	 * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
	 * 传统的只设置当前线程的方式不能满足此业务需求，必须使用栈，后进先出。
	 * </pre>
	 */
	private static final ThreadLocal<ConcurrentMap<String, TransactionManagerEntity>> TRANSACTION_MANAGER =
			new NamedThreadLocal<ConcurrentMap<String,TransactionManagerEntity>>("transaction-manager") {
		@Override
		protected ConcurrentMap<String, TransactionManagerEntity> initialValue() {
			return new ConcurrentHashMap<>();
		}
	};
	/**-------------------线程中执行-----------------------*/
	/**
	 * 协程内部传输上下文对象，协程之间不共享，协程安全
	 */
	private static final FiberThreadLocal<ConcurrentMap<String, TransactionManagerEntity>> FIBER_TRANSACTION_MANAGER =
			new FiberThreadLocal<ConcurrentMap<String, TransactionManagerEntity>>("fiber-transaction-manager") {
		@Override
		protected ConcurrentMap<String, TransactionManagerEntity> initialValue() {
			return new ConcurrentHashMap<>();
		}
	};

	/**
	 * 当前线程获取
	 * @return
	 */
	public static ConcurrentMap<String, TransactionManagerEntity> currentTransactionManager() {
		return Fiber.currentFiber() == null?TRANSACTION_MANAGER.get():currentFiberTransactionManager();
	}

	/**
	 * 获得当前线程数据库连接
	 *
	 * @return 返回数据库连接 Connection
	 */
	public static TransactionManagerEntity getTransactionManager(String key) {
		return Fiber.currentFiber() == null?TRANSACTION_MANAGER.get().get(key):getFiberTransactionManager(key);
	}

	/**
	 * 当前线程添加数据库连接
	 *
	 * @param entity 数据库连接请求
	 */
	public static boolean addTransactionManager(TransactionManagerEntity entity) {
		if (Fiber.currentFiber() == null){
			if (Optional.ofNullable(entity).isPresent()){
				TRANSACTION_MANAGER.get().put(entity.getDataSource(),entity);
				return true;
			}
			return false;
		}else {
			return addFiberTransactionManager(entity);
		}
	}

	/**
	 * 清空当前线程数据源
	 * <p>
	 * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
	 * </p>
	 */
	public static void clearTransactionManager() {
		if (Fiber.currentFiber() == null){
			ConcurrentMap<String, TransactionManagerEntity> map = TRANSACTION_MANAGER.get();
			map.clear();
			TRANSACTION_MANAGER.remove();
		}else {
			clearFiberTransactionManager();
		}

	}

	/**
	 * 判断当前线程数据源是否开启事务
	 * @return
	 */
	public static boolean getTransaction(String key){
		// 判断是否在当前协程中执行
		return Fiber.currentFiber() == null?TRANSACTION_MANAGER.get().get(key).getTransaction():getFiberTransaction(key);
	}

	/**-------------------协程中执行-----------------------*/

	public static ConcurrentMap<String, TransactionManagerEntity> currentFiberTransactionManager() {
		return FIBER_TRANSACTION_MANAGER.get();
	}

	/**
	 * 获得当前线程数据库连接
	 *
	 * @return 返回数据库连接 Connection
	 */
	public static TransactionManagerEntity getFiberTransactionManager(String key) {
		return FIBER_TRANSACTION_MANAGER.get().get(key);
	}

	/**
	 * 当前线程添加数据库连接
	 *
	 * @param entity 数据库连接请求
	 */
	public static boolean addFiberTransactionManager(TransactionManagerEntity entity) {
		if (Optional.ofNullable(entity).isPresent()){
			FIBER_TRANSACTION_MANAGER.get().put(entity.getDataSource(),entity);
			return true;
		}
		return false;
	}

	/**
	 * 清空当前线程数据源
	 * <p>
	 * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
	 * </p>
	 */
	public static void clearFiberTransactionManager() {
		ConcurrentMap<String,TransactionManagerEntity> map = TRANSACTION_MANAGER.get();
		map.clear();
		FIBER_TRANSACTION_MANAGER.remove();
	}

	/**
	 * 判断当前线程数据源是否开启事务
	 * @return
	 */
	public static boolean getFiberTransaction(String key){
		return FIBER_TRANSACTION_MANAGER.get().get(key).getTransaction();
	}


}
