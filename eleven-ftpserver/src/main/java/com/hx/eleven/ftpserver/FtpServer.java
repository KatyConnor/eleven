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

import com.hx.eleven.ftpserver.ftplet.FtpException;


/**
 *
 * 创建一个ftp server 服务
 *
 * @author wml
 */
public interface FtpServer {

    /**
     * 启动 ftp server 服务， 并打开一个 listener thread 线程
     * @throws FtpException
     */
    void start() throws FtpException;
    
    /**
     * 停止 ftp server 服务， 停止 listener thread 线程
     */
    void stop();

    /**
     * 获取 ftp server 状态，
     * @return 关闭：true；启动：false
     */
    boolean isStopped();
    
    /**
     * 暂停新发起的请求
     */
    void suspend();
    
    /**
     * 恢复服务器处理程序
     */
    void resume();
    
    /**
     * 服务器是否挂起
     * @return 服务挂起：true 否则： false
     */
    boolean isSuspended();
    
}
