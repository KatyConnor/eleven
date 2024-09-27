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
package com.hx.nine.eleven.datasources.config;

import com.hx.nine.eleven.datasources.event.DataSourceInitEvent;
import com.hx.nine.eleven.datasources.properties.DataSourceProperties;
import com.hx.nine.eleven.datasources.utils.ClassUtils;
import com.hx.nine.eleven.datasources.utils.HXLogger;
import com.hx.nine.eleven.datasources.creator.*;
import com.hx.nine.eleven.core.annotations.Component;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源创建器creator自动装配
 * @author wml
 * @since 1.5.0
 */
@Component(init = "dataSourceCreator")
public class DynamicDataSourceCreatorAutoConfiguration {

    // 数据源创建顺序
    private final List<DataSourceCreator> dataSourceCreators;

    @Resource
    protected DataSourceInitEvent dataSourceInitEvent;
    @Resource
    protected DataSourceProperties dataSourceProperties;

    public DynamicDataSourceCreatorAutoConfiguration(){
        dataSourceCreators = new ArrayList<>();
    }

    /**
     * 数据源创建者
     * @return
     */
    public void dataSourceCreator() {
        hikariDataSourceCreator();
        druidDataSourceCreator();
        beeCpDataSourceCreator();
        dbcp2DataSourceCreator();

        // 创建creator
        HXLogger.build(this).info("-------init DefaultDataSourceCreator, data source creator size = {} -------",dataSourceCreators.size());
        Map<String,DataSourceCreator> creatorMap = new HashMap<>(dataSourceCreators.size());
        dataSourceCreators.forEach(obj ->{
            creatorMap.put(obj.support(),obj);
        });
        DefaultDynamicDataSourceCreator defaultDynamicDataSourceCreator = new DefaultDynamicDataSourceCreator(creatorMap);
        DefaultElevenApplicationContext.build().addBean(defaultDynamicDataSourceCreator);
    }

    /**
     * 存在Druid数据源时, 加入创建器
     */
    public void druidDataSourceCreator() {
        if (ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource")){
            HXLogger.build(this).info("-------init DruidDataSourceCreator-------");
            DruidDataSourceCreator druidDataSourceCreator = new DruidDataSourceCreator();
            druidDataSourceCreator.setDataSourceInitEvent(dataSourceInitEvent);
            druidDataSourceCreator.setDataSourceProperties(dataSourceProperties);
            dataSourceCreators.add(druidDataSourceCreator);
        }
    }

    /**
     * 存在Hikari数据源时, 加入创建器
     */
    public void hikariDataSourceCreator() {
        if (ClassUtils.isPresent("com.zaxxer.hikari.HikariDataSource")){
            HXLogger.build(this).info("-------init HikariDataSourceCreator-------");
            HikariDataSourceCreator hikariDataSourceCreator = new HikariDataSourceCreator();
            hikariDataSourceCreator.setDataSourceInitEvent(dataSourceInitEvent);
            hikariDataSourceCreator.setDataSourceProperties(dataSourceProperties);
            dataSourceCreators.add(hikariDataSourceCreator);
        }
    }

    /**
     * 存在BeeCp数据源时, 加入创建器
     */
    public void beeCpDataSourceCreator() {
        if (ClassUtils.isPresent("cn.beecp.BeeDataSource")){
            HXLogger.build(this).info("-------init BeeCpDataSourceCreator-------");
            BeeCpDataSourceCreator beeCpDataSourceCreator = new BeeCpDataSourceCreator();
            beeCpDataSourceCreator.setDataSourceInitEvent(dataSourceInitEvent);
            beeCpDataSourceCreator.setDataSourceProperties(dataSourceProperties);
            dataSourceCreators.add(beeCpDataSourceCreator);
        }
    }

    /**
     * 存在Dbcp2数据源时, 加入创建器
     */
    public void dbcp2DataSourceCreator() {
        if (ClassUtils.isPresent("org.apache.commons.dbcp2.BasicDataSource")) {
            HXLogger.build(this).info("-------init Dbcp2DataSourceCreator-------");
            Dbcp2DataSourceCreator dbcp2DataSourceCreator = new Dbcp2DataSourceCreator();
            dbcp2DataSourceCreator.setDataSourceInitEvent(dataSourceInitEvent);
            dbcp2DataSourceCreator.setDataSourceProperties(dataSourceProperties);
            dataSourceCreators.add(dbcp2DataSourceCreator);
        }
    }
}
