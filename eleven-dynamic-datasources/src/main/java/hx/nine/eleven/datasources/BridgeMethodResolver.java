///*
//package hx.nine.eleven.datasources;
//
//import hx.nine.eleven.commons.utils.ConcurrentReferenceHashMap;
//import hx.nine.eleven.datasources.utils.ClassUtils;
//import hx.nine.eleven.datasources.utils.ReflectionUtils;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//*/
///**
// * @auth wml
// * @date 2024/11/13
// *//*
//
//public final class BridgeMethodResolver {
//
//	private static final Map<Method, Method> cache = new ConcurrentReferenceHashMap();
//
//	public static Method findBridgedMethod(Method bridgeMethod) {
//		if (!bridgeMethod.isBridge()) {
//			return bridgeMethod;
//		} else {
//			Method bridgedMethod = (Method)cache.get(bridgeMethod);
//			if (bridgedMethod == null) {
//				List<Method> candidateMethods = new ArrayList();
//				ReflectionUtils.MethodFilter filter = (candidateMethod) -> {
//					return isBridgedCandidateFor(candidateMethod, bridgeMethod);
//				};
//				ReflectionUtils.doWithMethods(bridgeMethod.getDeclaringClass(), candidateMethods::add, filter);
//				if (!candidateMethods.isEmpty()) {
//					bridgedMethod = candidateMethods.size() == 1 ? candidateMethods.get(0) : searchCandidates(candidateMethods, bridgeMethod);
//				}
//
//				if (bridgedMethod == null) {
//					bridgedMethod = bridgeMethod;
//				}
//
//				cache.put(bridgeMethod, bridgedMethod);
//			}
//
//			return bridgedMethod;
//		}
//	}
//
//	private static boolean isBridgedCandidateFor(Method candidateMethod, Method bridgeMethod) {
//		return !candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod)
//				&& candidateMethod.getName().equals(bridgeMethod.getName())
//				&& candidateMethod.getParameterCount() == bridgeMethod.getParameterCount();
//	}
//
//	private static Method searchCandidates(List<Method> candidateMethods, Method bridgeMethod) {
//		if (candidateMethods.isEmpty()) {
//			return null;
//		} else {
//			Method previousMethod = null;
//			boolean sameSig = true;
//
//			Method candidateMethod;
//			for(Iterator var4 = candidateMethods.iterator(); var4.hasNext(); previousMethod = candidateMethod) {
//				candidateMethod = (Method)var4.next();
//				if (isBridgeMethodFor(bridgeMethod, candidateMethod, bridgeMethod.getDeclaringClass())) {
//					return candidateMethod;
//				}
//
//				if (previousMethod != null) {
//					sameSig = sameSig && Arrays.equals(candidateMethod.getGenericParameterTypes(), previousMethod.getGenericParameterTypes());
//				}
//			}
//
//			return sameSig ? (Method)candidateMethods.get(0) : null;
//		}
//	}
//
//	static boolean isBridgeMethodFor(Method bridgeMethod, Method candidateMethod, Class<?> declaringClass) {
//		if (isResolvedTypeMatch(candidateMethod, bridgeMethod, declaringClass)) {
//			return true;
//		} else {
//			Method method = findGenericDeclaration(bridgeMethod);
//			return method != null && isResolvedTypeMatch(method, candidateMethod, declaringClass);
//		}
//	}
//
//	private static boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod, Class<?> declaringClass) {
//		Type[] genericParameters = genericMethod.getGenericParameterTypes();
//		if (genericParameters.length != candidateMethod.getParameterCount()) {
//			return false;
//		} else {
//			Class<?>[] candidateParameters = candidateMethod.getParameterTypes();
//
//			for(int i = 0; i < candidateParameters.length; ++i) {
//				ResolvableType genericParameter = ResolvableType.forMethodParameter(genericMethod, i, declaringClass);
//				Class<?> candidateParameter = candidateParameters[i];
//				if (candidateParameter.isArray() && !candidateParameter.getComponentType().equals(genericParameter.getComponentType().toClass())) {
//					return false;
//				}
//
//				if (!ClassUtils.resolvePrimitiveIfNecessary(candidateParameter).equals(ClassUtils.resolvePrimitiveIfNecessary(genericParameter.toClass()))) {
//					return false;
//				}
//			}
//
//			return true;
//		}
//	}
//
//	private static Method findGenericDeclaration(Method bridgeMethod) {
//		for(Class superclass = bridgeMethod.getDeclaringClass().getSuperclass(); superclass != null && Object.class != superclass; superclass = superclass.getSuperclass()) {
//			Method method = searchForMatch(superclass, bridgeMethod);
//			if (method != null && !method.isBridge()) {
//				return method;
//			}
//		}
//
//		Class<?>[] interfaces = ClassUtils.getAllInterfacesForClass(bridgeMethod.getDeclaringClass());
//		return searchInterfaces(interfaces, bridgeMethod);
//	}
//
//	private static Method searchForMatch(Class<?> type, Method bridgeMethod) {
//		try {
//			return type.getDeclaredMethod(bridgeMethod.getName(), bridgeMethod.getParameterTypes());
//		} catch (NoSuchMethodException var3) {
//			return null;
//		}
//	}
//
//	private static Method searchInterfaces(Class<?>[] interfaces, Method bridgeMethod) {
//		Class[] var2 = interfaces;
//		int var3 = interfaces.length;
//
//		for(int var4 = 0; var4 < var3; ++var4) {
//			Class<?> ifc = var2[var4];
//			Method method = searchForMatch(ifc, bridgeMethod);
//			if (method != null && !method.isBridge()) {
//				return method;
//			}
//
//			method = searchInterfaces(ifc.getInterfaces(), bridgeMethod);
//			if (method != null) {
//				return method;
//			}
//		}
//
//		return null;
//	}
//}
//*/
