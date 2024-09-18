package com.hx.bytebuddy.aop.creator;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.hx.bytebuddy.aop.ObjectProxyCreator;
import com.hx.bytebuddy.aop.interceptor.advice.AdvisorMethodInterceptor;
import com.hx.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.bytebuddy.aop.interceptor.ProxyMethodDelegationInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用<tt>ByteBuddy</tt>来做动态代理的{@link com.hx.bytebuddy.aop.ObjectProxyCreator}
 *
 * @author wml
 * @date 2023-04-06
 */
public class ByteBuddyCreator implements ObjectProxyCreator{

    private final static Logger LOGGER = LoggerFactory.getLogger(ByteBuddyCreator.class);

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
     * 类拦截器模式
     * @param classLoader   类加载器
     * @param target        目标类
     * @param interceptor   拦截器实现
     * @param <T>           目标类类型
     * @return              返回创建的代理类
     */
    private <T> T createProxy(ClassLoader classLoader,Class<T> target,MethodInterceptor interceptor) {
        try {
            ConstructorAccess constructorAccess = ConstructorAccess.get(target);
            return new ByteBuddy()
                    .subclass(target)
                    .method(ElementMatchers.isDeclaredBy(target))
                    .intercept(MethodDelegation.to(new ProxyMethodDelegationInterceptor(interceptor,constructorAccess.newInstance())))
                    .make()
                    .load(classLoader)
                    .getLoaded().newInstance();
        } catch (Exception e) {
            LOGGER.error("创建代理类对象失败: {}",e);
            return null;
        }
    }

    /**
     * 创建advice类型代理类
     * @param target  目标代理类
     * @param <T>     代理类型
     * @return        返回生成的代理类
     */
    private <T> T createAdviceProxy(ClassLoader classLoader,Class<T> target){
        try {
            return new ByteBuddy()
                    .subclass(target)
                    .method(ElementMatchers.isDeclaredBy(target))
                    .intercept(Advice.to(AdvisorMethodInterceptor.class))//MethodDelegation
                    .make()
                    .load(classLoader)
                    .getLoaded().newInstance();
        } catch (Exception e) {
            LOGGER.error("创建代理类对象失败: {}",e);
            return null;
        }
    }
}


