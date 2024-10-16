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

package com.hx.eleven.ftpserver.impl;

import com.hx.eleven.ftpserver.ConnectionConfig;

/**
 * <strong> 默认配置 </strong>
 *
 */
public class DefaultConnectionConfig implements ConnectionConfig {

    /**
     * 最大并发登录用户数
     */
    private final int maxLogins;

    /**
     * 是否允许匿名登录，默认不允许匿名登录
     */
    private final boolean anonymousLoginEnabled;

    /**
     *  最大匿名登录数
     */
    private final int maxAnonymousLogins;

    /**
     * 用户登录失败的最大允许次数
     */
    private final int maxLoginFailures;

    /**
     * 登录失败最大等待时间
     */
    private final int loginFailureDelay;

    /**
     * 最大线程数
     */
    private final int maxThreads;

    public DefaultConnectionConfig() {
        this(false, 500, 20, 10, 3, 0);
    }

    public DefaultConnectionConfig(boolean anonymousLoginEnabled,
            int loginFailureDelay, int maxLogins, int maxAnonymousLogins,
            int maxLoginFailures, int maxThreads) {
        this.anonymousLoginEnabled = anonymousLoginEnabled;
        this.loginFailureDelay = loginFailureDelay;
        this.maxLogins = maxLogins;
        this.maxAnonymousLogins = maxAnonymousLogins;
        this.maxLoginFailures = maxLoginFailures;
        this.maxThreads = maxThreads;
    }

    public int getLoginFailureDelay() {
        return loginFailureDelay;
    }

    public int getMaxAnonymousLogins() {
        return maxAnonymousLogins;
    }

    public int getMaxLoginFailures() {
        return maxLoginFailures;
    }

    public int getMaxLogins() {
        return maxLogins;
    }

    public boolean isAnonymousLoginEnabled() {
        return anonymousLoginEnabled;
    }
    
    public int getMaxThreads() {
        return maxThreads;
    }
    
}
