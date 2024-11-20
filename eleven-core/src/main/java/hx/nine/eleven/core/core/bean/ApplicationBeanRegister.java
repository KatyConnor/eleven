package hx.nine.eleven.core.core.bean;

import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;

/**
 * bean 初始化之前、初始化之后处理，这里可以指定
 * @TODO 切入处理可能需要考虑多框架集成，目前只支持单个使用，多个使用将抛出异常
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
	abstract void before(DefaultElevenApplicationContext context);

	/**
	 * bean初始化之后处理
	 * @param context
	 */
	abstract void after(DefaultElevenApplicationContext context);

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
