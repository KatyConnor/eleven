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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据传输连接对象
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface DataConnection {

    /**
     * 客户端传输的数据
     * @param session 当前会话session {@link FtpSession}
     * @param out  客户端传输的数据流 {@link OutputStream}
     * @return 传输数据长度
     * @throws IOException
     */
    long transferFromClient(FtpSession session, OutputStream out)
            throws IOException;

    /**
     * 传输数据到客户端
     * @param session 当前会话session {@link FtpSession}
     * @param in 传输到客户端的数据流
     * @return 传输数据长度
     * @throws IOException
     */
    long transferToClient(FtpSession session, InputStream in)
            throws IOException;

    /**
     * 将字符串传送到客户端，例如在LIST期间
     * @param session 当前会话session {@link FtpSession}
     * @param str 要传输的字符串
     * @throws IOException
     */
    void transferToClient(FtpSession session, String str) throws IOException;

}