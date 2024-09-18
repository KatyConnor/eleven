package com.hx.bytebuddy.aop.interceptor;

import com.hx.bytebuddy.aop.invoke.MethodInvocation;
import net.bytebuddy.implementation.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ProxyMethodDelegationInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyMethodDelegationInterceptor.class);

	private final MethodInterceptor methodInterceptor;
	private final Object target;

	public ProxyMethodDelegationInterceptor(MethodInterceptor methodInterceptor,Object target) {
		this.methodInterceptor = methodInterceptor;
		this.target = target;
	}

	/**
	 *  拦截器
	 * @param proxy      被拦截的目标对象 （动态生成的代理对象）
	 * @param method     正在执行的方法Method对象（目标对象父类的Method）
	 * @param arguments  正在执行的目标方法的全部参数
	 * @return
	 * @throws Throwable 抛出异常
	 */
	@RuntimeType
	public Object invoke(@This Object proxy, @Origin Method method,
						 @AllArguments Object[] arguments) throws Throwable {
		LOGGER.info("进入[{}]:[{}]方法",this.target.getClass().getName(),method.getName());
		// 开启事务
		MethodInvocation invocation = new MethodInvocation(method,arguments,proxy,this.target);
		Object result = methodInterceptor.intercept(invocation);
		// 提交事务
		return result;
	}
}
