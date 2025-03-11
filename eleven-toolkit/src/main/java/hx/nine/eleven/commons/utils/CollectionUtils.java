package hx.nine.eleven.commons.utils;

import hx.nine.eleven.commons.entity.BeanMappingEntity;
import hx.nine.eleven.commons.function.IAction;
import net.sf.cglib.beans.BeanMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wml
 * @Date 2019-03-27
 */
public class CollectionUtils {

    /**
     *  获取指定字段
     * @param list
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> List<String> getObjectsField(List<T> list,String fieldName){
        if (isEmpty(list)){
            return null;
        }

        List<String> results = new ArrayList<>(list.size());
        for (Object obj : list){
            Object value = BeanMap.create(obj).get(fieldName);
            results.add(null != value?String.valueOf(value):"");
        }
        return results;
    }

    /**
     * 集合时候为空
     * @param list
     * @return
     */
    public static boolean isEmpty(Collection<?> list){
        return null == list || list.isEmpty();
    }

    /**
     * 集合是否非空
     * @param list
     * @param <T>
     * @return
     */
    public static boolean isNotEmpty(Collection<?> list){
        return !isEmpty(list);
    }

    /**
     * 复制对象属性
     * @param source 源对象
     * @param target 目标对象
     * @param clazz 泛型类型
     */
    public static <S,T> void copy(Collection<S> source, Collection<T> target, Class<T> clazz){
        copyProperties(source,target,clazz,null);
    }

    /**
     * 复制对象属性
     * @param source 源对象
     * @param target 目标对象
     * @param tClass  泛型类型
     * @param action 指定不同属性映射关系
     */
    public static <S,T,A extends BeanMappingEntity> void copy(Collection<S> source, Collection<T> target, Class<T> tClass, IAction<A> action) {
        Collection<T> result = copy(source, tClass,action);
        target.addAll(result);
    }
    /**
     * 复制对象属性
     * @param source 源对象
     * @param tClass  泛型类型
     * @param action 指定不同属性映射关系
     */
    public static <S,T,A extends BeanMappingEntity> Collection<T> copy(Collection<S> source, Class<T> tClass, IAction<A> action) {
        return source.stream().filter(o -> null != o).map((s)->{
            return BeanUtils.copyProperties(s, tClass,action);
        }).collect(Collectors.toList());
    }

    /**
     * 复制对象属性
     * @param source 源对象
     * @param target 目标对象
     * @param clazz  泛型类型
     * @param properties 指定不同属性映射关系
     */
    public static <S,T> void copyProperties(Collection<S> source, Collection<T> target, Class<T> clazz, Map<String, String> properties) {
        Collection<T> results = copyProperties(source,clazz,properties);
        target.addAll(results);
    }
    /**
     * 复制对象属性
     * @param source 源对象
     * @param clazz  泛型类型
     * @param properties 指定不同属性映射关系
     */
    public static <S,T>  Collection<T> copyProperties(Collection<S> source,Class<T> clazz, Map<String, String> properties) {
        BeanMappingEntity beanMappingEntity = initBeanMappingEntity(properties);
        return source.stream().filter(o -> null != o).map(s->{
            if (null != properties && !properties.isEmpty()){
                return copyProperties(s,clazz,beanMappingEntity);
            }else {
                return BeanUtils.copy(s, clazz);
            }
        }).collect(Collectors.toList());
    }

    private static <S,T> T copyProperties(S s,Class<T> clazz,BeanMappingEntity beanMappingEntity){
        return BeanUtils.copyProperties(s, clazz,()-> beanMappingEntity);
    }

    private static BeanMappingEntity initBeanMappingEntity(Map<String, String> propertites){
        BeanMappingEntity beanMappingEntity = BeanMappingEntity.build();
        propertites.forEach((k,v)->{
            beanMappingEntity.mapping(k,v);
        });
        return beanMappingEntity;
    }
}
