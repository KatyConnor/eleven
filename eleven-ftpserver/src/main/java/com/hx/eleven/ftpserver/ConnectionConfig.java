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

package com.hx.eleven.ftpserver;

/**
 *
 * connection 链接配置
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 *
 */
public interface ConnectionConfig {

    /**
     * 用户登录失败的最大次数
     * @return The maximum number of failure login attempts
     */
    int getMaxLoginFailures();

    /**
     *
     * 登录失败最大等待时间
     *
     * @return 毫秒单位
     */
    int getLoginFailureDelay();

    /**
     * 最大匿名登录数
     * @return  最大匿名登录数
     */
    int getMaxAnonymousLogins();

    /**
     * 最大并发登录用户数
     * @return  最大用户数
     */
    int getMaxLogins();

    /**
     *  是否允许匿名登录
     * @return
     */
    boolean isAnonymousLoginEnabled();

    /**
     *
     * 返回服务器允许为处理客户端请求创建的最大线程数
     *
     * @return 服务器允许为处理客户端请求创建的最大线程数
     */
    int getMaxThreads();
}
