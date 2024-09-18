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
package com.hx.db.datasources.event;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 多数据源连接池创建事件
 *
 * @author wml
 * @since 3.5.0
 */
public interface DataSourceInitEvent {

    /**
     * 数据源连接池创建前执行（可用于参数解密）
     *
     * @param propertiesMap 数据源基础信息
     */
    void beforeCreate(Map<String, Object> propertiesMap);

    /**
     * 数据源连接池创建后执行
     *
     * @param dataSource 数据源
     */
    void afterCreate(DataSource dataSource);
}
