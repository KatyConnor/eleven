package com.hx.bytebuddy.aop.util;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.google.common.collect.Maps;
import com.hx.bytebuddy.aop.ObjectProxyCreator;
import com.hx.bytebuddy.aop.interceptor.CglibMethodInterceptor;
import net.sf.cglib.proxy.Enhancer;

/**
 * 动态代理相关的工具类
 * @author wml
 * @date 2023-04-06
 */
public abstract class ProxyUtil {
    // 空对象数据组
    public static final Object[] EMPTY_ARGUMENTS = Emptys.EMPTY_OBJECT_ARRAY;
    public static final Class<?>[] EMPTY_ARGUMENT_TYPES = Emptys.EMPTY_CLASS_ARRAY;
    //
    private static final Map<Class<?>, Class<?>> WRAPPER_CLASS_MAP;
    private static final Map<Class<?>, Object> NULL_VALUE_MAP;
    // 存储拦截器
    private static final ServiceLoader<CglibMethodInterceptor> CGLIB_METHOD_INTERCEPTORS = ServiceLoader.load(CglibMethodInterceptor.class);

    static {
        Map<Class<?>, Class<?>> wrappers = Maps.newHashMapWithExpectedSize(8);
        // 基本类型
        wrappers.put(Integer.TYPE, Integer.class);
        wrappers.put(Character.TYPE, Character.class);
        wrappers.put(Boolean.TYPE, Boolean.class);
        wrappers.put(Short.TYPE, Short.class);
        wrappers.put(Long.TYPE, Long.class);
        wrappers.put(Float.TYPE, Float.class);
        wrappers.put(Double.TYPE, Double.class);
        wrappers.put(Byte.TYPE, Byte.class);
        WRAPPER_CLASS_MAP = Collections.unmodifiableMap(wrappers);

        // 默认值
        Map<Class<?>, Object> nullValues = Maps.newHashMapWithExpectedSize(8);
        nullValues.put(Integer.TYPE, Integer.valueOf(0));
        nullValues.put(Long.TYPE, Long.valueOf(0));
        nullValues.put(Short.TYPE, Short.valueOf((short) 0));
        nullValues.put(Byte.TYPE, Byte.valueOf((byte) 0));
        nullValues.put(Float.TYPE, Float.valueOf(0.0f));
        nullValues.put(Double.TYPE, Double.valueOf(0.0));
        nullValues.put(Character.TYPE, Character.valueOf((char) 0));
        nullValues.put(Boolean.TYPE, Boolean.FALSE);
        NULL_VALUE_MAP = Collections.unmodifiableMap(nullValues);
    }

    public static String getJavaClassName(Class<?> clazz) {
        if (clazz.isArray()) {
            return getJavaClassName(clazz.getComponentType()) + "[]";
        }

        return clazz.getName();
    }

    public static Class<?> getWrapperClass(Class<?> primitiveType) {
        return WRAPPER_CLASS_MAP.get(primitiveType);
    }

    public static <T> T nullValue(Class<T> type) {
        @SuppressWarnings("unchecked")
        T result = (T) NULL_VALUE_MAP.get(type);
        return result;
    }

    /**
     * 获取代理类创建对象
     * @return
     */
    public static <T extends ObjectProxyCreator> T getInstance(Class<T> proxyClass) {
        ConstructorAccess constructorAccess = ConstructorAccess.get(proxyClass);
        return  (T) constructorAccess.newInstance();
    }

    /**
     * cglib 创建代理类
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T createCglibProxyInstance(Class<T> target,CglibMethodInterceptor interceptor){
        // 1.cglib工具类
        Enhancer enhancer = new Enhancer();
        // 2.设置父类
        enhancer.setSuperclass(target);
        // 3.设置回调函数
        enhancer.setCallback(interceptor);
        return (T)enhancer.create();
    }

    public static boolean isHashCode(Method method) {
        return "hashCode".equals(method.getName()) && Integer.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 0;
    }

    public static boolean isEqualsMethod(Method method) {
        return "equals".equals(method.getName()) && Boolean.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 1 && Object.class.equals(method.getParameterTypes()[0]);
    }
}
