package com.hx.vertx.boot.aop.bytebuddy;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public interface AdvisorInterceptor {

	/**
	 *
	 * @RuntimeType：不进行严格的参数类型检测，在参数匹配失败时，尝试使用类型转换方式（runtime type casting）进行类型转换，匹配相应方法。
	 * @This：注入被拦截的目标对象（动态生成的目标对象）。
	 * @Origin：注入正在执行的方法Method 对象（目标对象父类的Method）。如果拦截的是字段的话，该注解应该标注到 Field 类型参数。
	 * @AllArguments：注入正在执行的方法的全部参数。
	 * @Super：注入目标对象的一个代理
	 * @SuperCall：这个注解比较特殊，我们要在 intercept() 方法中调用 被代理/增强 的方法的话，需要通过这种方式注入，与 Spring AOP 中的
	 * ProceedingJoinPoint.proceed() 方法有点类似，需要注意的是，这里不能修改调用参数，从上面的示例的调用也能看出来，参数不用单独传递，
	 * 都包含在其中了。另外，@SuperCall 注解还可以修饰 Runnable 类型的参数，只不过目标方法的返回值就拿不到了。
	 *
	 *
	 * @param target 被拦截的目标对象 （动态生成的目标对象）
	 * @param method 正在执行的方法Method 对象（目标对象父类的Method）
	 * @param argumengts 正在执行的方法的全部参数
	 * @param delegate  目标对象的一个代理
	 * @param callable  方法的调用者对象 对原始方法的调用依靠它
	 * @return
	 * @throws Exception
	 */
	@RuntimeType //将返回值转换成具体的方法返回值类型,加了这个注解 intercept 方法才会被执行
	Object intercept(@This Object target,@Origin Method method,
					 @AllArguments Object[] argumengts,
					 @Super Object delegate,
					 @SuperCall Callable<?> callable) throws Exception;

	/**
	 * 执行方法
	 * @param method
	 * @param arguments
	 */
	@Advice.OnMethodEnter
	void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments);

	/**
	 * 方法执行之后
	 * @param method
	 * @param arguments
	 * @param ret
	 */
	@Advice.OnMethodExit
	void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret);
}
