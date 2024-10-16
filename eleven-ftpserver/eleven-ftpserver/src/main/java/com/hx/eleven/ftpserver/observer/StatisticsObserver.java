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

import java.net.InetAddress;

/**
 * 
 * 观察者实现接口
 *
 * @author
 */
public interface StatisticsObserver {

    /**
     * 用户文件上传通知
     */
    void notifyUpload();

    /**
     * 用户文件下载通知。
     */
    void notifyDownload();

    /**
     * 用户文件删除通知
     */
    void notifyDelete();

    /**
     *  用户创建目录通知
     */
    void notifyMkdir();

    /**
     * 用户删除目录通知
     */
    void notifyRmdir();

    /**
     * 新用户登录通知
     * @param anonymous
     */
    void notifyLogin(boolean anonymous);

    /**
     *
     * 用户登录失败通知
     * @param address 故障来源的远程地址
     */
    void notifyLoginFail(InetAddress address);

    /**
     * 用户注销通知
     * @param anonymous
     */
    void notifyLogout(boolean anonymous);

    /**
     * 连接打开通知
     */
    void notifyOpenConnection();

    /**
     * 连接关闭通知
     */
    void notifyCloseConnection();

}
