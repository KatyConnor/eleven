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

package com.hx.eleven.ftpserver.listener.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hx.eleven.ftpserver.DataConnectionConfiguration;
import com.hx.eleven.ftpserver.FtpServerConfigurationException;
import com.hx.eleven.ftpserver.context.DefaultFtpServerContext;
import com.hx.eleven.ftpserver.enums.ClientAuthEnum;
import com.hx.eleven.ftpserver.handler.FtpHandler;
import com.hx.eleven.ftpserver.ipfilter.MinaSessionFilter;
import com.hx.eleven.ftpserver.session.FtpIoSession;
import com.hx.eleven.ftpserver.ssl.SslConfiguration;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.eleven.ftpserver.request.DefaultFtpHandler;
import com.hx.eleven.ftpserver.context.FtpServerContext;
import com.hx.eleven.ftpserver.ipfilter.SessionFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NIO实现Listener
 *
 * @author wml
 */
public class NioListener extends AbstractListener {

	private final Logger LOG = LoggerFactory.getLogger(NioListener.class);

	private SocketAcceptor acceptor;
	private InetSocketAddress address;
	// 是否挂起，默认 false
	boolean suspended = false;
	// handler对象
	private FtpHandler handler = new DefaultFtpHandler();

	public NioListener(String serverAddress, int port, boolean implicitSsl, SslConfiguration sslConfiguration,
					   DataConnectionConfiguration dataConnectionConfig, int idleTimeout, SessionFilter sessionFilter) {
		super(serverAddress, port, implicitSsl, sslConfiguration, dataConnectionConfig, idleTimeout, sessionFilter);
	}

	/**
	 * 启动 Listener
	 *
	 * @param context
	 */
	public synchronized void start(FtpServerContext context) {
		if (!isStopped()) {
			throw new IllegalStateException("Listener already started");
		}

		try {
			this.acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors());
			if (this.getServerAddress() != null) {
				this.address = new InetSocketAddress(this.getServerAddress(), this.getPort());
			} else {
				this.address = new InetSocketAddress(this.getPort());
			}
			this.acceptor.setReuseAddress(true);
			this.acceptor.getSessionConfig().setReadBufferSize(2048);
			this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, this.getIdleTimeout());
			//减小默认的接收器缓冲区大小
			this.acceptor.getSessionConfig().setReceiveBufferSize(512);

			MdcInjectionFilter mdcFilter = new MdcInjectionFilter();
			this.acceptor.getFilterChain().addLast("mdcFilter", mdcFilter);
			SessionFilter sessionFilter = this.getSessionFilter();
			if (sessionFilter != null) {
				// 将IP过滤器添加到过滤器链中
				this.acceptor.getFilterChain().addLast("sessionFilter", new MinaSessionFilter(sessionFilter));
			}
			this.acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(context.getThreadPoolExecutor()));
			this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new FtpServerProtocolCodecFactory()));
			this.acceptor.getFilterChain().addLast("mdcFilter2", mdcFilter);
			this.acceptor.getFilterChain().addLast("logger", new FtpLoggingFilter());
			// 是否 ssl 加密
			if (this.isImplicitSsl()) {
				SslConfiguration sslConf = this.getSslConfiguration();
				SslFilter sslFilter;
				try {
					sslFilter = new SslFilter(sslConf.getSSLContext());
				} catch (GeneralSecurityException e) {
					throw new FtpServerConfigurationException("SSL could not be initialized, check configuration");
				}
				if (sslConf.getClientAuth() == ClientAuthEnum.NEED) {
					sslFilter.setNeedClientAuth(true);
				} else if (sslConf.getClientAuth() == ClientAuthEnum.WANT) {
					sslFilter.setWantClientAuth(true);
				}
				if (sslConf.getEnabledProtocols() != null) {
					sslFilter.setEnabledProtocols(sslConf.getEnabledProtocols());
				}
				if (sslConf.getEnabledCipherSuites() != null) {
					sslFilter.setEnabledCipherSuites(sslConf.getEnabledCipherSuites());
				}
				this.acceptor.getFilterChain().addFirst("sslFilter", sslFilter);
			}
			this.handler.init(context, this);
			this.acceptor.setHandler(new FtpHandlerAdapter(context, this.handler));
			try {
				this.acceptor.bind(this.address);
			} catch (IOException e) {
				throw new FtpServerConfigurationException("Failed to bind to address " + address + ", check configuration", e);
			}
			updatePort();

		} catch (RuntimeException e) {
			stop();
			throw e;
		}
	}

	private void updatePort() {
		this.setPort(this.acceptor.getLocalAddress().getPort());
	}

	public synchronized void stop() {
		// 关闭服务
		if (this.acceptor != null) {
			this.acceptor.unbind();
			this.acceptor.dispose();
			this.acceptor = null;
		}
	}

	public boolean isStopped() {
		return this.acceptor == null;
	}

	public boolean isSuspended() {
		return this.suspended;
	}

	public synchronized void resume() {
		if (this.acceptor != null && this.suspended) {
			try {
				LOG.debug("Resuming listener");
				this.acceptor.bind(this.address);
				LOG.debug("Listener resumed");
				updatePort();
				this.suspended = false;
			} catch (IOException e) {
				LOG.error("Failed to resume listener", e);
			}
		}
	}

	public synchronized void suspend() {
		if (this.acceptor != null && !this.suspended) {
			LOG.debug("Suspending listener");
			this.acceptor.unbind();
			this.suspended = true;
			LOG.debug("Listener suspended");
		}
	}

	public synchronized Set<FtpIoSession> getActiveSessions() {
		Map<Long, IoSession> sessions = this.acceptor.getManagedSessions();
		Set<FtpIoSession> ftpSessions = new HashSet<>();
		for (IoSession session : sessions.values()) {
			ftpSessions.add(new FtpIoSession(session, ElevenApplicationContextAware.getBean(DefaultFtpServerContext.class)));
		}
		return ftpSessions;
	}
}
