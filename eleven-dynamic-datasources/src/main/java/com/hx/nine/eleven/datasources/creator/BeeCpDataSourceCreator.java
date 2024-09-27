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

import cn.beecp.BeeDataSource;
import cn.beecp.BeeDataSourceConfig;
import com.hx.nine.eleven.datasources.properties.beecp.BeeCpDataSourceProperties;
import com.hx.nine.eleven.datasources.support.DdConstants;
import com.hx.nine.eleven.datasources.utils.ConfigMergeCreator;
import com.hx.lang.commons.utils.BeanMapUtil;
import com.hx.nine.eleven.commons.utils.StringUtils;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;

/**
 * BeeCp数据源创建器
 *
 * @author wml
 * @since 2020/5/14
 */
@Slf4j
public class BeeCpDataSourceCreator extends AbstractDataSourceCreator {

    private static final ConfigMergeCreator<BeeCpDataSourceProperties, BeeDataSourceConfig> MERGE_CREATOR = new ConfigMergeCreator<>("BeeCp", BeeCpDataSourceProperties.class, BeeDataSourceConfig.class);

    private static Method copyToMethod = null;

    static {
        try {
            copyToMethod = BeeDataSourceConfig.class.getDeclaredMethod("copyTo", BeeDataSourceConfig.class);
            copyToMethod.setAccessible(true);
        } catch (NoSuchMethodException ignored) {
        }
    }

    @Override
    public String support() {
        return DdConstants.BEECP;
    }

    @Override
    public int maximumPoolSize(DataSource dataSource, JsonObject jsonObject) throws SQLException {
        return 0;
    }

    @Override
    public DataSource getDataSource(JsonObject jsonObject) throws SQLException {
        BeeCpDataSourceProperties properties = jsonObject.mapTo(BeeCpDataSourceProperties.class);
        Map<String, Object> propertiesMap = BeanMapUtil.beanToMap(properties);
        BeeDataSourceConfig config = BeanMapUtil.mapToBean(propertiesMap, BeeDataSourceConfig.class);
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        config.setJdbcUrl(properties.getUrl());
        config.setPoolName(properties.getDataSourcePoolName());
        String driverClassName = properties.getDriverClassName();
        if (!StringUtils.isEmpty(driverClassName)) {
            config.setDriverClassName(driverClassName);
        }
        if (Boolean.FALSE.equals(properties.getLazy())) {
            return new BeeDataSource(config);
        }
        BeeDataSource beeDataSource = new BeeDataSource();
        try {
            copyToMethod.invoke(config, beeDataSource);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return beeDataSource;
//        HXLogger.build(this).warn("数据源类型不匹配，需要创建 BeeDataSource 的 config 参数");
    }

    @Override
    public void close(DataSource dataSource) throws SQLException {

    }
}
