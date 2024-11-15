package com.hx.nine.eleven.core.aop;
/**
 * 自定义实现代理处理
 * @author wml
 * @date 2023-04-02
 */
public abstract class ElevenObjectProxy {

	/**
	 * 校验是否代理
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public abstract <T> boolean checkProxy(Class<T> tClass);

	/**
	 * 创建代理对象，代理创建失败则返回 null
	 * @param target 目标类class
	 * @param <T>    目标类类型
	 * @return       返回创建的目标代理对象
	 */
	public abstract <T> T creatProxy(Class<T> target) throws Exception;
}
