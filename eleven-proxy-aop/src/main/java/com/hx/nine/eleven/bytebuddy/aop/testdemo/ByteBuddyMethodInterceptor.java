package com.hx.nine.eleven.bytebuddy.aop.testdemo;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.nine.eleven.bytebuddy.aop.annotation.HXDataSource;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.nine.eleven.bytebuddy.aop.invoke.Invocation;
import com.hx.nine.eleven.commons.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class ByteBuddyMethodInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object result = null;
		Method method = invocation.getMethod();
		Object target = invocation.getTarget();
		Object[] arguments = invocation.getArguments();
		String da = findKey(method,target,arguments);
		try {
			System.out.println("方法执行前处理,查找到注解："+da);
			result = invocation.proceed();
			System.out.println("方法执行结束");
		}catch (Throwable ex){
			System.out.println("方法执行异常");
			throw ex;
		}
		return result;
	}

	/**
	 * 从缓存获取数据源
	 *
	 * @param method       方法
	 * @param targetObject 目标对象
	 * @return ds
	 */
	public String findKey(Method method, Object targetObject,Object[] arguments) {
		if (method.getDeclaringClass() == Object.class) {
			throw new RuntimeException("目标对象类型不对，java.lang.Object");
		}
		String dataSource =  computeDatasource(method, targetObject,arguments);
		if (StringUtils.isEmpty(dataSource)){
			return null;
		}
		return dataSource;
	}

	/**
	 * 目标对象/方法查找 @HXDataSource 注解，方法必须是public
	 * 1. 当前方法,当前方法未找到去
	 * 2. 桥接方法
	 * 3. 当前类开始一直找到Object
	 * 4. 支持mybatis-plus, mybatis-spring
	 *
	 * @param method       目标对象执行方法
	 * @param targetObject 目标对象
	 * @return ds
	 */
	private String computeDatasource(Method method, Object targetObject,Object[] arguments) {
		//1、 判断当前方法是否是public
		if (!Modifier.isPublic(method.getModifiers())) {
			return null;
		}
		//2、 从当前方法接口中查找@HXDataSource，获取DataSource
		String dsAttr = findDataSourceAttribute(method);
		if (dsAttr != null) {
			return dsAttr;
		}
		Class<?> currentClass = targetObject.getClass().getSuperclass();
		Class<?>[] argumentsTypes = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			argumentsTypes[i] = arguments[i].getClass();
		}
		String methodName = method.getName();
		Method subMethod = null;
		while (currentClass != Object.class) {
			try {
				subMethod = currentClass.getDeclaredMethod(methodName,argumentsTypes);
				if (subMethod == null){
					subMethod = currentClass.getMethod(methodName,argumentsTypes);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			String datasourceAttr = findDataSourceAttribute(subMethod);
			if (datasourceAttr != null) {
				return datasourceAttr;
			}
			currentClass = currentClass.getSuperclass();
		}

		return null;
	}

	/**
	 * 默认的获取数据源名称方式
	 *
	 * @param targetObject 目标对象
	 * @return ds
	 */
//	private String getDefaultDataSourceAttr(Object targetObject) {
//		Class<?> targetClass = targetObject.getClass();
//		// 如果不是代理类, 从当前类开始, 不断的找父类的声明
//		if (!Proxy.isProxyClass(targetClass)) {
//			Class<?> currentClass = targetClass;
//			while (currentClass != Object.class) {
//				String datasourceAttr = findDataSourceAttribute(currentClass);
//				if (datasourceAttr != null) {
//					return datasourceAttr;
//				}
//				currentClass = currentClass.getSuperclass();
//			}
//		}
//		return null;
//	}

	/**
	 * 通过 AnnotatedElement 查找标记的注解, 映射为 DatasourceHolder
	 *
	 * @return 数据源映射持有者
	 */
	private String findDataSourceAttribute(Method method) {
		HXDataSource dataSource = method.getAnnotation(HXDataSource.class);
		if (dataSource == null){
			dataSource = method.getDeclaredAnnotation(HXDataSource.class);
		}
		if (dataSource != null) {
			return dataSource.value();
		}
		System.out.println("当前对象未找到 HXDataSource 注解");
		return null;
	}
}
