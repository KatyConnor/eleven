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

import com.hx.nine.eleven.datasources.FieldListToObjectConvert;
import com.hx.nine.eleven.datasources.enums.SeataMode;
import com.hx.nine.eleven.datasources.properties.beecp.BeeCpDataSourceProperties;
import com.hx.nine.eleven.datasources.properties.dbcp2.Dbcp2DataSourceProperties;
import com.hx.nine.eleven.datasources.properties.druid.DruidDataSourceProperties;
import com.hx.nine.eleven.datasources.properties.hikari.HikariCpDataSourceProperties;
import com.hx.nine.eleven.datasources.strategy.DynamicDataSourceStrategy;
import com.hx.nine.eleven.datasources.strategy.LoadBalanceDynamicDataSourceStrategy;
import com.hx.nine.eleven.datasources.utils.CryptoUtils;
import com.hx.lang.commons.annotation.FieldTypeConvert;
import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import com.hx.nine.eleven.core.annotations.NestedConfigurationProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;


/**
 * 动态数据源参数配置
 *
 * @author wml
 * @see DataSourceProperties
 * @since 1.0.0
 */
@Slf4j
@Getter
@Setter
@ConfigurationPropertiesBind(prefix = "vertx.hx.datasource.dynamic")
public class DynamicDataSourceProperties{


    /**
     * 必须设置默认的库,默认master
     */
    private String primary = "master";
    /**
     * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
     */
    private String dataSourcePoolName;
    /**
     * 是否启用严格模式,默认不启动. 严格模式下未匹配到数据源直接报错, 非严格模式下则使用默认数据源primary所设置的数据源
     */
    private Boolean strict = false;
    /**
     * 是否使用p6spy输出，默认不输出
     */
    private Boolean p6spy = false;
    /**
     * 是否使用开启seata，默认不开启
     */
    private Boolean seata = false;
    /**
     * seata使用模式，默认AT
     */
    private SeataMode seataMode = SeataMode.AT;
    /**
     * 解密公匙(如果未设置默认使用CryptoUtils.DEFAULT_PUBLIC_KEY_STRING)
     */
    private String publicKey = CryptoUtils.DEFAULT_PUBLIC_KEY_STRING;

    /**
     * 全局拦截器注册配置
     */
    private Class interceptorRegister;

    /**
     * 是否开启读写权限检查
     */
    private Boolean enableReadOnly = false;
    /**
     * 是否开启version、update_time字段检查
     */
    private Boolean enableUpdateCheck = false;

    private DatasourceInitProperties datasourceInitProperties;

    /**
     * 多数据源选择算法clazz，默认负载均衡算法
     */
    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;

    /**
     * aop with default ds annotation
     */
    @NestedConfigurationProperty(type = DynamicDatasourceAopProperties.class)
    private  DynamicDatasourceAopProperties aop;
    /**
     * Druid全局参数配置
     */
    @NestedConfigurationProperty(type = DruidDataSourceProperties.class)
    private  List<DruidDataSourceProperties> druid;
    /**
     * HikariCp全局参数配置
     */
    @NestedConfigurationProperty(type = HikariCpDataSourceProperties.class)
    @FieldTypeConvert(using = FieldListToObjectConvert.class)
    private List<HikariCpDataSourceProperties> hikari;
    /**
     * BeeCp全局参数配置
     */
    @NestedConfigurationProperty(type = BeeCpDataSourceProperties.class)
    private  List<BeeCpDataSourceProperties> beecp;
    /**
     * DBCP2全局参数配置
     */
    @NestedConfigurationProperty(type = Dbcp2DataSourceProperties.class)
    private  List<Dbcp2DataSourceProperties> dbcp2;
}
