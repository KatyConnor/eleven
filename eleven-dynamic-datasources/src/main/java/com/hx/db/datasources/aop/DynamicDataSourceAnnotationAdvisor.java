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
package com.hx.db.datasources.aop;

import com.hx.bytebuddy.aop.advice.AbstractPointcutAdvisor;
import com.hx.db.datasources.annotation.HXDataSource;
import com.hx.db.datasources.interceptor.DynamicDataSourceAnnotationInterceptor;
import com.hx.db.datasources.properties.DynamicDatasourceAopProperties;
import com.hx.db.datasources.utils.HXLogger;
import com.hx.vertx.boot.annotations.Component;
import com.hx.vertx.boot.core.context.DefaultVertxApplicationContext;

/**
 * 通过注解实现aop拦截
 * @author wml
 * @date 2022-10-28
 * @since 1.2.0
 */
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor {

    @Override
    public void init() {
        HXLogger.build(this).info("初始化数据源拦截器");
        DynamicDatasourceAopProperties aopProperties = DefaultVertxApplicationContext.build()
                .getProperties().getProperty(DynamicDatasourceAopProperties.class);
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor(aopProperties.getAllowedPublicOnly());
        this.addAnnotation(HXDataSource.class,interceptor);
    }
}
