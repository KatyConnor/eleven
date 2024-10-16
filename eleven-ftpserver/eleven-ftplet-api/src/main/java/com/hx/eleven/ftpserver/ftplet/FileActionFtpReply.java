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
 * 一种更具体的FtpReply类型，用于处理单个文件或目录(如MKD、DELE、RMD等)的命令。
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * 
 */

public interface FileActionFtpReply extends FtpReply {

    /**
     *
     *  返回操作所针对的文件(或目录)(例如上传、创建、列出)
     * 
     * @return the file on which the action was taken. May return
     *         <code>null</code>, if the file information is not available.
     */
    FtpFile getFile();
}
