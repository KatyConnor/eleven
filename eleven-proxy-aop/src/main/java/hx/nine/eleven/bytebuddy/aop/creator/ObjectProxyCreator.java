package hx.nine.eleven.bytebuddy.aop.creator;

import hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;

/**
 * 代理类创建器
 *
 * @author wml
 * @date 2023-04-13
 */
public interface ObjectProxyCreator {

    /**
     * 创建一个代理对象
     * @param target      目标对象
     * @param interceptor 拦截器
     * @param <T>        目标对象类型
     * @return           返回生成的代理对象
     */
    <T> T createInterceptorProxy(Class<T> target, MethodInterceptor interceptor);

    /**
     * 创建代理对象
     * @param classLoader  类加载器
     * @param target       目标对象
     * @param interceptor  拦截器
     * @param <T>          标对象类型
     * @return            返回生成的代理对象
     */
    <T> T createInterceptorProxy(ClassLoader classLoader, Class<T> target, MethodInterceptor interceptor);

    /**
     * 创建代理对象
     * @param target  目标对象
     * @param <T>     标对象类型
     * @return        返回生成的代理对象
     */
    <T> T createAdviceInterceptorProxy(Class<T> target);

    /**
     * 创建代理对象
     * @param classLoader 类加载器
     * @param target      目标对象
     * @param <T>         标对象类型
     * @return            返回生成的代理对象
     */
    <T> T createAdviceInterceptorProxy(ClassLoader classLoader,Class<T> target);
}
