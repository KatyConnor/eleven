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

package com.hx.eleven.ftpserver.ssl.impl;

import java.security.GeneralSecurityException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

import com.hx.eleven.ftpserver.enums.ClientAuthEnum;
import com.hx.eleven.ftpserver.ssl.SslConfiguration;
import com.hx.eleven.ftpserver.util.ClassUtils;
import com.hx.eleven.ftpserver.ssl.SslConfigurationFactory;

/**
 *
 *  配置控制通道或数据通道的SSL设置
 *  @author
 */
public class DefaultSslConfiguration implements SslConfiguration {

    private final KeyManagerFactory keyManagerFactory;
    private final TrustManagerFactory trustManagerFactory;
    private String[] enabledProtocols = new String[] {"TLSv1.2"};
    private final ClientAuthEnum clientAuthEnum;// = ClientAuth.NONE;
    private final String keyAlias;
    private final String[] enabledCipherSuites;
    private final SSLContext sslContext;
    private final SSLSocketFactory socketFactory;

    /**
     * 使用于 {@link SslConfigurationFactory}
     * 
     * @throws GeneralSecurityException
     */
    public DefaultSslConfiguration(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory,
                                   ClientAuthEnum clientAuthEnumReqd, String[] sslProtocols, String[] enabledCipherSuites, String keyAlias) throws GeneralSecurityException {
        super();
        this.clientAuthEnum = clientAuthEnumReqd;
        this.enabledCipherSuites = enabledCipherSuites;
        this.keyAlias = keyAlias;
        this.keyManagerFactory = keyManagerFactory;
        this.enabledProtocols = sslProtocols;
        this.trustManagerFactory = trustManagerFactory;
        this.sslContext = initContext();
        this.socketFactory = sslContext.getSocketFactory();
    }

    /**
     *
     * @throws GeneralSecurityException
     */
    public DefaultSslConfiguration(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory,
                                   ClientAuthEnum clientAuthEnumReqd, String sslProtocol, String[] enabledCipherSuites, String keyAlias) throws GeneralSecurityException {
        super();
        this.clientAuthEnum = clientAuthEnumReqd;
        this.enabledCipherSuites = enabledCipherSuites;
        this.keyAlias = keyAlias;
        this.keyManagerFactory = keyManagerFactory;
        this.enabledProtocols = new String[] {sslProtocol};
        this.trustManagerFactory = trustManagerFactory;
        this.sslContext = initContext();
        this.socketFactory = sslContext.getSocketFactory();
    }

    public SSLSocketFactory getSocketFactory() throws GeneralSecurityException {
        return socketFactory;
    }

    /**
     * @see SslConfiguration#getSSLContext(String)
     */
    public SSLContext getSSLContext(String enabledProtocol) throws GeneralSecurityException {
        return sslContext;
    }

    /**
     * @see SslConfiguration#getEnabledProtocol()
     */
    public String getEnabledProtoco() {
        if ((enabledProtocols != null) && (enabledProtocols.length > 0)) {
            //使用集合中第一个
            return enabledProtocols[0];
        } else {
            return DEFAULT_ENABLED_PROTOCOL;
        }
    }

    /**
     * @see SslConfiguration#getEnabledProtocols()
     */
    public String[] getEnabledProtocols() {
        return enabledProtocols;
    }

    /**
     * @see SslConfiguration#getClientAuth()
     */
    public ClientAuthEnum getClientAuth() {
        return clientAuthEnum;
    }

    /**
     * @see SslConfiguration#getSSLContext()
     */
    public SSLContext getSSLContext() throws GeneralSecurityException {
        return getSSLContext(enabledProtocols[0]);
    }

    /**
     * @see SslConfiguration#getEnabledCipherSuites()
     */
    public String[] getEnabledCipherSuites() {
        if (enabledCipherSuites != null) {
            return enabledCipherSuites.clone();
        } else {
            return null;
        }
    }

    private SSLContext initContext() throws GeneralSecurityException {
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
    
        // wrap key managers to allow us to control their behavior
        // (FTPSERVER-93)
        for (int i = 0; i < keyManagers.length; i++) {
            if (ClassUtils.extendsClass(keyManagers[i].getClass(), "javax.net.ssl.X509ExtendedKeyManager")) {
                keyManagers[i] = new ExtendedAliasKeyManager(keyManagers[i], keyAlias);
            } else if (keyManagers[i] instanceof X509KeyManager) {
                keyManagers[i] = new AliasKeyManager(keyManagers[i], keyAlias);
            }
        }
    
        // create and initialize the SSLContext
        SSLContext ctx = SSLContext.getInstance(enabledProtocols[0]);
        ctx.init(keyManagers, trustManagerFactory.getTrustManagers(), null);
        
        // Create the socket factory
        return ctx;
    }
}
