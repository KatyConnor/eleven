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

package org.apache.ftpserver.ssl;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * SSL configuration
 */
public interface SslConfiguration {

    String DEFAULT_ENABLED_PROTOCOL = "TLSv1.2";
    
    /**
     *
     * 创建 SSLSocket 的 factory <code>SslConfiguration</code>
     * @return <code>SslConfiguration</code>.
     * @throws GeneralSecurityException
     *
     */
    SSLSocketFactory getSocketFactory() throws GeneralSecurityException;

    /**
     * 获取 SSL context
     * 
     * @return The {@link SSLContext}
     * @throws GeneralSecurityException
     */
    SSLContext getSSLContext() throws GeneralSecurityException;

    /**
     * 根据指定协ssl议获取 SSL context
     * 
     * @param protocol
     *            The protocol, SSL or TLS must be supported
     * @return The {@link SSLContext}
     * @throws GeneralSecurityException
     */
    SSLContext getSSLContext(String protocol) throws GeneralSecurityException;

    /**
     *
     * 返回应该为此连接启用的密码套件。如果应该使用默认（由JVM决定）密码套件，则必须返回null。
     * @return
     */
    String[] getEnabledCipherSuites();

    /**
     *
     *  返回默认ssl协议
     * @return The name of the protocol as a String
     */
    default String getEnabledProtocol() {
        return DEFAULT_ENABLED_PROTOCOL;
    }

    /**
     *
     * 返回ssl协议列表
     * @return
     */
    String[] getEnabledProtocols();

    /**
     *
     * 返回所需的客户端身份验证设置
     * @return {@link ClientAuth#NEED} if client authentication is required, {@link ClientAuth#WANT} is client
     *         authentication is wanted or {@link ClientAuth#NONE} if no client authentication is the be performed
     */
    ClientAuth getClientAuth();
}
