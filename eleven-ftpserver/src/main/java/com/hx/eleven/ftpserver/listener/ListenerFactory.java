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

package com.hx.eleven.ftpserver.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.hx.eleven.ftpserver.DataConnectionConfiguration;
import com.hx.eleven.ftpserver.DataConnectionConfigurationFactory;
import com.hx.eleven.ftpserver.FtpServerConfigurationException;
import com.hx.eleven.ftpserver.listener.nio.NioListener;
import com.hx.eleven.ftpserver.ssl.SslConfiguration;
import com.hx.eleven.ftpserver.ipfilter.SessionFilter;

/**
 *
 * 创建 Listener 工厂类
 *
 * @author wml
 */
@Deprecated
public class ListenerFactory {

    private String serverAddress;
    private int port = 21;
    private boolean implicitSsl = false;
    private SslConfiguration ssl;
    private DataConnectionConfiguration dataConnectionConfig = new DataConnectionConfigurationFactory().createDataConnectionConfiguration();
    private int idleTimeout = 300;

    /**
     * The Session filter
     */
    private SessionFilter sessionFilter = null;

    public ListenerFactory() {

    }

    public ListenerFactory(Listener listener) {
        serverAddress = listener.getServerAddress();
        port = listener.getPort();
        ssl = listener.getSslConfiguration();
        implicitSsl = listener.isImplicitSsl();
        dataConnectionConfig = listener.getDataConnectionConfiguration();
        idleTimeout = listener.getIdleTimeout();
        this.sessionFilter = listener.getSessionFilter();
    }

    /**
     * 创建一个 Listener
     * @return The created listener
     */
    public Listener createListener() {
        try {
            InetAddress.getByName(serverAddress);
        } catch (UnknownHostException e) {
            throw new FtpServerConfigurationException("Unknown host", e);
        }

        if (sessionFilter == null) {
            throw new IllegalStateException("sessionFilter can not null ");
        }
            return new NioListener(serverAddress, port, implicitSsl, ssl,
                    dataConnectionConfig, idleTimeout, sessionFilter);
    }
}