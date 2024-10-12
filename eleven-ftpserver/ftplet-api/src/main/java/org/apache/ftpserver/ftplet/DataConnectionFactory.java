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
 * DataConnection 创建工厂Factory
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface DataConnectionFactory {

    /**
     * 打开一个 DataConnection 数据连接
     * 
     * @return  打开的  DataConnection 数据连接
     * @throws Exception
     */
    DataConnection openConnection() throws Exception;

    /**
     * 是否安全，即在SSL/TLS上运行。
     * 
     * @return true:false
     */

    boolean isSecure();

    /**
     * 关闭socket连接。如果不存在开放的数据连接，这将静默地忽略调用。
     */
    void closeDataConnection();

}