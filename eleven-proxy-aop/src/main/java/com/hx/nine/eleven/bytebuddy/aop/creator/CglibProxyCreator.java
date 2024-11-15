package com.hx.nine.eleven.bytebuddy.aop.creator;

import com.hx.nine.eleven.bytebuddy.aop.interceptor.CglibMethodInterceptor;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CGLIB 代理创建
 * @auth wml
 * @date 2024/11/15
 */
public class CglibProxyCreator implements ObjectProxyCreator {
	private final static Logger LOGGER = LoggerFactory.getLogger(CglibProxyCreator.class);

	@Override
	public <T> T createInterceptorProxy(Class<T> target, MethodInterceptor interceptor) {
		return createProxy(Thread.currentThread().getContextClassLoader(),target, interceptor);
	}

	@Override
	public <T> T createInterceptorProxy(ClassLoader classLoader, Class<T> target, MethodInterceptor interceptor) {
		return createProxy(classLoader, target, interceptor);
	}

	@Override
	public <T> T createAdviceInterceptorProxy(Class<T> target) {
		return createAdviceInterceptorProxy(Thread.currentThread().getContextClassLoader(),target);
	}

	@Override
	public <T> T createAdviceInterceptorProxy(ClassLoader classLoader, Class<T> target) {
		return createAdviceProxy(classLoader,target);
	}


	/**
	 * 创建cglib代理类
	 *
	 * @TODO issue：1 目前指定CglibMethodInterceptor的拦截器实现只能有一个，不符合实际场景使用，需要根据用户指定的源类和拦截实现来创建代理对象
	 *
	 * @author wml
	 * @data   2024-11-15
	 * @des    <p>
	 *     修复 issue：目前指定MethodInvocationInterceptor的拦截器实现只能有一个，不符合实际场景使用，
	 *     需要根据用户指定的源类和拦截实现来创建代理对象,代理拦截器实现统一 byteBuddy 使用统一个
	 *     {MethodInterceptor}
	 * </p>
	 *
	 *
	 * @param classLoader   类加载器
	 * @param target        目标类
	 * @param interceptor   拦截器实现
	 * @param <T>           目标类类型
	 * @return              返回创建的代理类
	 */
	private <T> T createProxy(ClassLoader classLoader,Class<T> target,MethodInterceptor interceptor) {
		// 1.cglib工具类
		Enhancer enhancer = new Enhancer();
		// 2.设置父类
		enhancer.setSuperclass(target);
		// 3.设置回调函数
		CglibMethodInterceptor methodInterceptor = new CglibMethodInterceptor(interceptor);
		enhancer.setCallback(methodInterceptor);
		return (T) enhancer.create();
	}

	/**
	 * 创建advice类型代理类
	 * @param target  目标代理类
	 * @param <T>     代理类型
	 * @return        返回生成的代理类
	 */
	private <T> T createAdviceProxy(ClassLoader classLoader,Class<T> target){
		throw new RuntimeException("站不支持该类型代理");
	}
}
