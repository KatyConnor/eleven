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

/**
 * 授予用户权限的接口，入用户登录授权
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface Authority {

    /**
     * 是否可以授权某个请求
     *
     * @param request 授权请求
     *
     * @return 授权： true; false
     */
    boolean canAuthorize(AuthorizationRequest request);

    /**
     * 授权操作 {@link AuthorizationRequest}.
     * 
     * @param request 授权请求 {@link AuthorizationRequest}
     * @return 返回授权后的授权请求
     */
    AuthorizationRequest authorize(AuthorizationRequest request);
}
