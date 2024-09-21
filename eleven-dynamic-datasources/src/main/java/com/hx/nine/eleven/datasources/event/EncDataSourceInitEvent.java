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
package com.hx.nine.eleven.datasources.event;

import com.hx.nine.eleven.datasources.utils.CryptoUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.nine.eleven.core.annotations.Component;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多数据源创建，默认加解密事件
 *
 * @author wml
 */
@Slf4j
@Component
public class EncDataSourceInitEvent implements DataSourceInitEvent {

    /**
     * 加密正则
     */
    private static final Pattern ENC_PATTERN = Pattern.compile("^ENC\\((.*)\\)$");

    /**
     * 数据源连接池创建前对URL连接地址，用户名，密码进行解密
     * @param propertiesMap 数据源基础信息
     */
    @Override
    public void beforeCreate(Map<String, Object> propertiesMap) {
        String publicKey = String.valueOf(propertiesMap.get("publicKey"));//dataSourceProperty.getPublicKey();
        if (StringUtils.hasText(publicKey)) {
            String url = String.valueOf(propertiesMap.get("url"));
            String username = String.valueOf(propertiesMap.get("username"));
            String password = String.valueOf(propertiesMap.get("password"));
            propertiesMap.put("url",decrypt(publicKey, url));
            propertiesMap.put("username",decrypt(publicKey, username));
            propertiesMap.put("password",decrypt(publicKey, password));
        }
    }

    @Override
    public void afterCreate(DataSource dataSource) {

    }

    /**
     * 字符串解密
     */
    private String decrypt(String publicKey, String cipherText) {
        if (StringUtils.hasText(cipherText)) {
            Matcher matcher = ENC_PATTERN.matcher(cipherText);
            if (matcher.find()) {
                try {
                    return CryptoUtils.decrypt(publicKey, matcher.group(1));
                } catch (Exception e) {
                    log.error("DynamicDataSourceProperties.decrypt error ", e);
                }
            }
        }
        return cipherText;
    }
}
