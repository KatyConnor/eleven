package com.hx.bytebuddy.aop.invoke;

import com.hx.bytebuddy.aop.util.ObjectUtil;
import com.hx.bytebuddy.aop.util.ProxyUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 拦截器调用接口实现
 *
 * @author wml
 * @date 2023-04-06
 */
public class MethodInvocation implements Invocation{

    private final Method method;
    private final Object[] arguments;
    private final Object proxy;
    private final Object target;

    public MethodInvocation(Method method, Object[] arguments, Object proxy,Object target) {
        this.method = method;
        this.arguments = ObjectUtil.defaultIfNull(ArrayUtils.clone(arguments), ProxyUtil.EMPTY_ARGUMENTS);
        this.proxy = proxy;
        this.target = target;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object getProxy() {
        return proxy;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(target, arguments);
    }
}
