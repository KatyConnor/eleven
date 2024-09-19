package com.hx.nine.eleven.commons.utils;

import com.google.common.collect.Lists;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean 和 map 转换操作类
 *
 * @Author wml
 * @Date 2018-01-04
 */
public class BeanMapUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanMapUtil.class);

    //使用缓存提高效率
    private static final ConcurrentHashMap<String, BeanCopier> mapCaches = new ConcurrentHashMap<>();

    /**
     * 对象转换成Map，转换出来的Map是一个代理对象
     *
     * @param bean 数据源
     * @param <T>
     * @return 返回 Map<String, Object>
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        return beanMap;
    }

    /**
     * map 转换成bean对象
     *
     * @param map  数据源
     * @param bean 对象类型
     * @param <T>
     * @return 返回 对象
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return BeanUtils.setField(map,bean);
    }

    /**
     * map转换成bean对象，类属性不能自己定义自己
     *
     * @param map       数据源
     * @param beanClass 对象类型
     * @param <T>
     * @return 返回 对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        T instance = BeanUtils.newInstance(beanClass);
        return BeanUtils.setField(map,instance);
    }

    /**
     * 将List<T>转换为List<Map<String, Object>> 
     *
     * @param beans
     * @param <T>
     * @return 返回 List<Map<String,Object>>
     */
    public static <T> List<Map<String, Object>> beansToListMap(List<T> beans) {
        List<Map<String, Object>> listMap = Lists.newArrayList();
        if (CollectionUtils.isEmpty(beans)) {
            return listMap;
        }
        for (T bean : beans) {
            Map<String, Object> map = beanToMap(bean);
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T> 
     *
     * @param listMap 数据源
     * @param clazz   返回对象type
     * @param <T>
     * @return 返回 List<T>
     */
    public static <T> List<T> listMapsToBeans(List<Map<String, Object>> listMap, Class<T> clazz) {
        List<T> beans = Lists.newArrayList();
        Optional.ofNullable(listMap).ifPresent(c -> {
            for (Map<String, Object> valueMap : listMap) {
                T object = BeanUtils.newInstance(clazz);
                T bean = mapToBean(valueMap, object);
                beans.add(bean);
            }
        });
        return beans;
    }
}
