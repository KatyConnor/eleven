package hx.nine.eleven.bytebuddy.aop.interceptor;

import hx.nine.eleven.bytebuddy.aop.invoke.MethodInvocation;
import net.bytebuddy.implementation.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * bytebuddy 代理对象创建是，绑定的跟拦截，实现对用户实现的拦截调用逻辑处理
 * @author wml
 * @date 2023-04-06
 */
public class ByteBuddyMethodDelegationInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ByteBuddyMethodDelegationInterceptor.class);

	private final MethodInterceptor methodInterceptor;
	private final Object target;

	public ByteBuddyMethodDelegationInterceptor(MethodInterceptor methodInterceptor, Object target) {
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
	public Object invoke(@This Object proxy, @Origin Method method, @AllArguments Object[] arguments) throws Throwable {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("---------进入 [{}:{}]方法开始执行逻辑---------",this.target.getClass().getName(),method.getName());
		}
		// @TODO 开启事务
		MethodInvocation invocation = new MethodInvocation(method,arguments,proxy,this.target);
		Object result = methodInterceptor.intercept(invocation);
		// @TODO 提交事务
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("---------方法 [{}:{}] 执行完毕---------",this.target.getClass().getName(),method.getName());
		}
		return result;
	}
}
