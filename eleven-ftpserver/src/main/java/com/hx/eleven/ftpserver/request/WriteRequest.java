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
 * 写入请求
 *
 * @author
 */
public class WriteRequest implements AuthorizationRequest {

    private String file;

    public WriteRequest() {
        // 默认根地址
        this("/");
    }

    /**
     * 指定文件地址
     * @param file  文件地址
     */
    public WriteRequest(final String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

}
