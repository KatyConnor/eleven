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

package com.hx.eleven.ftpserver.context;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import com.hx.eleven.ftpserver.ConnectionConfig;
import com.hx.eleven.ftpserver.command.CommandFactory;
import com.hx.eleven.ftpserver.ftpletcontainer.FtpletContainer;
import com.hx.eleven.ftpserver.listener.Listener;
import com.hx.eleven.ftpserver.message.MessageResource;
import com.hx.eleven.ftpserver.ftplet.FtpletContext;

/**
 * <strong>内部类，不直接使用</strong>
 * 
 *  上下文传递对象
 */
public interface FtpServerContext extends FtpletContext {

    /**
     * 获取 ConnectionConfig
     */
    ConnectionConfig getConnectionConfig();

    /**
     * 获取 message resource.
     */
    MessageResource getMessageResource();

    /**
     * 获取 ftplet container.
     */
    FtpletContainer getFtpletContainer();

    /**
     * 获取 Listener 对象
     * @param name
     * @return
     */
    Listener getListener(String name);

    /**
     * 获取所有Listeners
     * @return
     */
    Map<String, Listener> getListeners();

    /**
     * 获取 command factory.
     */
    CommandFactory getCommandFactory();

    /**
     * 消费所有 components 组件
     */
    void dispose();
    
    /**
     *  上线文线程池执行对象 ThreadPoolExecutor
     * @return
     */
    ThreadPoolExecutor getThreadPoolExecutor();
}
