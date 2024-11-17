package com.hx.nine.eleven.bytebuddy.aop.invoke;

import com.hx.nine.eleven.bytebuddy.aop.util.ProxyUtil;
import com.hx.nine.eleven.commons.utils.ObjectUtils;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 * 拦截器调用接口实现
 *
 * @author wml
 * @date 2023-04-06
 */
public class MethodInvocation implements Invocation{

    /**
     * 拦截执行方法
     */
    private Method method;

    /**
     * Cglib
     */
    private MethodProxy methodProxy;

    private boolean isCglibProxy;

    /**
     * 拦截方法参数
     */
    private final Object[] arguments;
    /**
     * 生成的代理对象
     */
    private final Object proxy;
    /**
     * 被代理的源对象
     */
    private final Object target;

    public MethodInvocation(Method method, Object[] arguments, Object proxy,Object target) {
        this.method = method;
        this.arguments = ObjectUtils.defaultIfNull(arguments, ProxyUtil.EMPTY_ARGUMENTS);
        this.proxy = proxy;
        this.target = target;
        this.isCglibProxy = false;
    }

    public MethodInvocation(MethodProxy methodProxy, Object[] arguments, Object proxy,Object target) {
        this.methodProxy = methodProxy;
        this.arguments = ObjectUtils.defaultIfNull(arguments, ProxyUtil.EMPTY_ARGUMENTS);
        this.proxy = proxy;
        this.target = target;
        this.isCglibProxy = true;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setMethodProxy(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
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
    public MethodProxy getMethodProxy() {
        return this.methodProxy;
    }

    @Override
    public boolean isCglibProxy() {
        return this.isCglibProxy;
    }

    @Override
    public Object proceed() throws Throwable {
        if (!this.isCglibProxy){
            return this.method.invoke(this.target, this.arguments);
        }
        return this.methodProxy.invokeSuper(this.proxy, this.arguments);
    }


}
