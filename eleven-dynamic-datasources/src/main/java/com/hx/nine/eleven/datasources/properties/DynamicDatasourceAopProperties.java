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
package com.hx.nine.eleven.datasources.properties;

import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import com.hx.nine.eleven.datasources.Ordered;
import lombok.Data;

/**
 * 多数据源aop相关配置
 *
 * @author wml
 *
 */
@Data
@ConfigurationPropertiesBind(prefix = "vertx.hx.datasource.dynamic.aop")
public class DynamicDatasourceAopProperties {

    /**
     * 是否开启 @annotaions DataSource 注解， 默认开启 true
     */
    private Boolean enabled = true;
    /**
     * aop order
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
    /**
     * aop allowedPublicOnly
     */
    private Boolean allowedPublicOnly = true;
}
