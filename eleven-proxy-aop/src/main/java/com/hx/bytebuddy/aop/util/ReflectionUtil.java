package com.hx.nine.eleven.bytebuddy.aop.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Optional;

/**
 * 反射处理工具类
 * @author wml
 * @date 2023-04-06
 */
public class ReflectionUtil {

    /**
     * 判断是否有超类
     *
     * @param clazz 目标类
     *
     * @return 如果有返回<code>true</code>，否则返回<code>false</code>
     */
    public static boolean hasSuperClass(Class<?> clazz) {
        return (clazz != null) && !clazz.equals(Object.class);
    }

    /**
     * 根据method名称从class中获取匹配的Method
     * @param clazz          目标class
     * @param methodName     目标class中匹配method名称
     * @param parameterTypes 目标method参数
     * @return 返回匹配的Method
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null || StringUtils.isBlank(methodName)) {
            return null;
        }

        for (Class<?> itr = clazz; hasSuperClass(itr); ) {
            Method[] methods = itr.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }
            itr = itr.getSuperclass();
        }
        return null;
    }

    /**
     * 根据method名称从Object对象中获取对象的Method
     * @param object          目标对象
     * @param methodName      方法名称
     * @param parameterTypes  方法参数
     * @return  返回匹配的Method
     */
    public static Method getMethod(Object object, String methodName, Class<?>... parameterTypes) {
        if (object == null || StringUtils.isBlank(methodName)) {
            return null;
        }

        for (Class<?> itr = object.getClass(); hasSuperClass(itr); ) {
            Method[] methods = itr.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }
            itr = itr.getSuperclass();
        }
        return null;
    }

    /**
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newInstance(final Class<T> clazz,Object[] args) {
        Constructor<?>[] constructors = getAllConstructorsOfClass(clazz, true);
        if (ArrayUtils.isEmpty(constructors)) {
            return null;
        }

        // 根据输入参数创建对象
        for (Constructor<?> constructor : constructors) {
            if (Arrays.equals(constructor.getParameterTypes(), args)){
                try {
                    return (T) constructor.newInstance(args);
                } catch (Exception e) {
                    throw new RuntimeException("create Constructor obj with method [{"+ Arrays.toString(args) +"}] for class " + clazz.getName());
                }
            }
        }
        return null;
    }

    /**
     * 默认构造函数初始化实例
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newDefaultInstance(final Class<T> clazz) {
        try {
            ConstructorAccess constructorAccess = ConstructorAccess.get(clazz);
            return (T) constructorAccess.newInstance();
        }catch (Exception ex){
            throw new RuntimeException("No default non parameter constructor found for class " + clazz.getName());
        }
    }



    /**
     * 获取当前类的所有构造方法
     * @param clazz       目标类
     * @param accessible  是否可访问
     * @return 返回所有的构造函数
     */
    public static Constructor<?>[] getAllConstructorsOfClass(final Class<?> clazz, boolean accessible) {
        if (clazz == null) {
            return null;
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (ArrayUtils.isNotEmpty(constructors)) {
            AccessibleObject.setAccessible(constructors, accessible);
        }
        return constructors;
    }

    /**
     * 获取默认的构造方法
     * @param constructors  所有构造函数
     * @return 返回默认的Optional<? extends Constructor<?>> 构造函数
     */
    private static Optional<? extends Constructor<?>> getDefaultConstructor(Constructor<?>[] constructors) {
        if (ArrayUtils.isEmpty(constructors)) {
            return Optional.absent();
        }

        for (Constructor<?> constructor : constructors) {
            if (ArrayUtils.isEmpty(constructor.getParameterTypes())) {
                return Optional.of(constructor);
            }
        }

        return Optional.absent();
    }



}
