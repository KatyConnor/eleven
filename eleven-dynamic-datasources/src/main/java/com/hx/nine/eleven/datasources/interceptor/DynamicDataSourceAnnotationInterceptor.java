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
package com.hx.nine.eleven.datasources.interceptor;

import com.hx.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.bytebuddy.aop.invoke.Invocation;
import com.hx.nine.eleven.datasources.support.DataSourceClassResolver;
import com.hx.nine.eleven.datasources.DynamicDataSourceContextHolder;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.nine.eleven.core.core.VertxApplicationContextAware;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManager;

import java.lang.reflect.Method;

/**
 * 注解拦截器
 * @author wml
 * @since 1.2.0
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

    /**
     * The identification of SPEL.
     */
    private final DataSourceClassResolver dataSourceClassResolver;


    public DynamicDataSourceAnnotationInterceptor(Boolean allowedPublicOnly) {
        dataSourceClassResolver = new DataSourceClassResolver(allowedPublicOnly);
    }

    /**
     * 切点添加数据源到当前线程队列,然后在执行切点方法，当切点方法执行完毕之后，释放当前数据源
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object target = invocation.getTarget();
        String dsKey = determineDatasourceKey(method,target);
        DynamicDataSourceContextHolder.push(dsKey);
        try {
            return invocation.proceed();
        } finally {
            // 清除数据源信息时，需要同时提交当前数据源未提交事务
            JooqTransactionManager jooqTransactionManager =VertxApplicationContextAware.getBean(JooqTransactionManager.class);
            jooqTransactionManager.commit();
            DynamicDataSourceContextHolder.poll();
        }
    }

    /**
     * 从缓存查找数据，第一次扫描到数据源需要添加到缓存中，如果没找到数据源就获取默认数据源
     * @param method
     * @return
     */
    private String determineDatasourceKey(Method method,Object obj) {
        String dataSource = dataSourceClassResolver.findKey(method,obj);
        // 获取默认数据源
        return StringUtils.isEmpty(dataSource)?null:dataSource;
    }
}
