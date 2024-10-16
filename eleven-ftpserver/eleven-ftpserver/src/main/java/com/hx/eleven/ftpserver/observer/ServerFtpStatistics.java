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

package com.hx.eleven.ftpserver.observer;

import com.hx.eleven.ftpserver.ftplet.FtpFile;
import com.hx.eleven.ftpserver.ftplet.FtpStatistics;
import com.hx.eleven.ftpserver.session.FtpIoSession;

/**
 *
 * 添加观察者和设置其功能
 *
 * @author wml
 */
public interface ServerFtpStatistics extends FtpStatistics {

    /**
     * 设置观察者
     * @param observer
     */
    void setObserver(StatisticsObserver observer);

    /**
     * 设置文件观察者
     * @param observer
     */
    void setFileObserver(FileObserver observer);

    /**
     * 增量上传计数
     * @param session
     * @param file
     * @param size
     */
    void setUpload(FtpIoSession session, FtpFile file, long size);

    /**
     * 增量下载计数
     * @param session
     * @param file
     * @param size
     */
    void setDownload(FtpIoSession session, FtpFile file, long size);

    /**
     * 新增目录计数
     * @param session
     * @param dir
     */
    void setMkdir(FtpIoSession session, FtpFile dir);

    /**
     * 删减目录计数
     * @param session
     * @param dir
     */
    void setRmdir(FtpIoSession session, FtpFile dir);

    /**
     * 删除计数
     * @param session
     * @param file
     */
    void setDelete(FtpIoSession session, FtpFile file);

    /**
     * 增加当前连接数
     * @param session
     */
    void setOpenConnection(FtpIoSession session);

    /**
     * 关闭连接计数
     * @param session
     */
    void setCloseConnection(FtpIoSession session);

    /**
     * 增加当前登录次数
     * @param session
     */
    void setLogin(FtpIoSession session);

    /**
     * 增加登录失败次数
     * @param session
     */
    void setLoginFail(FtpIoSession session);

    /**
     * 递减当前登录次数
     */
    void setLogout(FtpIoSession session);

    /**
     *
     * 重置所有累积总数计数器。不要重置当前计数器，如当前登录，否则当有人断开连接时，这些计数器将变为负值
     *
     */
    void resetStatisticsCounters();
}
