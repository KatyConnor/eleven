package com.hx.vertx.boot.core.bean;

import com.hx.vertx.boot.core.context.DefaultVertxApplicationContext;

/**
 * bean 初始化之前、初始化之后处理，这里可以指定
 * @param <T>
 */
public abstract class ApplicationBeanRegister<T> {

	/**
	 * 初始化的目标对象
	 */
	private  T bean;
	/**
	 * 目标对象Class类型
	 */
	private Class<T> tClass;
	/**
	 * 是否创建bean
	 */
	private boolean createBean;

	/**
	 * bean 初始化之前处理
	 * @param context
	 */
	abstract void before(DefaultVertxApplicationContext context);

	/**
	 * bean初始化之后处理
	 * @param context
	 */
	abstract void after(DefaultVertxApplicationContext context);

	public boolean isCreateBean() {
		return createBean;
	}

	public void setCreateBean(boolean createBean) {
		this.createBean = createBean;
	}

	public Class<T> gettClass() {
		return tClass;
	}

	public void settClass(Class<T> tClass) {
		this.tClass = tClass;
	}

	public T getBean() {
		return bean;
	}

	public void setBean(T bean) {
		this.bean = bean;
	}
}
