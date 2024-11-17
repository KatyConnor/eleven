package com.hx.nine.eleven.bytebuddy.aop.invoke;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 拦截器调用接口，类似<code>com.hx.nine.eleven.bytebuddy.aop.interceptor</code>中的<code>Invocation</code>接口
 *
 * @author wml
 * @date 2023-04-06
 */
public interface Invocation {

	/**
	 * 获取方法参数
	 *
	 * @return 方法参数数组
	 */
	Object[] getArguments();

	/**
	 * 返回method方法对象
	 *
	 * @return method方法对象
	 */
	Method getMethod();

	/**
	 * 返回代理对象
	 *
	 * @return 代理对象
	 */
	Object getProxy();

	/**
	 * 返回被代理的目标对象
	 *
	 * @return 目标对象
	 */
	Object getTarget();

	/**
	 * Cglib 代理
	 * @return
	 */
	MethodProxy getMethodProxy();

	/**
	 * Cglib 代理
	 * @return
	 */
	boolean isCglibProxy();

	/**
	 * 触发拦截器调用链
	 *
	 * @return 调用结果
	 *
	 * @throws Throwable
	 */
	Object proceed() throws Throwable;
}
