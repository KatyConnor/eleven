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

package org.apache.ftpserver.ftplet;

/**
 *
 * 封装了ftplet方法的返回值
 * DEFAULT < NO_FTPLET < SKIP < DISCONNECT
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public enum FtpletResult {

    /**
     * 这个返回值表示将调用下一个ftplet方法。
     * 如果没有其他ftplet可用，ftpserver将处理请求。
     */
    DEFAULT,

    /**
     *
     * 此返回值表明不会调用其他ftplet方法，但ftpserver将继续处理此请求。
     *
     */
    NO_FTPLET,

    /**
     *
     *  它表示ftpserver将跳过所有内容。不会对该请求进行进一步处理(ftplet和服务器)。
     *
     */
    SKIP,

    /**
     *
     *  它表示服务器将跳过并断开与客户端的连接。不会为来自同一客户端的其他请求提供服务。
     *
     */
    DISCONNECT;
}
