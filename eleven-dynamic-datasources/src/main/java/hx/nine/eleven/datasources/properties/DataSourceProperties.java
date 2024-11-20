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
package hx.nine.eleven.datasources.properties;

import hx.nine.eleven.datasources.enums.SeataMode;
import hx.nine.eleven.datasources.strategy.DynamicDataSourceStrategy;
import hx.nine.eleven.datasources.strategy.LoadBalanceDynamicDataSourceStrategy;
import hx.nine.eleven.datasources.utils.CryptoUtils;
import hx.nine.eleven.core.annotations.Component;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据源创建所需配置
 * @author wml
 */
@Data
@Accessors(chain = true)
@Component
public class DataSourceProperties {


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
     * JDBC url 地址
     */
    private String url;
    /**
     * JDBC 用户名
     */
    private String username;
    /**
     * JDBC 密码
     */
    private String password;
    /**
     * 解密公匙(如果未设置默认使用CryptoUtils.DEFAULT_PUBLIC_KEY_STRING)
     */
    private String publicKey = CryptoUtils.DEFAULT_PUBLIC_KEY_STRING;

    private DatasourceInitProperties datasourceInitProperties;

    /**
     * 多数据源选择算法clazz，默认负载均衡算法
     */
    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;

}
