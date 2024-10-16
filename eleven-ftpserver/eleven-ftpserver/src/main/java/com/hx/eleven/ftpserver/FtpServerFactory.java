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

package com.hx.eleven.ftpserver;

import java.util.Map;

import com.hx.eleven.ftpserver.listener.Listener;
import com.hx.eleven.ftpserver.command.CommandFactory;
import com.hx.eleven.ftpserver.ftplet.FileSystemFactory;
import com.hx.eleven.ftpserver.ftplet.Ftplet;
import com.hx.eleven.ftpserver.ftplet.UserManager;
import com.hx.eleven.ftpserver.ftpletcontainer.impl.DefaultFtpletContainer;
import com.hx.eleven.ftpserver.impl.DefaultFtpServer;
import com.hx.eleven.ftpserver.context.DefaultFtpServerContext;
import com.hx.eleven.ftpserver.message.MessageResource;

/**
 * 根据提供的配置创建服务器实例
 * @author wml
 */
@Deprecated
public class FtpServerFactory {

    private DefaultFtpServerContext serverContext;

    public FtpServerFactory() {
        serverContext = new DefaultFtpServerContext();
    }

    /**
     * 创建一个 FtpServer
     * @return
     */
    public FtpServer createServer() {
        return new DefaultFtpServer(serverContext);
    }

    /**
     * 获取所有 Listener
     * @return
     */
    public Map<String, Listener> getListeners() {
        return serverContext.getListeners();
    }

    /**
     * 根据 listener 名称获取
     * 
     * @param name
     * @return
     */
    public Listener getListener(final String name) {
        return serverContext.getListener(name);
    }

    /**
     * 指定 listener 名称添加listener
     * @param name     名称
     * @param listener
     */
    public void addListener(final String name, final Listener listener) {
        serverContext.addListener(name, listener);
    }

    /**
     *
     * @param listeners
     */
    public void setListeners(final Map<String, Listener> listeners) {
        serverContext.setListeners(listeners);
    }

    /**
     *
     * @return
     */
    public Map<String, Ftplet> getFtplets() {
        return serverContext.getFtpletContainer().getFtplets();
    }

    /**
     *
     * @param ftplets
     */
    public void setFtplets(final Map<String, Ftplet> ftplets) {
        serverContext.setFtpletContainer(new DefaultFtpletContainer(ftplets));
    }

    /**
     *
     * 获取 UserManager 用户管理对象
     * @return
     */
    public UserManager getUserManager() {
        return serverContext.getUserManager();
    }

    /**
     * 获取 UserManager 用户管理对象
     * 
     * @param userManager The {@link UserManager}
     * @throws IllegalStateException
     */
    public void setUserManager(final UserManager userManager) {
        serverContext.setUserManager(userManager);
    }

    /**
     *
     * @return
     */
    public FileSystemFactory getFileSystem() {
        return serverContext.getFileSystemManager();
    }

    /**
     *
     * @param fileSystem
     */
    public void setFileSystem(final FileSystemFactory fileSystem) {
        serverContext.setFileSystemManager(fileSystem);
    }

    /**
     *
     * @return
     */
    public CommandFactory getCommandFactory() {
        return serverContext.getCommandFactory();
    }

    /**
     *
     * @param commandFactory
     */
    public void setCommandFactory(final CommandFactory commandFactory) {
        serverContext.setCommandFactory(commandFactory);
    }

    /**
     * 获取 message 消息资源
     * @return The {@link MessageResource}
     */
    public MessageResource getMessageResource() {
        return serverContext.getMessageResource();
    }

    /**
     *  设置message 消息资源
     * @param messageResource  The {@link MessageResource}
     * @throws IllegalStateException
     */
    public void setMessageResource(final MessageResource messageResource) {
        serverContext.setMessageResource(messageResource);
    }

    /**
     *
     * @return
     */
    public ConnectionConfig getConnectionConfig() {
        return serverContext.getConnectionConfig();
    }

    /**
     *
     * @param connectionConfig
     */
    public void setConnectionConfig(final ConnectionConfig connectionConfig) {
        serverContext.setConnectionConfig(connectionConfig);
    }
}
