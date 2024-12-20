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

package com.hx.eleven.ftpserver.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import com.hx.eleven.ftpserver.FtpServerConfigurationException;
import com.hx.eleven.ftpserver.enums.ClientAuthEnum;
import com.hx.eleven.ftpserver.ssl.impl.DefaultSslConfiguration;
import com.hx.eleven.ftpserver.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据ssl 配置类创建 ssl 协议通道
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class SslConfigurationFactory {

	private final Logger LOG = LoggerFactory.getLogger(SslConfigurationFactory.class);

	// 密钥文件
	private File keystoreFile = new File("./res/.keystore");
	private String keystorePass;
	private String keystoreType = KeyStore.getDefaultType();
	private String keystoreAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
	private File trustStoreFile;
	private String trustStorePass;
	private String trustStoreType = KeyStore.getDefaultType();
	private String trustStoreAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
	private String[] sslProtocols = new String[]{"TLSv1.2"};
	private ClientAuthEnum clientAuthEnum = ClientAuthEnum.NONE;
	private String keyPass;
	private String keyAlias;
	private String[] enabledCipherSuites;

	/**
	 * 获取密钥文件地址
	 *
	 * @return
	 */
	public File getKeystoreFile() {
		return keystoreFile;
	}

	/**
	 * 设置密钥文件
	 *
	 * @param keyStoreFile A path to an existing key store file
	 */
	public void setKeystoreFile(File keyStoreFile) {
		if (keyStoreFile == null || keyStoreFile.length() == 0) {
			throw new FtpServerConfigurationException("KeystoreFile must not be null or zero length");
		}
		this.keystoreFile = keyStoreFile;
	}

	/**
	 *
	 * 密钥文件密码
	 * @return The password
	 */
	public String getKeystorePassword() {
		return keystorePass;
	}

	/**
	 *
	 * 设置密钥文件密码
	 * @param keystorePass The password
	 */
	public void setKeystorePassword(String keystorePass) {
		this.keystorePass = keystorePass;
	}

	/**
	 * 密钥类型, 默认类型详看 @see {@link KeyStore#getDefaultType()}
	 *
	 * @return
	 */
	public String getKeystoreType() {
		return keystoreType;
	}

	/**
	 * 设置密钥文件类型
	 *
	 * @param keystoreType The key store type
	 */
	public void setKeystoreType(String keystoreType) {
		if (keystoreType == null || keystoreType.length() == 0){
            throw new FtpServerConfigurationException("KeystoreType must not be null or zero length");
        }
		this.keystoreType = keystoreType;
	}

	/**
	 * The algorithm used to open the key store. Defaults to "SunX509"
	 *
	 * @return The key store algorithm
	 */
	public String getKeystoreAlgorithm() {
		return keystoreAlgorithm;
	}

	/**
	 * Override the key store algorithm used to open the key store
	 *
	 * @param keystoreAlgorithm The key store algorithm
	 */
	public void setKeystoreAlgorithm(String keystoreAlgorithm) {
		if (keystoreAlgorithm == null || keystoreAlgorithm.length() == 0)
			throw new FtpServerConfigurationException("KeystoreAlgorithm must not be null or zero length");
		this.keystoreAlgorithm = keystoreAlgorithm;

	}

	/**
	 * The SSL protocol used for this channel. Supported values are "SSL" and "TLS". Defaults to "TLS".
	 *
	 * @return The SSL protocol
	 */
	public String[] getSslProtocols() {
		return sslProtocols;
	}

	/**
	 * Set the SSL protocols used for this channel. Defaults to "TLSv1.2".
	 *
	 * @param sslProtocols The SSL protocols
	 */
	public void setSslProtocol(String... sslProtocols) {
		if (sslProtocols == null || sslProtocols.length == 0) {
			throw new FtpServerConfigurationException("SslProcotol must not be null or zero length");
		}

		this.sslProtocols = sslProtocols;
	}

	/**
	 * Set what client authentication level to use, supported values are "yes" or "true" for required authentication,
	 * "want" for wanted authentication and "false" or "none" for no authentication. Defaults to "none".
	 *
	 * @param clientAuthReqd The desired authentication level
	 */
	public void setClientAuthentication(String clientAuthReqd) {
		if ("true".equalsIgnoreCase(clientAuthReqd) || "yes".equalsIgnoreCase(clientAuthReqd) || "need".equalsIgnoreCase(clientAuthReqd)) {
			this.clientAuthEnum = ClientAuthEnum.NEED;
		} else if ("want".equalsIgnoreCase(clientAuthReqd)) {
			this.clientAuthEnum = ClientAuthEnum.WANT;
		} else {
			this.clientAuthEnum = ClientAuthEnum.NONE;
		}
	}

	/**
	 * The password used to load the key
	 *
	 * @return The password
	 */
	public String getKeyPassword() {
		return keyPass;
	}

	/**
	 * Set the password used to load the key
	 *
	 * @param keyPass The password
	 */
	public void setKeyPassword(String keyPass) {
		this.keyPass = keyPass;
	}

	/**
	 * Get the file used to load the truststore
	 *
	 * @return The {@link File} containing the truststore
	 */
	public File getTruststoreFile() {
		return trustStoreFile;
	}

	/**
	 * Set the password used to load the trust store
	 *
	 * @param trustStoreFile The password
	 */
	public void setTruststoreFile(File trustStoreFile) {
		this.trustStoreFile = trustStoreFile;
	}

	/**
	 * The password used to load the trust store
	 *
	 * @return The password
	 */
	public String getTruststorePassword() {
		return trustStorePass;
	}

	/**
	 * Set the password used to load the trust store
	 *
	 * @param trustStorePass The password
	 */
	public void setTruststorePassword(String trustStorePass) {
		this.trustStorePass = trustStorePass;
	}

	/**
	 * The trust store type, defaults to @see {@link KeyStore#getDefaultType()}
	 *
	 * @return The trust store type
	 */
	public String getTruststoreType() {
		return trustStoreType;
	}

	/**
	 * Set the trust store type
	 *
	 * @param trustStoreType The trust store type
	 */
	public void setTruststoreType(String trustStoreType) {
		this.trustStoreType = trustStoreType;
	}

	/**
	 * The algorithm used to open the trust store. Defaults to "SunX509"
	 *
	 * @return The trust store algorithm
	 */
	public String getTruststoreAlgorithm() {
		return trustStoreAlgorithm;
	}

	/**
	 * Override the trust store algorithm used to open the trust store
	 *
	 * @param trustStoreAlgorithm The trust store algorithm
	 */
	public void setTruststoreAlgorithm(String trustStoreAlgorithm) {
		this.trustStoreAlgorithm = trustStoreAlgorithm;

	}

	private KeyStore loadStore(File storeFile, String storeType, String storePass) throws IOException, GeneralSecurityException {
		InputStream fin = null;
		try {
			if (storeFile.exists()) {
				LOG.debug("Trying to load store from file");
				fin = new FileInputStream(storeFile);
			} else {
				LOG.debug("Trying to load store from classpath");
				fin = getClass().getClassLoader().getResourceAsStream(storeFile.getPath());

				if (fin == null) {
					throw new FtpServerConfigurationException("Key store could not be loaded from " + storeFile.getPath());
				}
			}

			KeyStore store = KeyStore.getInstance(storeType);
			store.load(fin, storePass.toCharArray());

			return store;
		} finally {
			IoUtils.close(fin);
		}
	}

	/**
	 * Create an instance of {@link SslConfiguration} based on the configuration of this factory.
	 *
	 * @return The {@link SslConfiguration} instance
	 */
	public SslConfiguration createSslConfiguration() {

		try {
			// initialize keystore
			LOG.debug("Loading key store from \"{}\", using the key store type \"{}\"", keystoreFile.getAbsolutePath(), keystoreType);
			KeyStore keyStore = loadStore(keystoreFile, keystoreType, keystorePass);

			KeyStore trustStore;

			if (trustStoreFile != null) {
				LOG.debug("Loading trust store from \"{}\", using the key store type \"{}\"", trustStoreFile.getAbsolutePath(), trustStoreType);
				trustStore = loadStore(trustStoreFile, trustStoreType, trustStorePass);
			} else {
				trustStore = keyStore;
			}

			String keyPassToUse;

			if (keyPass == null) {
				keyPassToUse = keystorePass;
			} else {
				keyPassToUse = keyPass;
			}

			// initialize key manager factory
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(keystoreAlgorithm);
			keyManagerFactory.init(keyStore, keyPassToUse.toCharArray());

			// initialize trust manager factory
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(trustStoreAlgorithm);
			trustManagerFactory.init(trustStore);

			return new DefaultSslConfiguration(keyManagerFactory, trustManagerFactory, clientAuthEnum, sslProtocols,
					enabledCipherSuites, keyAlias);
		} catch (Exception ex) {
			LOG.error("DefaultSsl.configure()", ex);
			throw new FtpServerConfigurationException("DefaultSsl.configure()", ex);
		}
	}

	/**
	 * Return the required client authentication setting
	 *
	 * @return {@link ClientAuthEnum#NEED} if client authentication is required, {@link ClientAuthEnum#WANT} is client
	 * authentication is wanted or {@link ClientAuthEnum#NONE} if no client authentication is the be performed
	 */
	public ClientAuthEnum getClientAuth() {
		return clientAuthEnum;
	}

	/**
	 * Returns the cipher suites that should be enabled for this connection. Must return null if the default (as decided
	 * by the JVM) cipher suites should be used.
	 *
	 * @return An array of cipher suites, or null.
	 */
	public String[] getEnabledCipherSuites() {
		if (enabledCipherSuites != null) {
			return enabledCipherSuites.clone();
		} else {
			return null;
		}
	}

	/**
	 * Set the allowed cipher suites, note that the exact list of supported cipher suites differs between JRE
	 * implementations.
	 *
	 * @param enabledCipherSuites
	 */
	public void setEnabledCipherSuites(String[] enabledCipherSuites) {
		if (enabledCipherSuites != null) {
			this.enabledCipherSuites = enabledCipherSuites.clone();
		} else {
			this.enabledCipherSuites = null;
		}
	}

	/**
	 * Get the server key alias to be used for SSL communication
	 *
	 * @return The alias, or null if none is set
	 */
	public String getKeyAlias() {
		return keyAlias;
	}

	/**
	 * Set the alias for the key to be used for SSL communication. If the specified key store contains multiple keys,
	 * this alias can be set to select a specific key.
	 *
	 * @param keyAlias The alias to use, or null if JSSE should be allowed to choose the key.
	 */
	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}
}
