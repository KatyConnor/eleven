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

package com.hx.eleven.ftpserver.auth;

import com.hx.eleven.ftpserver.ftplet.Authority;
import com.hx.eleven.ftpserver.ftplet.AuthorizationRequest;
import com.hx.eleven.ftpserver.request.WriteRequest;

/**
 *
 * @author
 */
public class WritePermission implements Authority {

    private String permissionRoot;

    public WritePermission() {
        this.permissionRoot = "/";
    }

    public WritePermission(final String permissionRoot) {
        this.permissionRoot = permissionRoot;
    }

    /**
     * 用户鉴权
     * @param request 授权请求 {@link AuthorizationRequest}
     * @return
     */
    public AuthorizationRequest authorize(final AuthorizationRequest request) {
        if (request instanceof WriteRequest) {
            WriteRequest writeRequest = (WriteRequest) request;
            String requestFile = writeRequest.getFile();
            if (requestFile.startsWith(permissionRoot)) {
                return writeRequest;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     *  鉴别
     * @param request 授权请求
     *
     * @return
     */
    public boolean canAuthorize(final AuthorizationRequest request) {
        return request instanceof WriteRequest;
    }

}
