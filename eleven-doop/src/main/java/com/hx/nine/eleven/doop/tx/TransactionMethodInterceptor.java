package com.hx.nine.eleven.doop.tx;

import com.hx.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.bytebuddy.aop.invoke.Invocation;

/**
 * 可以后续扩展注解事务
 */
public class TransactionMethodInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		return null;
	}
}
