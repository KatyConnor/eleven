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
import hx.nine.eleven.datasources.properties.dbcp2.Dbcp2DataSourceProperties;
import hx.nine.eleven.datasources.support.DdConstants;
import hx.nine.eleven.commons.utils.StringUtils;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * DBCP数据源创建器
 *
 * @author wml
 * @since 2021/5/18
 */
public class Dbcp2DataSourceCreator extends AbstractDataSourceCreator{

//    private static final ConfigMergeCreator<Dbcp2DataSourceProperties, BasicDataSource> MERGE_CREATOR = new ConfigMergeCreator<>("Dbcp2", Dbcp2DataSourceProperties.class, BasicDataSource.class);

    @Override
    public String support() {
        return DdConstants.DBCP2;
    }

    @Override
    public int maximumPoolSize(DataSource dataSource, Map<String,Object> config) throws SQLException {
        return 0;
    }

    @Override
    public DataSource getDataSource(Map<String,Object> config) throws SQLException {
        Dbcp2DataSourceProperties dbcp2DataSourceProperties =  BeanMapUtil.mapToBean(config,Dbcp2DataSourceProperties.class);
        BasicDataSource dataSource = BeanMapUtil.mapToBean(config, BasicDataSource.class);
        dataSource.setUsername(dbcp2DataSourceProperties.getUsername());
        dataSource.setPassword(dbcp2DataSourceProperties.getPassword());
        dataSource.setUrl(dbcp2DataSourceProperties.getUrl());
        String driverClassName = dbcp2DataSourceProperties.getDriverClassName();
        if (!StringUtils.isEmpty(driverClassName)) {
            dataSource.setDriverClassName(driverClassName);
        }
        if (Boolean.FALSE.equals(dbcp2DataSourceProperties.getLazy())) {
            dataSource.start();
        }
        return dataSource;
    }

    @Override
    public void close(DataSource dataSource) throws SQLException {

    }
}
