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

package com.hx.eleven.ftpserver.request;

import com.hx.eleven.ftpserver.ftplet.AuthorizationRequest;

/**
 *
 * 表示多次并发登录的请求
 *
 * @author
 */
public class ConcurrentLoginRequest implements AuthorizationRequest {

    /**
     * 并发登录数
     */
    private final int concurrentLogins;
    /**
     * 该IP请求的并发登录数
     */
    private final int concurrentLoginsFromThisIP;
    /**
     * 最大并发登录数
     */
    private int maxConcurrentLogins = 0;
    /**
     * 该IP最大登录数
     */
    private int maxConcurrentLoginsPerIP = 0;

    public ConcurrentLoginRequest(int concurrentLogins, int concurrentLoginsFromThisIP) {
        super();
        this.concurrentLogins = concurrentLogins;
        this.concurrentLoginsFromThisIP = concurrentLoginsFromThisIP;
    }

    public int getConcurrentLogins() {
        return concurrentLogins;
    }

    public int getConcurrentLoginsFromThisIP() {
        return concurrentLoginsFromThisIP;
    }

    public int getMaxConcurrentLogins() {
        return maxConcurrentLogins;
    }

   public void setMaxConcurrentLogins(int maxConcurrentLogins) {
        this.maxConcurrentLogins = maxConcurrentLogins;
    }

    public int getMaxConcurrentLoginsPerIP() {
        return maxConcurrentLoginsPerIP;
    }

   public void setMaxConcurrentLoginsPerIP(int maxConcurrentLoginsPerIP) {
        this.maxConcurrentLoginsPerIP = maxConcurrentLoginsPerIP;
    }
}
