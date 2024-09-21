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
package com.hx.nine.eleven.datasources.creator;

import com.hx.nine.eleven.datasources.properties.hikari.HikariCpDataSourceProperties;
import com.hx.nine.eleven.datasources.support.DdConstants;
import com.hx.nine.eleven.datasources.utils.HXLogger;
import com.hx.lang.commons.utils.BeanMapUtil;
import com.hx.lang.commons.utils.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.json.JsonObject;
import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;

/**
 * Hikari数据源创建器
 *
 * @author wml
 * @since 2020/1/21
 */
public class HikariDataSourceCreator extends AbstractDataSourceCreator{

    private static Method configCopyMethod = null;

    static {
        fetchMethod();
    }


    /**
     * to support springboot 1.5 and 2.x
     * HikariConfig 2.x use 'copyState' to copy config
     * HikariConfig 3.x use 'copyStateTo' to copy config
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    private static void fetchMethod() {
        try {
            configCopyMethod = HikariConfig.class.getMethod("copyState", HikariConfig.class);
            return;
        } catch (NoSuchMethodException ignored) {
            HXLogger.build(HikariDataSourceCreator.class).warn("HikariConfig does not has 'copyState' method");
        }

        if (configCopyMethod == null) {
            try {
                configCopyMethod = HikariConfig.class.getMethod("copyStateTo", HikariConfig.class);
                return;
            } catch (NoSuchMethodException ignored) {
                HXLogger.build(HikariDataSourceCreator.class).warn("HikariConfig does not has 'copyStateTo' method!");
            }
        }

        throw new RuntimeException("HikariConfig does not has 'copyState' or 'copyStateTo' method!");
    }

    /**
     * 数据库类型支持验证
     *
     * @return
     */
    @Override
    public String support() {
        return DdConstants.HIKARI;
    }

    @Override
    public int maximumPoolSize(DataSource dataSource, JsonObject jsonObject) throws SQLException {
        HikariCpDataSourceProperties properties = jsonObject.mapTo(HikariCpDataSourceProperties.class);
        return properties.getMaximumPoolSize() == null?0:properties.getMaximumPoolSize();
    }

    @Override
    public DataSource getDataSource(JsonObject jsonObject) throws SQLException {
        HikariCpDataSourceProperties properties = jsonObject.mapTo(HikariCpDataSourceProperties.class);
        Map<String, Object> propertiesMap = BeanMapUtil.beanToMap(properties);
        HikariConfig config = BeanMapUtil.mapToBean(propertiesMap, HikariConfig.class);
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setJdbcUrl(properties.getUrl());
        config.setPoolName(properties.getDataSourcePoolName());
        String driverClassName = properties.getDriverClassName();
        if (!StringUtils.isEmpty(driverClassName)) {
            config.setDriverClassName(driverClassName);
        }
        if (Boolean.FALSE.equals(properties.getLazy())) {
            return new HikariDataSource(config);
        }
        config.validate();
        HikariDataSource dataSource = new HikariDataSource();
        try {
            configCopyMethod.invoke(config, dataSource);
        } catch (IllegalAccessException | InvocationTargetException e) {
            HXLogger.build(this).warn("HikariConfig failed to copy to HikariDataSource");
            throw new RuntimeException("HikariConfig failed to copy to HikariDataSource", e);
        }
        return dataSource;
    }

    /**
     * 关闭数据源
     * @param dataSource
     * @throws SQLException
     */
    @Override
    public void close(DataSource dataSource) throws SQLException {
        ((HikariDataSource)dataSource).close();
    }
}
