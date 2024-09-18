/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hx.db.datasources.support;

import com.hx.db.datasources.annotation.HXDataSource;
import com.hx.db.datasources.aop.MethodClassKey;
import com.hx.db.datasources.utils.ClassUtils;
import com.hx.db.datasources.utils.HXLogger;
import com.hx.lang.commons.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据源解析器,解析当前线程数据源
 *
 * @author wml
 * @since 2.3.0
 */
@Slf4j
public class DataSourceClassResolver {

    /**
     * 缓存方法对应的数据源
     */
    private final Map<Object, String> dsCache = new ConcurrentHashMap<>();
    /**
     * 只能允许public method 抽象继承扩展修改aop 条件
     */
    private final boolean allowedPublicOnly;

    /**
     * 加入扩展, 给外部一个修改aop条件的机会
     *
     * @param allowedPublicOnly 只允许公共的方法, 默认为true
     */
    public DataSourceClassResolver(boolean allowedPublicOnly) {
        this.allowedPublicOnly = allowedPublicOnly;
    }

    /**
     * 从缓存获取数据源
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    public String findKey(Method method, Object targetObject) {
        if (method.getDeclaringClass() == Object.class) {
            throw new RuntimeException("目标对象类型不对，java.lang.Object");
        }
        Object cacheKey = new MethodClassKey(method, targetObject.getClass());
        String dataSource = this.dsCache.get(cacheKey);
        if (dataSource == null) {
            dataSource = computeDatasource(method, targetObject);
            if (StringUtils.isEmpty(dataSource)){
              return null;
            }
            this.dsCache.put(cacheKey, dataSource);
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
    private String computeDatasource(Method method, Object targetObject) {
        //1、 判断当前方法是否是public
        if (this.allowedPublicOnly && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        //2、 从当前方法接口中查找@HXDataSource，获取DataSource
        String dsAttr = findDataSourceAttribute(method);
        if (dsAttr != null) {
            return dsAttr;
        }

        Class<?> targetClass = targetObject.getClass();
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // 如果是JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        //2. 从桥接方法查找
        dsAttr = findDataSourceAttribute(specificMethod);
        if (dsAttr != null) {
            return dsAttr;
        }
        // 从当前方法声明的类查找
        dsAttr = findDataSourceAttribute(userClass);
        if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return dsAttr;
        }

        //since 3.4.1 从接口查找，只取第一个找到的
        for (Class<?> interfaceClazz : ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            dsAttr = findDataSourceAttribute(interfaceClazz);
            if (dsAttr != null) {
                return dsAttr;
            }
        }
        // 如果存在桥接方法
        if (specificMethod != method) {
            // 从桥接方法查找
            dsAttr = findDataSourceAttribute(method);
            if (dsAttr != null) {
                return dsAttr;
            }

            // 从桥接方法声明的类查找
            dsAttr = findDataSourceAttribute(method.getDeclaringClass());
            if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
                return dsAttr;
            }
        }

        // 如果都为找到就获取默认数据源
        return getDefaultDataSourceAttr(targetObject);
    }

    /**
     * 默认的获取数据源名称方式
     *
     * @param targetObject 目标对象
     * @return ds
     */
    private String getDefaultDataSourceAttr(Object targetObject) {
        Class<?> targetClass = targetObject.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                String datasourceAttr = findDataSourceAttribute(currentClass);
                if (datasourceAttr != null) {
                    return datasourceAttr;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 通过 AnnotatedElement 查找标记的注解, 映射为 DatasourceHolder
     *
     * @param ae AnnotatedElement
     * @return 数据源映射持有者
     */
    private String findDataSourceAttribute(AnnotatedElement ae) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, HXDataSource.class);
        if (attributes != null) {
            return attributes.getString("value");
        }
        HXLogger.build(this).warn("当前对象未找到 HXDataSource 注解");
        return null;
    }
}
