package hx.nine.eleven.file.utils;

import hx.nine.eleven.file.ConcurrentReferenceHashMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class ReflectionUtils {

    public static final ReflectionUtils.MethodFilter USER_DECLARED_METHODS = (method) -> {
        return !method.isBridge() && !method.isSynthetic();
    };
    public static final ReflectionUtils.FieldFilter COPYABLE_FIELDS = (field) -> {
        return !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
    };
    private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];
    private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap(256);
    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap(256);

    public ReflectionUtils() {
    }

    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        } else if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method or field: " + ex.getMessage());
        } else {
            if (ex instanceof InvocationTargetException) {
                handleInvocationTargetException((InvocationTargetException)ex);
            }

            if (ex instanceof RuntimeException) {
                throw (RuntimeException)ex;
            } else {
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    public static void handleInvocationTargetException(InvocationTargetException ex) {
        rethrowRuntimeException(ex.getTargetException());
    }

    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException)ex;
        } else if (ex instanceof Error) {
            throw (Error)ex;
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static void rethrowException(Throwable ex) throws Exception {
        if (ex instanceof Exception) {
            throw (Exception)ex;
        } else if (ex instanceof Error) {
            throw (Error)ex;
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static <T> Constructor<T> accessibleConstructor(Class<T> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Constructor<T> ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }

    public static void makeAccessible(Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }

    }

    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, EMPTY_CLASS_ARRAY);
    }

    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");

        for(Class searchType = clazz; searchType != null; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType, false);
            int length = methods.length;

            for(int i = 0; i < length; ++i) {
                Method method = methods[i];
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

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, EMPTY_OBJECT_ARRAY);
    }

    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception exception) {
            handleReflectionException(exception);
            throw new IllegalStateException("Should never get here");
        }
    }

    public static boolean declaresException(Method method, Class<?> exceptionType) {
        Assert.notNull(method, "Method must not be null");
        Class<?>[] declaredExceptions = method.getExceptionTypes();
        int length = declaredExceptions.length;

        for(int i = 0; i < length; ++i) {
            Class<?> declaredException = declaredExceptions[i];
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true;
            }
        }

        return false;
    }

    public static void doWithLocalMethods(Class<?> clazz, ReflectionUtils.MethodCallback mc) {
        Method[] methods = getDeclaredMethods(clazz, false);
        int length = methods.length;

        for(int i = 0; i < length; ++i) {
            Method method = methods[i];

            try {
                mc.doWith(method);
            } catch (IllegalAccessException var8) {
                throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + var8);
            }
        }

    }

    public static void doWithMethods(Class<?> clazz, ReflectionUtils.MethodCallback mc) {
        doWithMethods(clazz, mc, (ReflectionUtils.MethodFilter)null);
    }

    public static void doWithMethods(Class<?> clazz, ReflectionUtils.MethodCallback mc, ReflectionUtils.MethodFilter mf) {
        Method[] methods = getDeclaredMethods(clazz, false);
        int var5 = methods.length;
        for(int i = 0; i < var5; ++i) {
            Method method = methods[i];
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
                Class[] classes = clazz.getInterfaces();
                for(int i = 0; i < classes.length; ++i) {
                    Class<?> superIfc = classes[i];
                    doWithMethods(superIfc, mc, mf);
                }
            }
        } else {
            doWithMethods(clazz.getSuperclass(), mc, mf);
        }

    }

    public static Method[] getAllDeclaredMethods(Class<?> leafClass) {
        List<Method> methods = new ArrayList(32);
        doWithMethods(leafClass, methods::add);
        return (Method[])methods.toArray(EMPTY_METHOD_ARRAY);
    }

    public static Method[] getUniqueDeclaredMethods(Class<?> leafClass) {
        return getUniqueDeclaredMethods(leafClass, (ReflectionUtils.MethodFilter)null);
    }

    public static Method[] getUniqueDeclaredMethods(Class<?> leafClass,ReflectionUtils.MethodFilter mf) {
        List<Method> methods = new ArrayList(32);
        doWithMethods(leafClass, (method) -> {
            boolean knownSignature = false;
            Method methodBeingOverriddenWithCovariantReturnType = null;
            Iterator iterator = methods.iterator();

            while(iterator.hasNext()) {
                Method existingMethod = (Method)iterator.next();
                if (method.getName().equals(existingMethod.getName()) && method.getParameterCount() == existingMethod.getParameterCount() && Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())) {
                    if (existingMethod.getReturnType() != method.getReturnType() && existingMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
                        methodBeingOverriddenWithCovariantReturnType = existingMethod;
                        break;
                    }

                    knownSignature = true;
                    break;
                }
            }

            if (methodBeingOverriddenWithCovariantReturnType != null) {
                methods.remove(methodBeingOverriddenWithCovariantReturnType);
            }

            if (!knownSignature && !isCglibRenamedMethod(method)) {
                methods.add(method);
            }

        }, mf);
        return (Method[])methods.toArray(EMPTY_METHOD_ARRAY);
    }

    public static Method[] getDeclaredMethods(Class<?> clazz) {
        return getDeclaredMethods(clazz, true);
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
            } catch (Throwable throwable) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader() + "]", throwable);
            }
        }

        return result.length != 0 && defensive ? (Method[])result.clone() : result;
    }

    private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
        List<Method> result = null;
        Class[] classes = clazz.getInterfaces();
        int length = classes.length;

        for(int i = 0; i < length; ++i) {
            Class<?> ifc = classes[i];
            Method[] methods = ifc.getMethods();
            int methodsLength = methods.length;

            for(int j = 0; j < methodsLength; ++j) {
                Method ifcMethod = methods[j];
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

    public static boolean isEqualsMethod( Method method) {
        if (method != null && method.getName().equals("equals")) {
            if (method.getParameterCount() != 1) {
                return false;
            } else {
                return method.getParameterTypes()[0] == Object.class;
            }
        } else {
            return false;
        }
    }

    public static boolean isHashCodeMethod( Method method) {
        return method != null && method.getName().equals("hashCode") && method.getParameterCount() == 0;
    }

    public static boolean isToStringMethod( Method method) {
        return method != null && method.getName().equals("toString") && method.getParameterCount() == 0;
    }

    public static boolean isObjectMethod( Method method) {
        return method != null && (method.getDeclaringClass() == Object.class || isEqualsMethod(method) || isHashCodeMethod(method) || isToStringMethod(method));
    }

    public static boolean isCglibRenamedMethod(Method renamedMethod) {
        String name = renamedMethod.getName();
        if (!name.startsWith("CGLIB$")) {
            return false;
        } else {
            int i;
            for(i = name.length() - 1; i >= 0 && Character.isDigit(name.charAt(i)); --i) {
            }

            return i > "CGLIB$".length() && i < name.length() - 1 && name.charAt(i) == '$';
        }
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }

    }

    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, (Class)null);
    }

    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");

        for(Class searchType = clazz; Object.class != searchType && searchType != null; searchType = searchType.getSuperclass()) {
            Field[] fields = getDeclaredFields(searchType);
            int length = fields.length;

            for(int i = 0; i < length; ++i) {
                Field field = fields[i];
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
        }

        return null;
    }

    public static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
        }

    }

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {
            handleReflectionException(ex);
            throw new IllegalStateException("Should never get here");
        }
    }

    public static void doWithLocalFields(Class<?> clazz, ReflectionUtils.FieldCallback fc) {
        Field[] fields = getDeclaredFields(clazz);
        int length = fields.length;

        for(int i = 0; i < length; ++i) {
            Field field = fields[i];

            try {
                fc.doWith(field);
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + exception);
            }
        }

    }

    public static void doWithFields(Class<?> clazz, ReflectionUtils.FieldCallback fc) {
        doWithFields(clazz, fc, (ReflectionUtils.FieldFilter)null);
    }

    public static void doWithFields(Class<?> clazz, ReflectionUtils.FieldCallback fc, ReflectionUtils.FieldFilter ff) {
        Class targetClass = clazz;

        do {
            Field[] fields = getDeclaredFields(targetClass);
            int length = fields.length;

            for(int i = 0; i < length; ++i) {
                Field field = fields[i];
                if (ff == null || ff.matches(field)) {
                    try {
                        fc.doWith(field);
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + exception);
                    }
                }
            }

            targetClass = targetClass.getSuperclass();
        } while(targetClass != null && targetClass != Object.class);

    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Field[] result = (Field[])declaredFieldsCache.get(clazz);
        if (result == null) {
            try {
                result = clazz.getDeclaredFields();
                declaredFieldsCache.put(clazz, result.length == 0 ? EMPTY_FIELD_ARRAY : result);
            } catch (Throwable ex) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() + "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
            }
        }

        return result;
    }

    public static void shallowCopyFieldState(Object src, Object dest) {
        Assert.notNull(src, "Source for field copy cannot be null");
        Assert.notNull(dest, "Destination for field copy cannot be null");
        if (!src.getClass().isAssignableFrom(dest.getClass())) {
            throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
        } else {
            doWithFields(src.getClass(), (field) -> {
                makeAccessible(field);
                Object srcValue = field.get(src);
                field.set(dest, srcValue);
            }, COPYABLE_FIELDS);
        }
    }

    public static boolean isPublicStaticFinal(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }

    public static void clearCache() {
        declaredMethodsCache.clear();
        declaredFieldsCache.clear();
    }

    @FunctionalInterface
    public interface FieldFilter {
        boolean matches(Field field);
    }

    @FunctionalInterface
    public interface FieldCallback {
        void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    @FunctionalInterface
    public interface MethodFilter {
        boolean matches(Method method);
    }

    @FunctionalInterface
    public interface MethodCallback {
        void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
    }
}
