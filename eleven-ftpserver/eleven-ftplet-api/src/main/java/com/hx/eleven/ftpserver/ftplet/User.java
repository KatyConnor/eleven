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

package com.hx.eleven.ftpserver.ftplet;

import java.util.List;

/**
 * 用户基本操作接口
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface User {

    /**
     * 获取用户名
     * @return 返回用于登录的用户名
     */
    String getName();

    /**
     * 获取密码
     * @return 返回用户密码
     */
    String getPassword();

    /** 
     * 获取用户授权的所有权限
     * 
     * @return 返回所有权限
     */
    List<? extends Authority> getAuthorities();

    /**
     * 获取授予此用户的指定类型的权限
     * @param clazz 指定权限 {@link Authority}
     * @return 指定类的权限
     */
    List<? extends Authority> getAuthorities(Class<? extends Authority> clazz);

    /**
     * 为用户授权 {@link AuthorizationRequest}
     *
     * @param request 需要授权的request {@link AuthorizationRequest}
     *
     * @return  果用户已被授权，则填充AuthorizationRequest，否则为空。
     */
    AuthorizationRequest authorize(AuthorizationRequest request);

    /**
     * 获取以秒为单位的最大空闲时间。零或更少的空闲时间意味着没有限制。
     * @return 以秒为单位的空闲时间
     */
    int getMaxIdleTime();

    /**
     * 获取用户启用状态。
     * @return 用户启用：true、未启用：false
     */
    boolean getEnabled();

    /**
     * 获取用户主目录
     * @return 返回用户主目录路径地址
     */
    String getHomeDirectory();
}
