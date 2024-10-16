/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.hx.eleven.ftpserver.message.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.hx.eleven.ftpserver.util.IoUtils;
import com.hx.eleven.ftpserver.FtpServerConfigurationException;
import com.hx.eleven.ftpserver.message.MessageResource;
import com.hx.eleven.ftpserver.message.MessageResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>内部类，不直接使用</strong>
 * 
 * MessageResource实现，支持 i18n
 *
 */
public class DefaultMessageResource implements MessageResource {

    private final Logger LOG = LoggerFactory.getLogger(DefaultMessageResource.class);
    // message resource 地址，默认org/apache/ftpserver/message/
    private final static String RESOURCE_PATH = "org/apache/ftpserver/message/";

    private final List<String> languages;
    private final Map<String, PropertiesPair> messages;

    /**
     *  {@link MessageResourceFactory}
     */
    public DefaultMessageResource(List<String> languages, File customMessageDirectory) {
        if(languages != null) {
            this.languages = Collections.unmodifiableList(languages);
        } else {
            this.languages = null;
        }

        messages = new HashMap<>();
        if (languages != null) {
            for (String language : languages) {
                PropertiesPair pair = createPropertiesPair(language, customMessageDirectory);
                messages.put(language, pair);
            }
        }
        PropertiesPair pair = createPropertiesPair(null, customMessageDirectory);
        messages.put(null, pair);

    }

    private static class PropertiesPair {
        public Properties defaultProperties = new Properties();
        public Properties customProperties = new Properties();
    }

    /**
     *
     * 创建属性对对象。存储默认消息和自定义消息。
     */
    private PropertiesPair createPropertiesPair(String lang, File customMessageDirectory) {
        PropertiesPair pair = new PropertiesPair();
        String defaultResourceName;
        if (lang == null) {
            defaultResourceName = RESOURCE_PATH + "FtpStatus.properties";
        } else {
            defaultResourceName = RESOURCE_PATH + "FtpStatus_" + lang + ".properties";
        }
        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(defaultResourceName);
            if (in != null) {
                try {
                    pair.defaultProperties.load(in);
                } catch (IOException e) {
                    throw new FtpServerConfigurationException(
                            "Failed to load messages from \"" + defaultResourceName + "\", file not found in classpath");
                }
            } else {
                throw new FtpServerConfigurationException(
                        "Failed to load messages from \"" + defaultResourceName + "\", file not found in classpath");
            }
        } finally {
            IoUtils.close(in);
        }

        // 加载自定义资源
        File resourceFile = null;
        if (lang == null) {
            resourceFile = new File(customMessageDirectory, "FtpStatus.gen");
        } else {
            resourceFile = new File(customMessageDirectory, "FtpStatus_" + lang + ".gen");
        }
        in = null;
        try {
            if (resourceFile.exists()) {
                in = new FileInputStream(resourceFile);
                pair.customProperties.load(in);
            }
        } catch (Exception ex) {
            LOG.warn("MessageResourceImpl.createPropertiesPair()", ex);
            throw new FtpServerConfigurationException("MessageResourceImpl.createPropertiesPair()", ex);
        } finally {
            IoUtils.close(in);
        }
        return pair;
    }

    /**
     * 获取所有可用的message，返回一个不可修改的 list 集合
     */
    public List<String> getAvailableLanguages() {
        if (languages == null) {
            return null;
        } else {
            return Collections.unmodifiableList(languages);
        }
    }

    /**
     * 根据编码获取 message 定义的释义，先从自定义中查找，没找到再从默认中查找
     * @param code The reply code 编码
     * @param subId The sub ID
     * @param language The language 语言类型
     * @return
     */
    public String getMessage(int code, String subId, String language) {
        String key = String.valueOf(code);
        if (subId != null) {
            key = key + '.' + subId;
        }
        String value = null;
        PropertiesPair pair = null;
        if (language != null) {
            language = language.toLowerCase();
            pair = messages.get(language);
            if (pair != null) {
                value = pair.customProperties.getProperty(key);
                if (value == null) {
                    value = pair.defaultProperties.getProperty(key);
                }
            }
        }

        // 如果都没定义，查找是否有 key 为 null 的message
        if (value == null) {
            pair = messages.get(null);
            if (pair != null) {
                value = pair.customProperties.getProperty(key);
                if (value == null) {
                    value = pair.defaultProperties.getProperty(key);
                }
            }
        }
        return value;
    }

    /**
     * 获取所有 message 配置
     */
    public Map<String, String> getMessages(String language) {
        Properties messages = new Properties();
        PropertiesPair pair = this.messages.get(null);
        if (pair != null) {
            messages.putAll(pair.defaultProperties);
            messages.putAll(pair.customProperties);
        }
        if (language != null) {
            language = language.toLowerCase();
            pair = this.messages.get(language);
            if (pair != null) {
                messages.putAll(pair.defaultProperties);
                messages.putAll(pair.customProperties);
            }
        }
        
        Map<String, String> result = new HashMap<>();
        for(Object key : messages.keySet()) {
            result.put(key.toString(), messages.getProperty(key.toString()));
        }
        
        return Collections.unmodifiableMap(result);
    }

    /**
     * Dispose component - clear all maps.
     */
    public void dispose() {
        Iterator<String> it = messages.keySet().iterator();
        while (it.hasNext()) {
            String language = it.next();
            PropertiesPair pair = messages.get(language);
            pair.customProperties.clear();
            pair.defaultProperties.clear();
        }
        messages.clear();
    }
}
