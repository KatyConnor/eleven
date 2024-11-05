package com.hx.nine.eleven.bytebuddy.aop.testdemo;

import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.nine.eleven.bytebuddy.aop.invoke.Invocation;

public class ByteBuddyMethodInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result = null;
		try {
			System.out.println("方法执行前处理");
			result = invocation.proceed();
		}catch (Throwable ex){
			System.out.println("方法执行结束");
			throw ex;
		}
		return result;
	}
}
