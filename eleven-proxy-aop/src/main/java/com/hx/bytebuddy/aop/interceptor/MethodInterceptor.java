package com.hx.nine.eleven.bytebuddy.aop.interceptor;

import com.hx.nine.eleven.bytebuddy.aop.invoke.Invocation;
import java.io.Serializable;

/**
 * 拦截器接口
 *
 * @author wml
 * @date 2023-04-06
 */
public interface MethodInterceptor extends Serializable {

    Object intercept(Invocation invocation) throws Throwable;

}
