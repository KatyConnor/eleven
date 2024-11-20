package hx.nine.eleven.jdbc.enums;

/**
 * 事务传播级别
 * @auth wml
 * @date 2024/11/12
 */
public enum TransactionPropagationEnums {

	/**
	 * 若存在当前事务，则加入当前事务，若不存在当前事务，则创建新的事务
	 */
	REQUIRED(0),
	/**
	 * 若存在当前事务，则加入当前事务，若不存在当前事务，则以非事务的方式运行
	 */
	SUPPORTS(1),
	/**
	 * 若存在当前事务，则加入当前事务，若不存在当前事务，则抛出异常
	 */
	MANDATORY(2),
	/**
	 * 始终以新事务的方式运行，若存在当前事务，则暂停（挂起）当前事务。
	 */
	REQUIRES_NEW(3),
	/**
	 * 以非事务的方式运行，若存在当前事务，则暂停（挂起）当前事务。
	 */
	NOT_SUPPORTED(4),
	/**
	 * 以非事务的方式运行，若存在当前事务，则抛出异常。
	 */
	NEVER(5),
	/**
	 * 暂时不支持
	 */
	NESTED(6);

	private int value;

	TransactionPropagationEnums(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
