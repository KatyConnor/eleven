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

import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.datasources.enums.SeataMode;
import hx.nine.eleven.datasources.event.DataSourceInitEvent;
import hx.nine.eleven.datasources.properties.DataSourceProperties;
import hx.nine.eleven.datasources.support.ScriptRunner;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.datasources.utils.HXLogger;
import hx.nine.eleven.jdbc.DataSourceProvider;
import com.p6spy.engine.spy.P6DataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据库连接池创建器
 * <p>
 * 主要处理一些公共逻辑，如脚本和事件等
 *
 * @author wml
 */
@Slf4j
public abstract class AbstractDataSourceCreator implements DataSourceCreator, DataSourceProvider {

    protected DataSourceInitEvent dataSourceInitEvent;
    protected DataSourceProperties dataSourceProperties;

    public DataSourceInitEvent getDataSourceInitEvent() {
        return dataSourceInitEvent;
    }

    public void setDataSourceInitEvent(DataSourceInitEvent dataSourceInitEvent) {
        this.dataSourceInitEvent = dataSourceInitEvent;
    }

    public DataSourceProperties getDataSourceProperties() {
        return dataSourceProperties;
    }

    public void setDataSourceProperties(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    /**
     *  通过属性创建数据源
     * @param dataSourceProperty 数据源属性
     * @return
     */
    @Override
    public <T> DataSource createDataSource(T dataSourceProperty) {
        Map<String, Object> propertiesMap =  BeanMapUtil.beanToMap(dataSourceProperty);
        dataSourceInitEvent.beforeCreate(propertiesMap);
//        JsonObject jsonObjectProperty = JsonObject.mapFrom(dataSourceProperty);
        DataSource dataSource = null;
        try {
            dataSource = getDataSource(propertiesMap);
        } catch (SQLException e) {
            // @TODO 创建数据源失败
            HXLogger.build(this).error("创建数据源失败！",e);
        }
        dataSourceInitEvent.afterCreate(dataSource);
        // 判断是否执行脚本
//        if (dataSourceProperties.getDatasourceInitProperties() !=null){
//            this.runScrip(dataSource,dataSourceProperties); // 数据源创建之后执行脚本
//        }

        return dataSource;
    }

    /**
     * sql 脚本执行
     * @param dataSource  数据源
     */
    private void runScrip(DataSource dataSource,DataSourceProperties dataSourceProperties) {
        String schema = dataSourceProperties.getDatasourceInitProperties().getSchema();
        String data = dataSourceProperties.getDatasourceInitProperties().getData();
        if (StringUtils.hasText(schema) || StringUtils.hasText(data)) {
            ScriptRunner scriptRunner = new ScriptRunner(dataSourceProperties.getDatasourceInitProperties().isContinueOnError(),
                    dataSourceProperties.getDatasourceInitProperties().getSeparator());
            if (StringUtils.hasText(schema)) {
                scriptRunner.runScript(dataSource, schema);
            }
            if (StringUtils.hasText(data)) {
                scriptRunner.runScript(dataSource, data);
            }
        }
    }

    /**
     *
     * @param dataSource
     * @return
     */
    @Deprecated
    private DataSource wrapDataSource(DataSource dataSource, Map<String, Object> propertiesMap) {
        String name = String.valueOf(propertiesMap.get("dataSourcepoolName"));
        DataSource targetDataSource = dataSource;

        Boolean enabledP6spy = dataSourceProperties.getP6spy();//propertiesMap.get("p6spy").toString()
        if (enabledP6spy) {
            targetDataSource = new P6DataSource(dataSource);
            log.debug("dynamic-datasource [{}] wrap p6spy plugin", name);
        }

        Boolean enabledSeata = dataSourceProperties.getSeata();//Boolean.valueOf(propertiesMap.get("seata").toString());
        SeataMode seataMode = dataSourceProperties.getSeataMode();//(SeataMode)propertiesMap.get("seataMode");
        if (enabledSeata) {
            if (SeataMode.XA == seataMode) {
                targetDataSource = new DataSourceProxyXA(targetDataSource);
            } else {
                targetDataSource = new DataSourceProxy(targetDataSource);
            }
            log.debug("dynamic-datasource [{}] wrap seata plugin transaction mode ", name);
        }
        //
        return targetDataSource;
    }


}
