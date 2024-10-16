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

/**
 *
 * ftplet容器使用的ftplet配置对象，用于在初始化期间向ftplet传递信息。配置信息中包含初始化参数。
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface FtpletContext {

    /**
     * 获取用户管理器
     * @return The {@link UserManager}
     */
    UserManager getUserManager();

    /**
     * 获取文件系统管理器
     * @return The {@link FileSystemFactory}
     */
    FileSystemFactory getFileSystemManager();

    /**
     * 获取ftp统计信息
     * @return The {@link FtpStatistics}
     */
    FtpStatistics getFtpStatistics();

    /**
     * 获取 Ftplet
     * @param name  名称 {@link Ftplet}
     * @return 以提供的名称注册，如果不存在则为空 {@link Ftplet}
     */
    Ftplet getFtplet(String name);
}
