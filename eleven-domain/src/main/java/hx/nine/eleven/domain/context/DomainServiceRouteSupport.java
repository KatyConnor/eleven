package hx.nine.eleven.domain.context;

import hx.nine.eleven.domain.annotations.domain.support.DomainServiceMethod;
import hx.nine.eleven.domain.annotations.domain.support.MapperMethod;
import hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import hx.nine.eleven.domain.entity.RouterMethodEntity;
import com.esotericsoftware.reflectasm.MethodAccess;
import hx.nine.eleven.commons.utils.Builder;
import hx.nine.eleven.commons.utils.ObjectUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * domain service facade 执行方法自动切换
 * domain db mapper factory 执行方法自动切换
 * @author wml
 * @date 2022-10-28
 */
public class DomainServiceRouteSupport {

	private static final String DOMAIN_SUPPORT = "DOMAIN_SUPPORT";
	private static final String MAPPER_METHOD = "MAPPER_METHOD";

	/**
	 * 按照子交易码自动匹配 domain service facade接口中定义的交易码方法
	 * @param subTradeCode  子交易码
	 * @param obj           mapper factory 实例对象
	 * @param args          执行方法传递参数
	 * @return
	 */
	public static Object invokDomainService(String subTradeCode, Object obj, Object... args) {
		String domainServiceMethod = DomainContextAware.build().getDomainContext().getDomainServiceMethod();
		return invoke(subTradeCode, obj, false,domainServiceMethod, args);
	}

	/**
	 * 按照子交易码自动匹配 domain mapper factory 数据操作接口中定义的交易码方法
	 * @param subTradeCode  子交易码
	 * @param obj           mapper factory 实例对象
	 * @param args          执行方法传递参数
	 * @return
	 */
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
