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

package com.hx.eleven.ftpserver.listener.nio;

import java.net.InetAddress;
import java.util.List;

import com.hx.eleven.ftpserver.ipfilter.IpFilterType;
import com.hx.eleven.ftpserver.listener.Listener;
import com.hx.eleven.ftpserver.ssl.SslConfiguration;
import com.hx.eleven.ftpserver.DataConnectionConfiguration;
import com.hx.eleven.ftpserver.ipfilter.RemoteIpFilter;
import com.hx.eleven.ftpserver.ipfilter.SessionFilter;
import org.apache.mina.filter.firewall.Subnet;

/**
 *
 * AbstractListener 抽象类，定义Listener一些基本属性
 *
 */
public abstract class AbstractListener implements Listener {

    /**
     * 服务绑定地址
     */
    private final String serverAddress;
    /**
     * 端口号
     */
    private int port = 21;
    /**
     *  是否启用ssl协议
     */
    private final boolean implicitSsl;

    /**
     * 等待超时时间,单位：秒
     */
    private final int idleTimeout ;

    private final SslConfiguration ssl;

    private final List<InetAddress> blockedAddresses;

    private final List<Subnet> blockedSubnets;

    private final SessionFilter sessionFilter;

    private final DataConnectionConfiguration dataConnectionConfig;

    public AbstractListener(String serverAddress, int port,
            boolean implicitSsl, SslConfiguration sslConfiguration,
            DataConnectionConfiguration dataConnectionConfig, int idleTimeout,
            SessionFilter sessionFilter) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.implicitSsl = implicitSsl;
        this.dataConnectionConfig = dataConnectionConfig;
        this.ssl = sslConfiguration;
        this.idleTimeout = idleTimeout;
        this.sessionFilter = sessionFilter;
        this.blockedAddresses = null;
        this.blockedSubnets = null;
    }
    
    /**
     *
     * 根据 blockedAddresses黑名单地址（或子网列）地址创建一个SessionFilter，将给定的IP地址和/入黑名单。
     * 
     * @param blockedAddresses 黑名单IP地址
     * @param blockedSubnets  子网黑名单地址
     * @return
     */
    private static SessionFilter createBlackListFilter(List<InetAddress> blockedAddresses, List<Subnet> blockedSubnets) {
        if (blockedAddresses == null && blockedSubnets == null) {
            return null;
        }
        // 初始化一个黑名单filter拦截器
        RemoteIpFilter ipFilter = new RemoteIpFilter(IpFilterType.DENY);
        if (blockedSubnets != null) {
            ipFilter.addAll(blockedSubnets);
        }
        if (blockedAddresses != null) {
            for (InetAddress address : blockedAddresses) {
                ipFilter.add(new Subnet(address, 32));
            }
        }
        return ipFilter;
    }

    public boolean isImplicitSsl() {
        return this.implicitSsl;
    }

    public int getPort() {
        return this.port;
    }

    protected void setPort(int port) {
        this.port = port;
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public SslConfiguration getSslConfiguration() {
        return this.ssl;
    }

    public DataConnectionConfiguration getDataConnectionConfiguration() {
        return this.dataConnectionConfig;
    }

    public int getIdleTimeout() {
        return this.idleTimeout;
    }

    public List<InetAddress> getBlockedAddresses() {
        return this.blockedAddresses;
    }

    public List<Subnet> getBlockedSubnets() {
        return this.blockedSubnets;
    }

    public SessionFilter getSessionFilter() {
        return this.sessionFilter;
    }
}
