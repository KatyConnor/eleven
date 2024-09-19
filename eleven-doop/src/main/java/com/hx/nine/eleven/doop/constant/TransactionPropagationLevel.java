package com.hx.nine.eleven.doop.constant;

/**
 * 事务传播级别
 * @auth wml
 * @date 2024/3/27
 */
public interface TransactionPropagationLevel {

	/**
	 * 默认传播机制
	 * 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新事务。
	 * REQUIRED传播机制最常用的情况是在一个事务中进行多个操作，要么全部成功，要么全部失败。如果其中一个操作失败，整个事务都将被回滚。
	 */
	int TRANSACTION_PROPAGATION_REQUIRED = 1;

	/**
	 * 当前方法如果在一个事务中被调用或者上下文存在事务，则加入该事务；否则，以非事务的方式运行。
	 * SUPPORTS传播机制适用于对事务要求不高的操作，例如读取操作。
	 */
	int TRANSACTION_PROPAGATION_SUPPORTS = 2;

	/**
	 * 当前方法必须在一个事务中被调用，否则将抛出异常。MANDATORY传播机制适用于在需要事务的情况下调用方法。
	 */
	int TRANSACTION_PROPAGATION_MANDATORY = 3;

	/**
	 * 每次都会新建一个事务,即前方法必须开启一个新事务运行，如果当前存在事务，则挂起该事务。
	 * 当前新建事务完成以后，被挂起事务恢复再执行。
	 */
	int TRANSACTION_PROPAGATION_REQUIRES_NEW = 4;

	/**
	 * 当前方法不应该在事务中运行，如果存在事务，则挂起该事务。NOT_SUPPORTED传播机制适用于对事务没有要求的操作，例如日志记录等。
	 */
	int TRANSACTION_NOT_SUPPORTED = 5;

	/**
	 * 当前方法必须在一个嵌套事务中运行，如果当前存在事务，则在该事务内开启一个嵌套事务；如果当前没有事务，则创建一个新事务。
	 * NESTED传播机制适用于需要分步操作的场景，例如订单中创建订单和订单项的操作。
	 */
	int TRANSACTION_PROPAGATION_NESTED = 6;

	/**
	 * 当前方法不应该在事务中运行，如果存在事务，则抛出异常。NEVER传播机制适用于禁止在事务中运行的操作，例如安全检查等。
	 */
	int TRANSACTION_PROPAGATION_NEVER = 7;
}
