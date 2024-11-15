package com.hx.nine.eleven.bytebuddy.aop.interceptor;

import com.hx.nine.eleven.bytebuddy.aop.invoke.Invocation;
import java.io.Serializable;

/**
 * bytebuddy、jdk、cglib 代理统一拦截器实现； 拦截器接口,交给用户实现拦截内容
 *
 * @author wml
 * @date 2023-04-06
 */
public interface MethodInterceptor extends Serializable {

    Object intercept(Invocation invocation) throws Throwable;

}
