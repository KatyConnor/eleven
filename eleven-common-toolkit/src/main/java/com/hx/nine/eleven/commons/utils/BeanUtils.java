package com.hx.nine.eleven.commons.utils;

import com.hx.nine.eleven.commons.annotation.FieldList;
import com.hx.nine.eleven.commons.annotation.FieldTypeConvert;
import com.hx.nine.eleven.commons.entity.BeanMappingEntity;
import com.hx.nine.eleven.commons.enums.DefaultBaseDataTypeEnums;
import com.hx.nine.eleven.commons.exception.BeanConvertException;
import com.hx.nine.eleven.commons.function.IAction;
import com.hx.nine.eleven.commons.json.convert.FieldConvert;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象复制,必须保证源对象和目标对象的字段类型和命名一直
 *
 * @Author wml
 * @Date 2019-08-18
 */
public class BeanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);
    private static String SET_METHOD = "set";

    //使用缓存提高效率
    private static final ConcurrentHashMap<String, BeanCopier> mapCaches = new ConcurrentHashMap<>();

    /**
     * 对象复制,两个对象之间属性必须命名类型一致
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S source, Class<T> target) {
        return baseMapper(source, target);
    }

    /**
     * 对象复制，两个对象之间属性必须命名类型一致
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S source, T target) {
        return baseMapper(source, target);
    }

    /**
     * List<T> 对象属性值，list内部的对象类型，属性字段命名，字段类型必须保持一致
     *
     * @param sources
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> List<T> copyList(List<S> sources, Class<T> target) {
        List<T> results = Builder.of(ArrayList<T>::new).build();
        sources.stream().forEach(v -> {
            T instance = copyProperties(v, target);
            results.add(instance);
        });
        return results;
    }

    /**
     * 对象属性复制,支持特殊字段自定义处理复制，只需要字段保持一致即可将相同字段赋值给目标对象，
     * 属性字段类型会根据目标对象属性字段类型进行匹配转换
     *
     * @param source
     * @param target
     * @param action
     * @param <S>
     * @param <T>
     * @param <A>
     * @return
     */
    public static <S, T, A extends BeanMappingEntity> T copyProperties(S source, Class<T> target, IAction<A> action) {
        T instance = newInstance(target);//baseMapper(source, target);
        return setField(source, instance, action);
    }

    /**
     * 对象属性复制,支持特殊字段自定义处理复制，只需要字段保持一致即可将相同字段赋值给目标对象，
     * 属性字段类型会根据目标对象属性字段类型进行匹配转换
     *
     * @param source
     * @param target
     * @param action
     * @param <S>
     * @param <T>
     * @param <A>
     * @return
     */
    public static <S, T, A extends BeanMappingEntity> T copyProperties(S source, T target, IAction<A> action) {
        return setField(source, target, action);
    }

    /**
     * 对象字段复制，只需要字段保持一致即可将相同字段赋值给目标对象，
     * 属性字段类型会根据目标对象属性字段类型进行匹配转换
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copyProperties(S source, T target) {
        return setField(source, target);
    }

    /**
     * 对象字段复制，只需要字段保持一致即可将相同字段赋值给目标对象，
     * 属性字段类型会根据目标对象属性字段类型进行匹配转换
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copyProperties(S source, Class<T> target) {
        T instance = newInstance(target);
        return setField(source, instance);
    }

    /**
     * 将一个对象转换成另一个对象
     *
     * @param o
     * @param classzz
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> T convert(Object o, Class<T> classzz) {
        if (classzz.isAssignableFrom(o.getClass())) {
            return (T) o;
        }
        return copy(o, classzz);
    }


    /**
     * 对象之间字段赋值，字段类型，命名必须保持一致
     *
     * @param source
     * @param instance
     * @param action
     * @param <T>
     * @param <S>
     * @param <A>
     * @return
     */
    public static <T, S, A extends BeanMappingEntity> T setField(S source, T instance, IAction<A> action) {
        Map sourceMap = source instanceof Map ? (Map) source : BeanMap.create(source);
        BeanMap beanMap = BeanMap.create(instance);
        sourceMap.forEach((k, v) -> {
            if (beanMap.containsKey(k)) {
                beanMap.put(k, v);
            }
        });

        if (!Optional.ofNullable(action).isPresent()) {
            return (T) beanMap.getBean();
        }
        BeanMappingEntity beanMappingEntity = action.run();
        if (!Optional.ofNullable(beanMappingEntity).isPresent()) {
            return (T) beanMap.getBean();
        }

        List<BeanMappingEntity.MappingEntity> mappingEntities = beanMappingEntity.getMappingEntitys();
        Optional.ofNullable(mappingEntities).ifPresent((entities) -> {
            entities.forEach(o -> {
                if (StringUtils.isNotBlank(o.getSourceField()) && StringUtils.isNotBlank(o.getTargetField())) {
                    beanMap.put(o.getTargetField(), sourceMap.get(o.getSourceField()));
                }
                if (StringUtils.isNotBlank(o.getIgnoreField())) {
                    beanMap.put(o.getIgnoreField(), null);
                }
            });
        });

        return (T) beanMap.getBean();
    }

    /**
     * 指定属性字段赋值
     *
     * @param value
     * @param instance 被复制对象
     * @param field    被复制对象属性字段
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> T setField(V value, T instance, String field) {
        BeanMap beanMap = BeanMap.create(instance);
        beanMap.put(field, value);
        return (T) beanMap.getBean();
    }

    /**
     * 属性字段赋值
     *
     * @param source
     * @param instance
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> T setField(S source, T instance) {
        if (!Optional.ofNullable(source).isPresent()) {
            return instance;
        }
        Map sourceMap = source instanceof Map ? (Map) source : BeanMap.create(source);
        BeanMap beanMap = BeanMap.create(instance);
        Map<String, Field> fieldMap = new HashMap<>();
        Map<String, Class> classMap = new HashMap<>();
        Map<String, Object> returnObjMap = new HashMap<>();
        Map<String, Boolean> fieldSetMethodMap = new HashMap<>();
        findFieldTypeClass(instance.getClass(), classMap, fieldMap, fieldSetMethodMap);
        sourceMap.forEach((k, v) -> {
            if (Optional.ofNullable(v).isPresent()) {
                if (beanMap.containsKey(k)) {
                    // 属性字段set方法是否有返回值并且添加FieldSetMethodReturn注解
                    Field field = fieldMap.get(k.toString());
                    if(field == null){
                        field = fieldMap.get(StringUtils.convertFirstUpperCase(k.toString()));
                    }
                    if (field == null){
                        LOGGER.warn("field [{}] not in [{}]",k.toString(),instance.getClass());
                    }else {
                        if (fieldSetMethodMap.get(SET_METHOD + StringUtils.convertFirstUpperCase(field.getName())) != null) {
                            Object value = getValue(beanMap, field, classMap, k.toString(), v);
                            returnObjMap.put(k.toString(), value);
                        } else {
                            Object value = getValue(beanMap, field, classMap, k.toString(), v);
                            if (value != null){
                                beanMap.put(k, value);
                            }
                        }
                    }
                }
            }
        });

        T returnBean = (T) beanMap.getBean();
        MethodAccess methodAccess = MethodAccess.get(returnBean.getClass());
        returnObjMap.forEach((k, v) -> {
            methodAccess.invoke(returnBean, SET_METHOD + StringUtils.convertFirstUpperCase(k.toString()), v);
        });
        return returnBean;
    }

    /**
     * 初始化一个实例对象
     *
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> beanClass) {
        try {
            ConstructorAccess<T> constructorAccess = ConstructorAccess.get(beanClass);
            return constructorAccess.newInstance();
        } catch (Exception e) {
            LOGGER.error("创建对象异常" + e.getMessage());
        }
        return null;
    }

    private static Object getValue(BeanMap beanMap, Field field, Map<String, Class> classMap, String key, Object value) {
        // 字段是否添加FieldTypeConvert注解
        Class<?> type = beanMap.getPropertyType(key);
        FieldTypeConvert fieldDeserializer = field.getAnnotation(FieldTypeConvert.class);
        if (fieldDeserializer != null) {
            FieldConvert fieldConvert = newInstance(fieldDeserializer.using());
            return fieldConvert.convert(value);
        } else if (isReferenceClass(type)) {
            if (type.isAssignableFrom(value.getClass())) {
                return value;
            }
            Object obj = newInstance(type);
            return setField(value, obj);
        } else if (Collection.class.isAssignableFrom(type)) {
            //字段是Collection集合
            if (value instanceof List) {
                List<Object> valueMap = (List) value;
                Class<?> fieldClass = classMap.get(key);
                if (fieldClass == null) {
                    LOGGER.warn("目标对象字段[{}]属于List集合，并且没有指定明确的泛型类型，将跳过该字段赋值，可以尝试添加@FieldList注解",type.getName());
                    return null;
                }
                List<Object> list = new ArrayList<>();
                for (Object obj : valueMap) {
                    if (obj instanceof Map) {
                        if (!Map.class.isAssignableFrom(fieldClass)) {
                            Object object = newInstance(fieldClass);
                            object = setField(obj, object);
                            list.add(object);
                        } else {
                            list.add(obj);
                        }
                        continue;
                    }

                    if (fieldClass.isAssignableFrom(obj.getClass()) || obj.getClass().isAssignableFrom(fieldClass)) {
                        list.add(obj);
                        continue;
                    }

                    Object object = newInstance(fieldClass);
                    object = setField(obj, object);
                    list.add(object);
                }
                return list;
            } else {
                // @TODO 抛出异常，属性类型不匹配
                throw new BeanConvertException(value.getClass().getName() + "不能直接转换成" + type.getName());
            }
        } else if (Map.class.isAssignableFrom(type)) {
            return value instanceof Map ? value : BeanMapUtil.beanToMap(value);
        } else {
            return value;
        }
    }

    /**
     * 简单对象之间的属性复制
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    private static <S, T> T baseMapper(S source, T target) {
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
        return target;
    }

    /**
     * 简单对象之间属性复制
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    private static <S, T> T baseMapper(S source, Class<T> target) {
        BeanCopier copier = getBeanCopier(source.getClass(), target);
        T instance = null;
        try {
            instance = newInstance(target);
        } catch (Exception e) {
            LOGGER.error("mapper 创建对象异常" + e.getMessage());
        }
        copier.copy(source, instance, null);
        return instance;
    }

    /**
     * 获取BeanCopier 对象
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     */
    private static <S, T> BeanCopier getBeanCopier(Class<S> source, Class<T> target) {
        String baseKey = generateKey(source, target);
        BeanCopier copier;
        if (!mapCaches.containsKey(baseKey)) {
            copier = BeanCopier.create(source, target, false);
            mapCaches.put(baseKey, copier);
        } else {
            copier = mapCaches.get(baseKey);
        }
        return copier;
    }

    /**
     * 生成缓存key
     *
     * @param class1
     * @param class2
     * @return
     */
    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    /**
     * 查找属性中是否有添加@FieldList注解字段，根据注解标识的class类型将值进行转换
     *
     * @param beanClass
     * @param classMap
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> void findFieldTypeClass(Class<T> beanClass, Map<String, Class> classMap, Map<String, Field> fieldMap, Map<String, Boolean> fieldSetMethodMap) {
        Field[] fields = beanClass.getDeclaredFields();
        Method[] methods = beanClass.getDeclaredMethods();
        for (Method method : methods) {
            Class<?> returnType = method.getReturnType();
            if (returnType != null) {
                fieldSetMethodMap.put(method.getName(), true);
            }
        }

        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (Collection.class.isAssignableFrom(fieldType)) {
                FieldList fieldList = field.getAnnotation(FieldList.class);
                if (ObjectUtils.isEmpty(fieldList)){
                    LOGGER.warn("当前字段[{}]类型[{}]没有添加@FieldList注解，跳过List赋值",field.getName(),field.getType().getName());
                    continue;
                }
                classMap.put(field.getName(), fieldList.convertType());
            }
            fieldMap.put(field.getName(), field);
        }

        Class superClass = beanClass.getSuperclass();
        if (isReferenceClass(superClass) && !superClass.getSimpleName().equals("Object")) {
            findFieldTypeClass(superClass, classMap, fieldMap, fieldSetMethodMap);
        }
    }

    /**
     * 判断传入的class对象是否是基本数据类型
     *
     * @param classzz
     * @return
     */
    private static boolean isBasicTypes(Class<?> classzz) {
        // 基本数据类型,枚举类型、集合,如果当前字段的类型就是当前类本身，则退出，否则会进入死循环
        if (classzz.isEnum() || classzz.isPrimitive() || !DefaultBaseDataTypeEnums.noneMatch(classzz.getSimpleName())) {
            return true;
        }
        return false;
    }

    /**
     * 判断传入的class对象是否是引用类型，包含（接口、抽象类、普通类等所有class对象)
     *
     * @param classzz
     * @return
     */
    private static boolean isReferenceClass(Class<?> classzz) {
        if (isBasicTypes(classzz)) {
            return false;
        }
        // 是接口,抽象类，普通类，则继续寻找字段
        if (classzz.isInterface() || classzz.getClass() instanceof Object) {
            return true;
        }
        return false;
    }
}
