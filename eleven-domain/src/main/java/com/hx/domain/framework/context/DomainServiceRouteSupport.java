package com.hx.domain.framework.context;

import com.hx.domain.framework.annotations.domain.support.DomainServiceMethod;
import com.hx.domain.framework.annotations.domain.support.MapperMethod;
import com.hx.domain.framework.constant.WebHttpBodyConstant;
import com.hx.domain.framework.entity.RouterMethodEntity;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.lang.commons.utils.Builder;
import com.hx.lang.commons.utils.ObjectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动切换
 */
public class DomainServiceRouteSupport {

	private static final String DOMAIN_SUPPORT = "DOMAIN_SUPPORT";
	private static final String MAPPER_METHOD = "MAPPER_METHOD";

	public static Object invokDomainService(String subTradeCode, Object obj, Object... args) {
		String domainServiceMethod = DomainContextAware.build().getDomainContext().getDomainServiceMethod();
		return invoke(subTradeCode, obj, false,domainServiceMethod, args);
	}

	public static Object invokMapperFactory(String subTradeCode, Object obj, Object... args) {
		String mapperFactoryMethod = DomainContextAware.build().getDomainContext().getMapperFactoryMethod();
		return invoke(subTradeCode, obj, true,mapperFactoryMethod,args);
	}

	private static Object invoke(String subTradeCode, Object obj, boolean isMapper,String domainOrMapperMethod,Object... args) {
		Class classzz = obj.getClass();
		Method[] methods = classzz.getDeclaredMethods();
		MethodAccess methodAccess = MethodAccess.get(classzz);
		String mapperFactoryMethod = null;
		String domainServiceMethod = null;
		if (ObjectUtils.isNotEmpty(domainOrMapperMethod)) {
			if (domainOrMapperMethod.startsWith(WebHttpBodyConstant.MAPPER_FACTORY_METHOD)){
				mapperFactoryMethod = domainOrMapperMethod;
			}
			if (domainOrMapperMethod.startsWith(WebHttpBodyConstant.DOMAIN_SERVICE_METHOD)){
				domainServiceMethod = domainOrMapperMethod;
			}
		}
		String methodName = checkMethodName(methods, isMapper, subTradeCode, isMapper?mapperFactoryMethod:domainServiceMethod);
		//使用完毕清空当前mapperFactoryMethod
		if (ObjectUtils.isNotEmpty(mapperFactoryMethod)) {
			DomainContextAware.build().getDomainContext().setMapperFactoryMethod(null);
		}
		// 如果为空，返回null, 走默认方法调用
		if (methodName != null) {
			return methodAccess.invoke(obj, methodName, args);
		}
		return null;
	}

	private static String checkMethodName(Method[] methods, boolean isMapper, String subTradeCode, String invokeMethod) {
		Map<String, List<RouterMethodEntity>> methodMap = new HashMap<>();
		for (Method method : methods) {
			if (isMapper) {
				checkMapperFactoryMethod(method, subTradeCode, invokeMethod,methodMap);
			} else {
				checkDomainSupportMethod(method, subTradeCode,invokeMethod, methodMap);
			}
		}
		return getMethodName(isMapper,subTradeCode,methodMap);
	}

	private static void checkMapperFactoryMethod(Method method, String subTradeCode,String mapperFactoryMethod,
												   Map<String, List<RouterMethodEntity>> methodMap) {
		MapperMethod mapperMethod = method.getAnnotation(MapperMethod.class);
		if (mapperMethod == null) {
			mapperMethod = method.getDeclaredAnnotation(MapperMethod.class);
		}
		// 没有指明mapperFactoryMethod，则根据subTradeCode默认匹配唯一，如果不唯一抛出异常
		if (mapperMethod != null) {
			String childTradeCode = mapperMethod.subTradeCode();
			String mapperFactoryMethodOfAn = mapperMethod.mapperFactoryMethod();
			if (ObjectUtils.isEmpty(mapperFactoryMethod)) {
				if (childTradeCode.equals(subTradeCode) && ObjectUtils.isEmpty(mapperFactoryMethodOfAn)) {
					addRouterMethodEntity(MAPPER_METHOD,childTradeCode,method.getName(),null,methodMap);
				}
			} else {
				// 判断子交易码和执行编号
				if (mapperFactoryMethod.equals(mapperFactoryMethodOfAn) && childTradeCode.equals(subTradeCode)) {
					addRouterMethodEntity(MAPPER_METHOD,childTradeCode,method.getName(),mapperFactoryMethodOfAn,methodMap);
				}
			}
		}
	}

