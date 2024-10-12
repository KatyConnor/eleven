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

package org.apache.ftpserver.ftplet;

/**
 * 用于文件系统实现的工厂-它为用户返回文件系统视图。
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface FileSystemFactory {

    /**
     *  创建用户指定的文件系统视图。
     * @param user The user for which the file system should be created
     * @return The current {@link FileSystemView} for the provided user
     * @throws FtpException 
     */
    FileSystemView createFileSystemView(User user) throws FtpException;

}
