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
package hx.nine.eleven.datasources.properties.druid;

import com.alibaba.druid.filter.Filter;
import hx.nine.eleven.datasources.utils.CryptoUtils;
import hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import java.util.*;
import static com.alibaba.druid.pool.DruidAbstractDataSource.*;

/**
 * Druid参数配置
 *
 * @author TaoYu
 * @since 1.2.0
 */
@Data
@Slf4j
@ConfigurationPropertiesBind(prefix = "eleven.boot.datasource.dynamic.druid")
public class DruidDataSourceProperties {

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

    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    private Integer maxWait;
    private Long timeBetweenEvictionRunsMillis;
    private Long timeBetweenLogStatsMillis;
    private Integer statSqlMaxSize;
    private Long minEvictableIdleTimeMillis;
    private Long maxEvictableIdleTimeMillis;
    private String defaultCatalog;
    private Boolean defaultAutoCommit;
    private Boolean defaultReadOnly;
    private Integer defaultTransactionIsolation;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private String validationQuery;
    private Integer validationQueryTimeout;
    private Boolean useGlobalDataSourceStat;
    private Boolean asyncInit;
    private String filters;
    private Boolean clearFiltersEnable;
    private Boolean resetStatEnable;
    private Integer notFullTimeoutRetryCount;
    private Integer maxWaitThreadCount;
    private Boolean failFast;
    private Long phyTimeoutMillis;
    private Boolean keepAlive;
    private Boolean poolPreparedStatements;
    private Boolean initVariants;
    private Boolean initGlobalVariants;
    private Boolean useUnfairLock;
    private Boolean killWhenSocketReadTimeout;
    private Properties connectionProperties;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String initConnectionSqls;
    private Boolean sharePreparedStatements;
    private Integer connectionErrorRetryAttempts;
    private Boolean breakAfterAcquireFailure;
    private Boolean removeAbandoned;
    private Integer removeAbandonedTimeoutMillis;
    private Boolean logAbandoned;
    private Integer queryTimeout;
    private Integer transactionQueryTimeout;
    private Integer connectTimeout; // millisecond
    private Integer socketTimeout; //millisecond

    private Map<String, Object> wall = new HashMap<>();
    private Map<String, Object> slf4j = new HashMap<>();
    private Map<String, Object> log4j = new HashMap<>();
    private Map<String, Object> log4j2 = new HashMap<>();
    private Map<String, Object> commonsLog = new HashMap<>();
    private Map<String, Object> stat = new HashMap<>();

    private List<Class<? extends Filter>> proxyFilters = new ArrayList<>();

