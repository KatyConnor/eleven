package com.hx.nine.eleven.jooq.jdbc.tx;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberThreadLocal;
import  com.hx.nine.eleven.core.core.NamedThreadLocal;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JooqTransactionManagerHolder {

	/**
	 * 使用用链表存储(准确的是栈)
	 * <pre>
	 * 为了支持嵌套切换，如ABC三个service都是不同的数据源
	 * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
	 * 传统的只设置当前线程的方式不能满足此业务需求，必须使用栈，后进先出。
	 * </pre>
	 */
	private static final ThreadLocal<ConcurrentMap<String,JooqTransactionManagerEntity>> TRANSACTION_MANAGER =
			new NamedThreadLocal<ConcurrentMap<String,JooqTransactionManagerEntity>>("transaction-manager") {
		@Override
		protected ConcurrentMap<String,JooqTransactionManagerEntity> initialValue() {
			return new ConcurrentHashMap<>();
		}
	};


	/**
	 * 协程内部传输上下文对象，协程之间不共享，协程安全
	 */
	private static final FiberThreadLocal<ConcurrentMap<String,JooqTransactionManagerEntity>> FIBER_TRANSACTION_MANAGER = new FiberThreadLocal<ConcurrentMap<String,JooqTransactionManagerEntity>>("domain-context") {
		@Override
		protected ConcurrentMap<String,JooqTransactionManagerEntity> initialValue() {
			return new ConcurrentHashMap<>();
		}
	};
   /**-------------------线程中执行-----------------------*/
	public static ConcurrentMap<String,JooqTransactionManagerEntity> currentTransactionManager() {
		return Fiber.currentFiber() == null?TRANSACTION_MANAGER.get():currentFiberTransactionManager();
	}

	/**
	 * 获得当前线程数据库连接
	 *
	 * @return 返回数据库连接 Connection
	 */
	public static JooqTransactionManagerEntity getTransactionManager(String key) {
		return Fiber.currentFiber() == null?TRANSACTION_MANAGER.get().get(key):getFiberTransactionManager(key);
	}

	/**
	 * 当前线程添加数据库连接
	 *
	 * @param entity 数据库连接请求
	 */
	public static boolean addTransactionManager(JooqTransactionManagerEntity entity) {
		if (Fiber.currentFiber() == null){
			if (Optional.ofNullable(entity).isPresent()){
				TRANSACTION_MANAGER.get().put(entity.getKey(),entity);
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
			ConcurrentMap<String,JooqTransactionManagerEntity> map = TRANSACTION_MANAGER.get();
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

	public static ConcurrentMap<String,JooqTransactionManagerEntity> currentFiberTransactionManager() {
		return FIBER_TRANSACTION_MANAGER.get();
	}

	/**
	 * 获得当前线程数据库连接
	 *
	 * @return 返回数据库连接 Connection
	 */
	public static JooqTransactionManagerEntity getFiberTransactionManager(String key) {
		return FIBER_TRANSACTION_MANAGER.get().get(key);
	}

	/**
	 * 当前线程添加数据库连接
	 *
	 * @param entity 数据库连接请求
	 */
	public static boolean addFiberTransactionManager(JooqTransactionManagerEntity entity) {
		if (Optional.ofNullable(entity).isPresent()){
			FIBER_TRANSACTION_MANAGER.get().put(entity.getKey(),entity);
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
		ConcurrentMap<String,JooqTransactionManagerEntity> map = TRANSACTION_MANAGER.get();
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
