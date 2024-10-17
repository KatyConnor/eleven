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

import java.net.InetAddress;
import java.util.Date;

/**
 *
 * ftp 服务统计信息
 * @author wml
 */
public interface FtpStatistics {

    /**
     *  服务器启动时间
     * @return
     */
    Date getStartTime();

    /**
     * 获取上传的文件数
     * @return
     */
    int getTotalUploadNumber();

    /**
     * 获取下载的文件数
     * @return
     */
    int getTotalDownloadNumber();

    /**
     * 获取已删除的文件数
     * @return
     */
    int getTotalDeleteNumber();

    /**
     *  获取上传的总字节数
     * @return
     */
    long getTotalUploadSize();

    /**
     * 获取下载的总字节数
     * @return
     */
    long getTotalDownloadSize();

    /**
     * 获取创建的总目录
     * @return
     */
    int getTotalDirectoryCreated();

    /**
     * 获取删除的总目录
     * @return
     */
    int getTotalDirectoryRemoved();

    /**
     * 获取总连接数
     * @return
     */
    int getTotalConnectionNumber();

    /**
     * 获取当前连接数
     * @return
     */
    int getCurrentConnectionNumber();

    /**
     * 获取总登录号
     * @return
     */
    int getTotalLoginNumber();

    /**
     * 获取失败的登录总数
     * @return
     */
    int getTotalFailedLoginNumber();

    /**
     * 获取当前登录号
     * @return
     */
    int getCurrentLoginNumber();

    /**
     * 获取总匿名登录号
     * @return
     */
    int getTotalAnonymousLoginNumber();

    /**
     * 获取当前的匿名登录号
     * @return
     */
    int getCurrentAnonymousLoginNumber();

    /**
     * 获取特定用户的登录号
     * @param user
     * @return
     */
    int getCurrentUserLoginNumber(User user);

    /**
     *
     * 从ipAddress中获取特定用户的登录号
     * @param user 登录用户帐号
     * @param ipAddress  远程用户的IP地址
     *
     * @return 所提供的用户和IP地址的登录总数
     */
    int getCurrentUserLoginNumber(User user, InetAddress ipAddress);
}
