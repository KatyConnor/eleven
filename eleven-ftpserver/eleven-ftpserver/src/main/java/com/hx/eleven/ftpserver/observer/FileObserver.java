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
import com.hx.eleven.ftpserver.session.FtpIoSession;

/**
 *
 * 文件上传下载观察者
 *
 * @author wml
 */
public interface FileObserver {

    /**
     * 用户文件上传通知
     */
    void notifyUpload(FtpIoSession session, FtpFile file, long size);

    /**
     * 用户文件下载通知
     */
    void notifyDownload(FtpIoSession session, FtpFile file, long size);

    /**
     * 用户文件删除通知
     */
    void notifyDelete(FtpIoSession session, FtpFile file);

    /**
     *  用户创建目录文件通知
     */
    void notifyMkdir(FtpIoSession session, FtpFile file);

    /**
     * 用户删除目录通知
     */
    void notifyRmdir(FtpIoSession session, FtpFile file);

}
