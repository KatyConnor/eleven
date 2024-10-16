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

package com.hx.eleven.ftpserver.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hx.eleven.ftpserver.ConnectionConfig;
import com.hx.eleven.ftpserver.FtpServer;
import com.hx.eleven.ftpserver.command.CommandFactory;
import com.hx.eleven.ftpserver.listener.Listener;
import com.hx.eleven.ftpserver.message.MessageResource;
import com.hx.eleven.ftpserver.context.FtpServerContext;
import com.hx.eleven.ftpserver.ftplet.FileSystemFactory;
import com.hx.eleven.ftpserver.ftplet.FtpException;
import com.hx.eleven.ftpserver.ftplet.Ftplet;
import com.hx.eleven.ftpserver.ftplet.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * ftp server 服务实现
 *
 * @author wml
 */
public class DefaultFtpServer implements FtpServer {

    private final Logger LOG = LoggerFactory.getLogger(DefaultFtpServer.class);

    // ftp server 上下文
    private FtpServerContext serverContext;
    // 服务挂起
    private boolean suspended = false;
    // 服务启动
    private boolean started = false;

    public DefaultFtpServer(final FtpServerContext serverContext) {
        this.serverContext = serverContext;
    }

    /**
     * 启动 ftp server 服务。打开一个新的 listener thread 线程
     * @throws FtpException
     */
    @Override
    public void start() throws FtpException {
        if (serverContext == null) {
            throw new IllegalStateException("FtpServer has been stopped. Restart is not supported");
        }

        // 启动 Listener thread 线程
        List<Listener> startedListeners = new ArrayList<>();
        try {
            Map<String, Listener> listeners = serverContext.getListeners();
            for (Listener listener : listeners.values()) {
                listener.start(serverContext);
                startedListeners.add(listener);
            }
    
            // 初始化Ftplet容器
            serverContext.getFtpletContainer().init(serverContext);
            started = true;
            LOG.info("FTP server started");
        } catch(Exception e) {
            for(Listener listener : startedListeners) {
                listener.stop();
            }
            if(e instanceof FtpException) {
                throw (FtpException)e;
            } else {
                throw (RuntimeException)e;
            }
        }
    }

    @Override
    public void stop() {
        if (serverContext == null) {
            return;
        }

        // 停止所有 listeners
        Map<String, Listener> listeners = serverContext.getListeners();
        for (Listener listener : listeners.values()) {
            listener.stop();
        }

        // 销毁 Ftplet 容器
        serverContext.getFtpletContainer().destroy();

        // 释放serverContext资源
        if (serverContext != null) {
            serverContext.dispose();
            serverContext = null;
        }
        started = false;
    }

    @Override
    public boolean isStopped() {
        return !started;
    }

    @Override
    public void suspend() {
        if (!started) {
            return;
        }

        LOG.debug("Suspending server");
        // 停止所有 all listeners
        Map<String, Listener> listeners = serverContext.getListeners();
        for (Listener listener : listeners.values()) {
            listener.suspend();
        }

        suspended = true;
        LOG.debug("Server suspended");
    }

    @Override
    public void resume() {
        if (!suspended) {
            return;
        }

        LOG.debug("Resuming server");
        Map<String, Listener> listeners = serverContext.getListeners();
        for (Listener listener : listeners.values()) {
            listener.resume();
        }

        suspended = false;
        LOG.debug("Server resumed");
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    public FtpServerContext getServerContext() {
        return this.serverContext;
    }

    /**
     * 获取所有 Listener
     * @return
     */
    public Map<String, Listener> getListeners() {
        return this.serverContext.getListeners();
    }

    /**
     * 根据 listener 名称获取 listener
     * @param name listener名称
     * @return
     */
    public Listener getListener(final String name) {
        return this.serverContext.getListener(name);
    }

    /**
     * 获取所有的Ftplets
     * @return
     */
    public Map<String, Ftplet> getFtplets() {
        return this.serverContext.getFtpletContainer().getFtplets();
    }

    /**
     *
     * 获取用户 userManager 对象
     * @return
     */
    public UserManager getUserManager() {
        return this.serverContext.getUserManager();
    }

    /**
     *
     * @return
     */
    public FileSystemFactory getFileSystem() {
        return this.serverContext.getFileSystemManager();
    }

    /**
     *
     * @return
     */
    public CommandFactory getCommandFactory() {
        return this.serverContext.getCommandFactory();
    }

    /**
     *
     * @return
     */
    public MessageResource getMessageResource() {
        return this.serverContext.getMessageResource();
    }

    /**
     *
     * @return
     */
    public ConnectionConfig getConnectionConfig() {
        return this.serverContext.getConnectionConfig();
    }
}
