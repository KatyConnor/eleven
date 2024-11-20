package hx.nine.eleven.datasources.utils;

import hx.nine.eleven.bytebuddy.aop.util.Assert;
import hx.nine.eleven.commons.utils.ConcurrentReferenceHashMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @auth wml
 * @date 2024/11/13
 */
public class ReflectionUtils {

	private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap(256);
	private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

	public static final ReflectionUtils.MethodFilter USER_DECLARED_METHODS = (method) -> {
		return !method.isBridge() && !method.isSynthetic() && method.getDeclaringClass() != Object.class;
	};

	@FunctionalInterface
	public interface MethodCallback {
		void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
	}

	@FunctionalInterface
	public interface MethodFilter {
		boolean matches(Method method);

		default ReflectionUtils.MethodFilter and(ReflectionUtils.MethodFilter next) {
			Assert.notNull(next, "Next MethodFilter must not be null");
			return (method) -> {
				return this.matches(method) && next.matches(method);
			};
		}
	}

	public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
		List<Method> methods = new ArrayList(20);
		doWithMethods(leafClass, methods::add);
		return methods.toArray(EMPTY_METHOD_ARRAY);
	}

	public static void doWithMethods(Class<?> clazz,ReflectionUtils.MethodCallback mc) {
		doWithMethods(clazz, mc, null);
	}

	public static void doWithMethods(Class<?> clazz, ReflectionUtils.MethodCallback mc,ReflectionUtils.MethodFilter mf) {
		if (mf != USER_DECLARED_METHODS || clazz != Object.class) {
			Method[] methods = getDeclaredMethods(clazz, false);
			Method[] var4 = methods;
			int var5 = methods.length;

			int var6;
			for(var6 = 0; var6 < var5; ++var6) {
				Method method = var4[var6];
				if (mf == null || mf.matches(method)) {
					try {
						mc.doWith(method);
					} catch (IllegalAccessException var9) {
						throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + var9);
					}
				}
			}

			if (clazz.getSuperclass() == null || mf == USER_DECLARED_METHODS && clazz.getSuperclass() == Object.class) {
				if (clazz.isInterface()) {
					Class[] var10 = clazz.getInterfaces();
					var5 = var10.length;

					for(var6 = 0; var6 < var5; ++var6) {
						Class<?> superIfc = var10[var6];
						doWithMethods(superIfc, mc, mf);
					}
				}
			} else {
				doWithMethods(clazz.getSuperclass(), mc, mf);
			}

		}
	}

	private static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
		Assert.notNull(clazz, "Class must not be null");
		Method[] result = (Method[])declaredMethodsCache.get(clazz);
		if (result == null) {
			try {
				Method[] declaredMethods = clazz.getDeclaredMethods();
				List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
				if (defaultMethods != null) {
					result = new Method[declaredMethods.length + defaultMethods.size()];
					System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
					int index = declaredMethods.length;

					for(Iterator var6 = defaultMethods.iterator(); var6.hasNext(); ++index) {
						Method defaultMethod = (Method)var6.next();
						result[index] = defaultMethod;
					}
				} else {
					result = declaredMethods;
				}

				declaredMethodsCache.put(clazz, result.length == 0 ? EMPTY_METHOD_ARRAY : result);
			} catch (Throwable var8) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader() + "]", var8);
			}
		}

		return result.length != 0 && defensive ? (Method[])result.clone() : result;
	}

	private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
		List<Method> result = null;
		Class[] var2 = clazz.getInterfaces();
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Class<?> ifc = var2[var4];
			Method[] var6 = ifc.getMethods();
			int var7 = var6.length;

			for(int var8 = 0; var8 < var7; ++var8) {
				Method ifcMethod = var6[var8];
				if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
					if (result == null) {
						result = new ArrayList();
					}

					result.add(ifcMethod);
				}
			}
		}

		return result;
	}

	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");

		for(Class searchType = clazz; searchType != null; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType, false);
			Method[] var5 = methods;
			int var6 = methods.length;

			for(int var7 = 0; var7 < var6; ++var7) {
				Method method = var5[var7];
				if (name.equals(method.getName()) && (paramTypes == null || hasSameParams(method, paramTypes))) {
					return method;
				}
			}
		}

		return null;
	}

	private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
		return paramTypes.length == method.getParameterCount() && Arrays.equals(paramTypes, method.getParameterTypes());
	}
}
