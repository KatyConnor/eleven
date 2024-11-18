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
package com.hx.nine.eleven.datasources.config;

import com.hx.nine.eleven.bytebuddy.aop.PointcutMethodInterceptorFactory;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.datasources.annotation.HXDataSource;
import com.hx.nine.eleven.datasources.creator.DefaultDynamicDataSourceCreator;
import com.hx.nine.eleven.datasources.interceptor.DynamicDataSourceAnnotationInterceptor;
import com.hx.nine.eleven.datasources.properties.DynamicDataSourceProperties;
import com.hx.nine.eleven.datasources.properties.DynamicDatasourceAopProperties;
import com.hx.nine.eleven.datasources.strategy.DynamicDataSourceStrategy;
import com.hx.nine.eleven.datasources.HXDynamicRoutingDataSource;
import com.hx.nine.eleven.datasources.utils.DataSourcePermissionCheck;
import com.hx.nine.eleven.datasources.utils.HXLogger;
import com.hx.nine.eleven.commons.utils.ObjectUtils;
import com.hx.nine.eleven.core.annotations.Component;
import com.hx.nine.eleven.core.annotations.ConditionalOnBean;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源自动配置类
 *
 * @author wml
 * @see DynamicDataSourceStrategy
 * @see HXDynamicRoutingDataSource
 * @since 1.0.0
 */
@Slf4j
//@ConditionalOnProperty(prefix = "vertx.hx.datasource.dynamic", name = "enabled", havingValue = "true")
@Component(init = "initDataSource")
@ConditionalOnBean(value = {DynamicDataSourceProperties.class, DefaultDynamicDataSourceCreator.class})
public class DynamicDataSourceAutoConfiguration {

    /**
     * 创建数据源
     * @return
     */
    public void initDataSource() {
        HXLogger.build(this).info("开始初始化数据源");
        DynamicDataSourceProperties properties = DefaultElevenApplicationContext.build()
                .getProperties(DynamicDataSourceProperties.class);
        DefaultDynamicDataSourceCreator creator = DefaultElevenApplicationContext.build().getBean(DefaultDynamicDataSourceCreator.class);
        HXDynamicRoutingDataSource dataSource = new HXDynamicRoutingDataSource();
        Map<Object,DataSource> dataSourceMap = creator.createAllDataSource(properties);
        Object object = dataSourceMap.get(properties.getPrimary()); // 默认数据源
        if (ObjectUtils.isEmpty(object)){
            HXLogger.build(this).error("默认数据源不能为空，请设置默认数据源");
            throw new RuntimeException("默认数据源不能为空，请设置默认数据源");
        }
        dataSource.setTargetDataSources(dataSourceMap);    // 其他数据源
        dataSource.setDefaultTargetDataSource((DataSource)object);       // 默认数据源
        DataSourcePermissionCheck.setDefaultDataSource(properties.getPrimary()); // 默认数据源名称
        dataSource.setMasterDataSource(properties.getPrimary());
        DefaultElevenApplicationContext.build().addBean("dataSource",dataSource);
        // 添加 HXDataSource 切点
        HXLogger.build(this).info("初始化[HXDataSource]数据源拦截器");
        DynamicDatasourceAopProperties aopProperties = DefaultElevenApplicationContext.build()
                .getProperties().getProperty(DynamicDatasourceAopProperties.class);
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor(aopProperties.getAllowedPublicOnly());
        PointcutMethodInterceptorFactory.addAnnotation(HXDataSource.class,interceptor);
    }
}
