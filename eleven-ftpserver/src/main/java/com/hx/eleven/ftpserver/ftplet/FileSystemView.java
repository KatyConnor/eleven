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
 * 这是对用户访问FTP进行文件系统视图展示的抽象
 *
 * @author
 */
public interface FileSystemView {

    /**
     * 用户根目录
     * @return
     * @throws FtpException 
     */
    FtpFile getHomeDirectory() throws FtpException;

    /**
     * Get user current directory.
     * @return The {@link FtpFile} for the users current directory
     * @throws FtpException 
     */
    FtpFile getWorkingDirectory() throws FtpException;

    /**
     * 切换用户目录
     * @param dir 要切换的目录
     * @return 切换成功：true|失败：false
     * @throws FtpException 
     */
    boolean changeWorkingDirectory(String dir) throws FtpException;

    /**
     * 获取文件
     * @param file The path to the file to get
     * @return The {@link FtpFile} for the provided path
     * @throws FtpException 
     */
    FtpFile getFile(String file) throws FtpException;

    /**
     * Does the file system support random file access?
     * @return true if the file supports random access
     * @throws FtpException 
     */
    boolean isRandomAccessible() throws FtpException;

    /**
     * Dispose file system view.
     */
    void dispose();
}