    /**
     * 根据全局配置和本地配置结合转换为Properties
     *
     * @return Druid配置
     */
    public Properties toProperties() {
        Properties properties = new Properties();
        if (this.initialSize != null && !this.initialSize.equals(DEFAULT_INITIAL_SIZE)) {
            properties.setProperty(DruidConsts.INITIAL_SIZE, String.valueOf(this.initialSize));
        }
        if (this.maxActive != null && !this.maxActive.equals(DEFAULT_MAX_WAIT)) {
            properties.setProperty(DruidConsts.MAX_ACTIVE, String.valueOf(this.maxActive));
        }
        if (this.minIdle != null && !this.minIdle.equals(DEFAULT_MIN_IDLE)) {
            properties.setProperty(DruidConsts.MIN_IDLE, String.valueOf(this.minIdle));
        }
        if (this.maxWait != null && !this.maxWait.equals(DEFAULT_MAX_WAIT)) {
            properties.setProperty(DruidConsts.MAX_WAIT, String.valueOf(this.maxWait));
        }
        if (this.timeBetweenEvictionRunsMillis != null && !this.timeBetweenEvictionRunsMillis.equals(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS)) {
            properties.setProperty(DruidConsts.TIME_BETWEEN_EVICTION_RUNS_MILLIS, String.valueOf(this.timeBetweenEvictionRunsMillis));
        }
        if (this.timeBetweenLogStatsMillis != null && this.timeBetweenLogStatsMillis > 0) {
            properties.setProperty(DruidConsts.TIME_BETWEEN_LOG_STATS_MILLIS, String.valueOf(this.timeBetweenLogStatsMillis));
        }
        if (this.minEvictableIdleTimeMillis != null && !this.minEvictableIdleTimeMillis.equals(DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS)) {
            properties.setProperty(DruidConsts.MIN_EVICTABLE_IDLE_TIME_MILLIS, String.valueOf(this.minEvictableIdleTimeMillis));

        }
        if (this.maxEvictableIdleTimeMillis != null && !this.maxEvictableIdleTimeMillis.equals(DEFAULT_MAX_EVICTABLE_IDLE_TIME_MILLIS)) {
            properties.setProperty(DruidConsts.MAX_EVICTABLE_IDLE_TIME_MILLIS, String.valueOf(this.maxEvictableIdleTimeMillis));
        }
        if (this.testWhileIdle != null && !this.testWhileIdle.equals(DEFAULT_WHILE_IDLE)) {
            properties.setProperty(DruidConsts.TEST_WHILE_IDLE, Boolean.FALSE.toString());
        }
        if (this.testOnBorrow != null && !this.testOnBorrow.equals(DEFAULT_TEST_ON_BORROW)) {
            properties.setProperty(DruidConsts.TEST_ON_BORROW, Boolean.TRUE.toString());
        }
        if (this.validationQuery != null && this.validationQuery.length() > 0) {
            properties.setProperty(DruidConsts.VALIDATION_QUERY, this.validationQuery);
        }
        if (this.useGlobalDataSourceStat != null && this.useGlobalDataSourceStat.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.USE_GLOBAL_DATA_SOURCE_STAT, Boolean.TRUE.toString());
        }
        if (this.asyncInit != null && this.asyncInit.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.ASYNC_INIT, Boolean.TRUE.toString());
        }

        //filters单独处理，默认了stat
        if (this.filters == null) {
            filters = DruidConsts.STAT_STR;
        }
        if (this.publicKey != null && this.publicKey.length() > 0 && !filters.contains(DruidConsts.CONFIG_STR)) {
            filters += "," + DruidConsts.CONFIG_STR;
        }
        properties.setProperty(DruidConsts.FILTERS, filters);
        if ( this.clearFiltersEnable != null &&  this.clearFiltersEnable.equals(Boolean.FALSE)) {
            properties.setProperty(DruidConsts.CLEAR_FILTERS_ENABLE, Boolean.FALSE.toString());
        }

        if (this.resetStatEnable != null && this.resetStatEnable.equals(Boolean.FALSE)) {
            properties.setProperty(DruidConsts.RESET_STAT_ENABLE, Boolean.FALSE.toString());
        }
        if (this.notFullTimeoutRetryCount != null && !this.notFullTimeoutRetryCount.equals(0)) {
            properties.setProperty(DruidConsts.NOT_FULL_TIMEOUT_RETRY_COUNT, String.valueOf(this.notFullTimeoutRetryCount));
        }

        if (this.maxWaitThreadCount != null && !this.maxWaitThreadCount.equals(-1)) {
            properties.setProperty(DruidConsts.MAX_WAIT_THREAD_COUNT, String.valueOf(this.maxWaitThreadCount));
        }

        if ( this.failFast != null &&  this.failFast.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.FAIL_FAST, Boolean.TRUE.toString());
        }
        if (this.phyTimeoutMillis != null && !this.phyTimeoutMillis.equals(DEFAULT_PHY_TIMEOUT_MILLIS)) {
            properties.setProperty(DruidConsts.PHY_TIMEOUT_MILLIS, String.valueOf(this.phyTimeoutMillis));
        }

        if (this.keepAlive != null && this.keepAlive.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.KEEP_ALIVE, Boolean.TRUE.toString());
        }

        if (this.poolPreparedStatements != null && this.poolPreparedStatements.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.POOL_PREPARED_STATEMENTS, Boolean.TRUE.toString());
        }

        if (this.initVariants != null && this.initVariants.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.INIT_VARIANTS, Boolean.TRUE.toString());
        }

        if (this.initGlobalVariants != null && this.initGlobalVariants.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.INIT_GLOBAL_VARIANTS, Boolean.TRUE.toString());
        }

        if (useUnfairLock != null) {
            properties.setProperty(DruidConsts.USE_UNFAIR_LOCK, String.valueOf(this.useUnfairLock));
        }

        if (this.killWhenSocketReadTimeout != null && this.killWhenSocketReadTimeout.equals(Boolean.TRUE)) {
            properties.setProperty(DruidConsts.KILL_WHEN_SOCKET_READ_TIMEOUT, Boolean.TRUE.toString());
        }

        if (this.publicKey != null && this.publicKey.length() > 0) {
            if ( this.connectionProperties == null) {
                this.connectionProperties = new Properties();
            }
            this.connectionProperties.setProperty("config.decrypt", Boolean.TRUE.toString());
            this.connectionProperties.setProperty("config.decrypt.key", this.publicKey);
        }

        if (this.maxPoolPreparedStatementPerConnectionSize != null && !this.maxPoolPreparedStatementPerConnectionSize.equals(10)) {
            properties.setProperty(DruidConsts.MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE, String.valueOf(this.maxPoolPreparedStatementPerConnectionSize));
        }

        if (this.initConnectionSqls != null && this.initConnectionSqls.length() > 0) {
            properties.setProperty(DruidConsts.INIT_CONNECTION_SQLS, this.initConnectionSqls);
        }
        return properties;
    }

    public List<Class<? extends Filter>> getProxyFilters() {
        return proxyFilters;
    }

    public void setProxyFilters(List<Class<? extends Filter>> proxyFilters) {
        this.proxyFilters = proxyFilters;
    }
}