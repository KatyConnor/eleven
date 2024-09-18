package com.hx.vertx.jooq.jdbc.utils;

import com.esotericsoftware.reflectasm.MethodAccess;

public class TableFieldUtils {

	private final static String VERSION_METHOD = "getVersion";
	private final static String VERSION_ID = "getId";

	/**
	 * 获取version字段值
	 * @param po   数据库存储对象
	 * @param <P>  对象泛型类型
	 * @return 	   返回属性值
	 */
	public static <P> Object fieldVersion(P po){
		MethodAccess method = MethodAccess.get(po.getClass());
		String[] methods = method.getMethodNames();
		if (checkMethod(methods,VERSION_METHOD)){
			return method.invoke(po,VERSION_METHOD);
		}
		return null;
	}

	public static <P> Object fieldID(P po){
		MethodAccess method = MethodAccess.get(po.getClass());
		String[] methods = method.getMethodNames();
		if (checkMethod(methods,VERSION_ID)){
			return method.invoke(po,VERSION_ID);
		}
		return null;
	}

	public static boolean checkMethod(String[] methods,String methodName){
		for (String str : methods){
			if (str.equals(methodName)){
				return true;
			}
		}
		return false;
	}
}