	/**
	 * 匹配满足条件的DomainSupport方法
	 * @param method
	 * @param subTradeCode
	 * @return
	 */
	private static void checkDomainSupportMethod(Method method, String subTradeCode,String domainServiceMethod, Map<String, List<RouterMethodEntity>> methodMap) {
		DomainServiceMethod domainServiceMethodAn = method.getAnnotation(DomainServiceMethod.class);
		if (domainServiceMethodAn == null) {
			domainServiceMethodAn = method.getDeclaredAnnotation(DomainServiceMethod.class);
		}
		if (domainServiceMethodAn != null) {
			String childTradeCode = domainServiceMethodAn.subTradeCode();
			String domainServiceMethodOfAn = domainServiceMethodAn.method();
			if (ObjectUtils.isEmpty(domainServiceMethod)) {
				if (childTradeCode.equals(subTradeCode) && ObjectUtils.isEmpty(domainServiceMethodOfAn)) {
					addRouterMethodEntity(DOMAIN_SUPPORT,childTradeCode,method.getName(),null,methodMap);
				}
			} else {
				// 判断子交易码和执行编号
				if (domainServiceMethod.equals(domainServiceMethodOfAn) && childTradeCode.equals(subTradeCode)) {
					addRouterMethodEntity(DOMAIN_SUPPORT,childTradeCode,method.getName(),domainServiceMethodOfAn,methodMap);
				}
			}
		}
	}

	private static String getMethodName(boolean isMapper,String subTradeCode,Map<String, List<RouterMethodEntity>> methodMap){
		if (isMapper) {
			List<RouterMethodEntity> routerMethodEntityList = methodMap.get(MAPPER_METHOD + subTradeCode);
			if (ObjectUtils.isEmpty(routerMethodEntityList) || routerMethodEntityList.size() > 1){
				throw new RuntimeException("没有匹配到满足条件的MapperFactory可执行方法");
			}
			return routerMethodEntityList.get(0).getMethodName();
		}

		List<RouterMethodEntity> routerMethodEntityList = methodMap.get(DOMAIN_SUPPORT + subTradeCode);
		if (ObjectUtils.isEmpty(routerMethodEntityList) || routerMethodEntityList.size() > 1){
			throw new RuntimeException("没有匹配到满足条件的DomainService可执行方法");
		}
		return routerMethodEntityList.get(0).getMethodName();
	}

	private static void addRouterMethodEntity(String methodType,String subTradeCode,String methodName,
											  String mapperFactoryMethod,Map<String, List<RouterMethodEntity>> methodMap){
		List<RouterMethodEntity> routerMethodEntityList = methodMap.get(methodType + subTradeCode);
		if (ObjectUtils.isEmpty(routerMethodEntityList)) {
			routerMethodEntityList = new ArrayList<>();
		}
		RouterMethodEntity routerMethodEntity = Builder.of(RouterMethodEntity::new)
				.with(RouterMethodEntity::setSubTradeCode, subTradeCode)
				.with(RouterMethodEntity::setMapperFactoryMethod, mapperFactoryMethod)
				.with(RouterMethodEntity::setMethodName, methodName).build();
		routerMethodEntityList.add(routerMethodEntity);
		methodMap.put(methodType + subTradeCode, routerMethodEntityList);
	}
}
