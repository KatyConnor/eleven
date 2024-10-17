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
import java.util.List;
import java.util.Set;

import com.hx.eleven.ftpserver.session.FtpIoSession;
import com.hx.eleven.ftpserver.ssl.SslConfiguration;
import com.hx.eleven.ftpserver.DataConnectionConfiguration;
import com.hx.eleven.ftpserver.context.FtpServerContext;
import com.hx.eleven.ftpserver.ipfilter.SessionFilter;
import org.apache.mina.filter.firewall.Subnet;

/**
 *
 * Listener 监听
 * @author
 */
public interface Listener {

    /**
     * 启动 Listener
     * @param serverContext
     */
    void start(FtpServerContext serverContext);

    /**
     * 停止 Listener
     * .
     */
    void stop();

    /**
     * 检查当前 Listener 事后关闭
     * @return
     */
    boolean isStopped();

    /**
     *
     *  暂停接受请求，使用{@link # Resume()}方法恢复监听器
     *
     */
    void suspend();

    /**
     * 恢复挂起的监听器
     */
    void resume();

    /**
     *
     * 检查 Listener 侦听器当前是否挂起
     * @return
     */
    boolean isSuspended();

    /**
     *
     * 返回此侦听器的当前活动会话，如果没有活动会话，则返回空{@link Set}。
     * @return The currently active sessions
     */
    Set<FtpIoSession> getActiveSessions();

    /**
     *
     * 这个监听器是自动处于SSL模式还是客户端必须显式请求使用SSL
     * @return
     */
    boolean isImplicitSsl();

    /**
     * 获取 {@link SslConfiguration}使用
     *
     * @return The current {@link SslConfiguration}
     */
    SslConfiguration getSslConfiguration();

    /**
     *
     * 获取端口
     * @return The port
     */
    int getPort();

    /**
     *
     * 服务器绑定的地址
     * @return The local socket {@link InetAddress}, if set
     */
    String getServerAddress();

    /**
     *
     *  获取在此侦听器中进行的数据连接的配置
     * @return The data connection configuration
     */
    DataConnectionConfiguration getDataConnectionConfiguration();

    /**
     *
     * 超时时间
     * @return The idle time out
     */
    int getIdleTimeout();

    /**
     * @deprecated 获取
     *
     * 
     * @return
     */
    @Deprecated
    List<InetAddress> getBlockedAddresses();

    /**
     * @deprecated
     * 
     * @return
     */
    @Deprecated
    List<Subnet> getBlockedSubnets();

    /**
     *
     * 
     * @return
     */
    SessionFilter getSessionFilter();
}