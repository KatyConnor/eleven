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

package org.apache.ftpserver.impl;

import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.ssl.SslConfiguration;

/**
 * <p>
 *  数据连接配置
 */
public class DefaultDataConnectionConfiguration implements DataConnectionConfiguration {

	/**
	 * 最大空闲时间，单位：秒
	 */
	private final int idleTime;
	/**
	 * ssl配置
	 */
	private final SslConfiguration ssl;
	/**
	 * 连接是否激活状态
	 */
	private final boolean activeEnabled;
	/**
	 * 本地使用IP地址
	 */
	private final String activeLocalAddress;
	/**
	 * 本地使用端口
	 */
	private final int activeLocalPort;
	/**
	 * 是否检查激活使用IP
	 */
	private final boolean activeIpCheck;
	/**
	 *
	 */
	private final String passiveAddress;
	private final String passiveExternalAddress;
	private final PassivePorts passivePorts;
	private final boolean passiveIpCheck;
    /**
     * 是否强制使用 ssl 通道
     */
	private final boolean implicitSsl;

	/**
	 * {@link DataConnectionConfigurationFactory}
	 */
	public DefaultDataConnectionConfiguration(int idleTime, SslConfiguration ssl, boolean activeEnabled,
											  boolean activeIpCheck, String activeLocalAddress,
											  int activeLocalPort, String passiveAddress,
											  PassivePorts passivePorts, String passiveExternalAddress,
											  boolean passiveIpCheck, boolean implicitSsl) {
		this.idleTime = idleTime;
		this.ssl = ssl;
		this.activeEnabled = activeEnabled;
		this.activeIpCheck = activeIpCheck;
		this.activeLocalAddress = activeLocalAddress;
		this.activeLocalPort = activeLocalPort;
		this.passiveAddress = passiveAddress;
		this.passivePorts = passivePorts;
		this.passiveExternalAddress = passiveExternalAddress;
		this.passiveIpCheck = passiveIpCheck;
		this.implicitSsl = implicitSsl;
	}

	/**
	 * Get the maximum idle time in seconds.
	 */
	public int getIdleTime() {
		return idleTime;
	}

	/**
	 * Is PORT enabled?
	 */
	public boolean isActiveEnabled() {
		return activeEnabled;
	}

	/**
	 * Check the PORT IP?
	 */
	public boolean isActiveIpCheck() {
		return activeIpCheck;
	}

	/**
	 * Get the local address for active mode data transfer.
	 */
	public String getActiveLocalAddress() {
		return activeLocalAddress;
	}

	/**
	 * Get the active local port number.
	 */
	public int getActiveLocalPort() {
		return activeLocalPort;
	}

	/**
	 * Get passive host.
	 */
	public String getPassiveAddress() {
		return passiveAddress;
	}

	/**
	 * Get external passive host.
	 */
	public String getPassiveExernalAddress() {
		return passiveExternalAddress;
	}

	public boolean isPassiveIpCheck() {
		return passiveIpCheck;
	}

	/**
	 * 请求一个被动端口。将阻塞直到端口可用
	 */
	public synchronized int requestPassivePort() {
		return passivePorts.reserveNextPort();
	}

	/**
	 * Retrive the passive ports configured for this data connection
	 *
	 * @return The String of passive ports
	 */
	public String getPassivePorts() {
		return passivePorts.toString();
	}

	/**
	 * 释放端口
	 */
	public synchronized void releasePassivePort(final int port) {
		passivePorts.releasePort(port);
	}

	/**
	 * Get SSL component.
	 */
	public SslConfiguration getSslConfiguration() {
		return ssl;
	}

	/**
	 * @see org.apache.ftpserver.DataConnectionConfiguration#isImplicitSsl()
	 */
	public boolean isImplicitSsl() {
		return implicitSsl;
	}
}
