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

import com.hx.nine.eleven.datasources.properties.DynamicDataSourceProperties;
import com.hx.nine.eleven.datasources.properties.beecp.BeeCpDataSourceProperties;
import com.hx.nine.eleven.datasources.properties.dbcp2.Dbcp2DataSourceProperties;
import com.hx.nine.eleven.datasources.properties.druid.DruidDataSourceProperties;
import com.hx.nine.eleven.datasources.properties.hikari.HikariCpDataSourceProperties;
import com.hx.nine.eleven.datasources.support.DdConstants;
import com.hx.nine.eleven.datasources.utils.DataSourcePermissionCheck;
import com.hx.nine.eleven.datasources.utils.HXLogger;
import com.hx.nine.eleven.commons.utils.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统筹调度创建系统配置所有数据源
 *
 * @author wml
 * @date 2023-01-22
 * @since 2.3.0
 */
@Slf4j
@Setter
public class DefaultDynamicDataSourceCreator {

    private Map<String, DataSourceCreator> creatorMap;

    Map<Object, DataSource> dataSourceMap = new HashMap<>();

    public DefaultDynamicDataSourceCreator(Map<String, DataSourceCreator> creatorMap) {
        this.creatorMap = creatorMap;
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty
     * @return
     */
    public Map<Object, DataSource> createAllDataSource(DynamicDataSourceProperties dataSourceProperty) {
        HXLogger.build(this).info("开始创建数据源");
        List<HikariCpDataSourceProperties> hikariList = dataSourceProperty.getHikari();
        if (hikariList != null && hikariList.size() > 0) {
            createHikariDataSource(hikariList);
        }
        List<DruidDataSourceProperties> druidList = dataSourceProperty.getDruid();
        if (druidList != null && druidList.size() > 0) {
            createDruidDataSource(druidList);
        }

        List<BeeCpDataSourceProperties> beecpList = dataSourceProperty.getBeecp();
        if (beecpList != null && beecpList.size() > 0) {
            createBeecpDataSource(beecpList);
        }

        List<Dbcp2DataSourceProperties> dbcp2List = dataSourceProperty.getDbcp2();
        if (dbcp2List != null && dbcp2List.size() > 0) {
            createDbcp2DataSource(dbcp2List);
        }
        HXLogger.build(this).info("一共创建数据源 [{}] 个,{}", this.dataSourceMap.size(), this.dataSourceMap.size() <= 0 ? "请检查数据源配置" : "数据源加载完成");
        return this.dataSourceMap;
    }

    /**
     * 创建 HikariCp 数据源
     *
     * @param hikariList
     */
    private void createHikariDataSource(List<HikariCpDataSourceProperties> hikariList) {
        AtomicInteger i = new AtomicInteger();
        StringBuilder name = new StringBuilder(DdConstants.HIKARI);
        name.append("_");
        hikariList.forEach(obj -> {
            DataSource dataSource = createDataSource(obj, DdConstants.HIKARI);
            String dataSourceName = obj.getDataSourcePoolName();
            if (this.dataSourceMap.containsKey(dataSourceName)) {
                HXLogger.build(this).warn("[{dataSourceName}] 数据源名称重复,忽略本次创建", dataSourceName);
            } else {
                this.dataSourceMap.put(StringUtils.isBlank(dataSourceName) ? name.append(i.incrementAndGet()).toString() : dataSourceName, dataSource);
            }
            DataSourcePermissionCheck.put(obj.getDataSourcePoolName(),obj.getDbReadOnly());
            DataSourcePermissionCheck.addPermission(obj.getDataSourcePoolName(),obj.getPermission());
            name.deleteCharAt(name.length() -1);
        });

    }

    /**
     * 创建 druid 数据源
     *
     * @param druidList
     */
    private void createDruidDataSource(List<DruidDataSourceProperties> druidList) {
        AtomicInteger i = new AtomicInteger();
        StringBuilder name = new StringBuilder(DdConstants.DRUID);
        name.append("_");
        druidList.forEach(obj -> {
            DataSource dataSource = createDataSource(obj, DdConstants.DRUID);
            String dataSourceName = obj.getDataSourcePoolName();
            if (this.dataSourceMap.containsKey(dataSourceName)) {
                HXLogger.build(this).warn("[{dataSourceName}] 数据源名称重复,忽略本次创建", dataSourceName);
            } else {
                this.dataSourceMap.put(StringUtils.isBlank(dataSourceName) ? name.append(i.incrementAndGet()).toString() : dataSourceName, dataSource);
            }
            DataSourcePermissionCheck.put(obj.getDataSourcePoolName(),obj.getDbReadOnly());
            DataSourcePermissionCheck.addPermission(obj.getDataSourcePoolName(),obj.getPermission());
            name.deleteCharAt(name.length() -1);
        });
    }

    /**
     * 创建 beecp 数据源
     *
     * @param beecpList
     */
    private void createBeecpDataSource(List<BeeCpDataSourceProperties> beecpList) {
        AtomicInteger i = new AtomicInteger();
        StringBuilder name = new StringBuilder(DdConstants.BEECP);
        name.append("_");
        beecpList.forEach(obj -> {
            DataSource dataSource = createDataSource(obj, DdConstants.BEECP);
            String dataSourceName = obj.getDataSourcePoolName();
            if (this.dataSourceMap.containsKey(dataSourceName)) {
                HXLogger.build(this).warn("[{dataSourceName}] 数据源名称重复,忽略本次创建", dataSourceName);
            } else {
                this.dataSourceMap.put(StringUtils.isBlank(dataSourceName) ? name.append(i.incrementAndGet()).toString() : dataSourceName, dataSource);
            }
            DataSourcePermissionCheck.put(obj.getDataSourcePoolName(),obj.getDbReadOnly());
            DataSourcePermissionCheck.addPermission(obj.getDataSourcePoolName(),obj.getPermission());
            name.deleteCharAt(name.length() -1);
        });
    }

    /**
     * 创建 dbcp2 数据源
     *
     * @param dbcp2List
     */
    private void createDbcp2DataSource(List<Dbcp2DataSourceProperties> dbcp2List) {
        AtomicInteger i = new AtomicInteger();
        StringBuilder name = new StringBuilder(DdConstants.DBCP2);
        name.append("_");
        dbcp2List.forEach(obj -> {
            DataSource dataSource = createDataSource(obj, DdConstants.DBCP2);
            String dataSourceName = obj.getDataSourcePoolName();
            if (this.dataSourceMap.containsKey(dataSourceName)) {
                HXLogger.build(this).warn("[{dataSourceName}] 数据源名称重复,忽略本次创建", dataSourceName);
            } else {
                this.dataSourceMap.put(StringUtils.isBlank(dataSourceName) ? name.append(i.incrementAndGet()).toString() : dataSourceName, dataSource);
            }
            DataSourcePermissionCheck.put(obj.getDataSourcePoolName(),obj.getDbReadOnly());
            DataSourcePermissionCheck.addPermission(obj.getDataSourcePoolName(),obj.getPermission());
            name.deleteCharAt(name.length() -1);
        });
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty
     * @param <T>
     * @return
     */
    private <T> DataSource createDataSource(T dataSourceProperty, String supportType) {
        DataSourceCreator creator = this.creatorMap.get(supportType);
        if (creator == null) {
            throw new IllegalStateException("creator must not be null,please check the DataSourceCreator");
        }
        return creator.createDataSource(dataSourceProperty);
    }

}
