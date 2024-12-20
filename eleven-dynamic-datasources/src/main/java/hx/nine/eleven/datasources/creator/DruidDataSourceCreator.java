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
package hx.nine.eleven.datasources.creator;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.datasources.exception.ErrorCreateDataSourceException;
import hx.nine.eleven.datasources.properties.druid.DruidDataSourceProperties;
import hx.nine.eleven.datasources.properties.druid.DruidLogConfigUtil;
import hx.nine.eleven.datasources.properties.druid.DruidStatConfigUtil;
import hx.nine.eleven.datasources.properties.druid.DruidWallConfigUtil;
import hx.nine.eleven.datasources.support.DdConstants;
import hx.nine.eleven.datasources.utils.HXLogger;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Druid数据源创建器
 *
 * @author wml
 * @since 2020/1/21
 */
@Slf4j
public class DruidDataSourceCreator extends AbstractDataSourceCreator {

    @Override
    public String support() {
        return DdConstants.DRUID;
    }

    @Override
    public int maximumPoolSize(DataSource dataSource,  Map<String,Object> config) throws SQLException {
        return 0;
    }

    @Override
    public DataSource getDataSource( Map<String,Object> config) throws SQLException {
        DruidDataSourceProperties dataSourceProperties = BeanMapUtil.mapToBean(config,DruidDataSourceProperties.class);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setName(dataSourceProperties.getDataSourcePoolName());
        String driverClassName = dataSourceProperties.getDriverClassName();
        if (!StringUtils.isEmpty(driverClassName)) {
            dataSource.setDriverClassName(driverClassName);
        }

        Properties properties = dataSourceProperties.toProperties();
        List<Filter> proxyFilters = this.initFilters(properties.getProperty("druid.filters"),dataSourceProperties);
        dataSource.setProxyFilters(proxyFilters);

        dataSource.configFromPropety(properties);
        //连接参数单独设置
        dataSource.setConnectProperties(dataSourceProperties.getConnectionProperties());
        //设置druid内置properties不支持的的参数
        this.setParam(dataSource,dataSourceProperties);

        if (Boolean.FALSE.equals(dataSourceProperties.getLazy())) {
            try {
                dataSource.init();
            } catch (SQLException e) {
                throw new ErrorCreateDataSourceException("druid create error", e);
            }
        }
        return dataSource;
    }

    @Override
    public void close(DataSource dataSource) throws SQLException {

    }

    private List<Filter> initFilters(String filters,DruidDataSourceProperties properties) {
        List<Filter> proxyFilters = new ArrayList<>(2);
        if (!StringUtils.isEmpty(filters)) {
            String[] filterItems = filters.split(",");
            for (String filter : filterItems) {
                switch (filter) {
                    case "stat":
                        proxyFilters.add(DruidStatConfigUtil.toStatFilter(properties.getStat()));
                        break;
                    case "wall":
                        WallConfig wallConfig = DruidWallConfigUtil.toWallConfig(properties.getWall());
                        WallFilter wallFilter = new WallFilter();
                        wallFilter.setConfig(wallConfig);
                        proxyFilters.add(wallFilter);
                        break;
                    case "slf4j":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Slf4jLogFilter.class, properties.getSlf4j()));
                        break;
                    case "commons-log":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(CommonsLogFilter.class, properties.getCommonsLog()));
                        break;
                    case "log4j":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Log4jFilter.class, properties.getLog4j()));
                        break;
                    case "log4j2":
                        proxyFilters.add(DruidLogConfigUtil.initFilter(Log4j2Filter.class, properties.getLog4j2()));
                        break;
                    default:
                        HXLogger.build(this).warn("dynamic-datasource current not support [{}]", filter);
                }
            }
        }
        for (Class<? extends Filter> filterId : properties.getProxyFilters()) {
            proxyFilters.add(DefaultElevenApplicationContext.build().getBean(filterId));
        }
        return proxyFilters;
    }

    private void setParam(DruidDataSource dataSource,DruidDataSourceProperties properties) {
        String defaultCatalog = properties.getDefaultCatalog();
        if (defaultCatalog != null) {
            dataSource.setDefaultCatalog(defaultCatalog);
        }
        Boolean defaultAutoCommit = properties.getDefaultAutoCommit();
        if (defaultAutoCommit != null && !defaultAutoCommit) {
            dataSource.setDefaultAutoCommit(false);
        }
        Boolean defaultReadOnly = properties.getDefaultReadOnly();
        if (defaultReadOnly != null) {
            dataSource.setDefaultReadOnly(defaultReadOnly);
        }
        Integer defaultTransactionIsolation = properties.getDefaultTransactionIsolation();
        if (defaultTransactionIsolation != null) {
            dataSource.setDefaultTransactionIsolation(defaultTransactionIsolation);
        }

        Boolean testOnReturn = properties.getTestOnReturn();
        if (testOnReturn != null && testOnReturn) {
            dataSource.setTestOnReturn(true);
        }
        Integer validationQueryTimeout = properties.getValidationQueryTimeout();
        if (validationQueryTimeout != null && !validationQueryTimeout.equals(-1)) {
            dataSource.setValidationQueryTimeout(validationQueryTimeout);
        }

        Boolean sharePreparedStatements = properties.getSharePreparedStatements();
        if (sharePreparedStatements != null && sharePreparedStatements) {
            dataSource.setSharePreparedStatements(true);
        }
        Integer connectionErrorRetryAttempts = properties.getConnectionErrorRetryAttempts();
        if (connectionErrorRetryAttempts != null && !connectionErrorRetryAttempts.equals(1)) {
            dataSource.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        }
        Boolean breakAfterAcquireFailure = properties.getBreakAfterAcquireFailure();
        if (breakAfterAcquireFailure != null && breakAfterAcquireFailure) {
            dataSource.setBreakAfterAcquireFailure(true);
        }

        Integer timeout = properties.getRemoveAbandonedTimeoutMillis();
        if (timeout != null) {
            dataSource.setRemoveAbandonedTimeoutMillis(timeout);
        }

        Boolean abandoned = properties.getRemoveAbandoned();
        if (abandoned != null) {
            dataSource.setRemoveAbandoned(abandoned);
        }

        Boolean logAbandoned = properties.getLogAbandoned();
        if (logAbandoned != null) {
            dataSource.setLogAbandoned(logAbandoned);
        }

        Integer queryTimeOut = properties.getQueryTimeout();
        if (queryTimeOut != null) {
            dataSource.setQueryTimeout(queryTimeOut);
        }

        Integer transactionQueryTimeout = properties.getTransactionQueryTimeout();
        if (transactionQueryTimeout != null) {
            dataSource.setTransactionQueryTimeout(transactionQueryTimeout);
        }

        // since druid 1.2.12
        Integer connectTimeout = properties.getConnectTimeout();
        if (connectTimeout != null) {
            try {
                DruidDataSource.class.getMethod("setConnectTimeout", int.class);
                dataSource.setConnectTimeout(connectTimeout);
            } catch (NoSuchMethodException e) {
                log.warn("druid current not support connectTimeout,please update druid 1.2.12 +");
            }
        }

        Integer socketTimeout = properties.getSocketTimeout();
        if (connectTimeout != null) {
            try {
                DruidDataSource.class.getMethod("setSocketTimeout", int.class);
                dataSource.setSocketTimeout(socketTimeout);
            } catch (NoSuchMethodException e) {
                log.warn("druid current not support setSocketTimeout,please update druid 1.2.12 +");
            }
        }
    }
}
