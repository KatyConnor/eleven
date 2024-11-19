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
package com.hx.nine.eleven.datasources.properties.hikari;

import com.hx.nine.eleven.datasources.utils.CryptoUtils;
import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * HikariCp参数配置
 *
 * @author wml
 * @since 2.4.1
 */
@Data
@Slf4j
@ConfigurationPropertiesBind(prefix = "eleven.boot.datasource.dynamic.hikari")
public class HikariCpDataSourceProperties{

    /**
     * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
     */
    private String dataSourcePoolName;
    /**
     * 连接池类型，如果不设置自动查找 Druid > HikariCp
     */
    private Class<? extends DataSource> type;
    /**
     * JDBC driver
     */
    private String driverClassName;
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
    /**
     * 是否懒加载数据源
     */
    private Boolean lazy = false;
    /**
     * 数据库是否只读权限，默认为false
     */
    private Boolean dbReadOnly = false;
    /**
     * 数据源操作权限，优先级dbReadOnly->permission
     */
    private String[] permission;

    private String catalog;
    private Long connectionTimeout;
    private Long validationTimeout;
    private Long idleTimeout;
    private Long leakDetectionThreshold;
    private Long maxLifetime;
    private Integer maxPoolSize;
    private Integer maximumPoolSize;
    private Integer minIdle;
    private Integer minimumIdle;

    private Long initializationFailTimeout;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassName;
    private String dataSourceJndiName;
    private String transactionIsolationName;
    private Boolean isAutoCommit;
    private Boolean isReadOnly;
    private Boolean isIsolateInternalQueries;
    private Boolean isRegisterMbeans;
    private Boolean isAllowPoolSuspension;
    private Properties dataSourceProperties;
    private Properties healthCheckProperties;

    /**
     * 高版本才有
     */
    private String schema;
    private String exceptionOverrideClassName;
    private Long keepaliveTime;
    private Boolean sealed;

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maxPoolSize = maximumPoolSize;
    }

    public void setMinimumIdle(Integer minimumIdle) {
        this.minIdle = minimumIdle;
    }

    public static void main(String[] args) {
        System.out.println(TimeUnit.SECONDS.toMillis(30L));
    }
}
