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

package com.hx.eleven.ftpserver.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.hx.eleven.ftpserver.ConnectionConfig;
import com.hx.eleven.ftpserver.auth.ConcurrentLoginPermission;
import com.hx.eleven.ftpserver.auth.TransferRatePermission;
import com.hx.eleven.ftpserver.auth.WritePermission;
import com.hx.eleven.ftpserver.command.CommandFactoryFactory;
import com.hx.eleven.ftpserver.db.HSQLDatasource;
import com.hx.eleven.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
import com.hx.eleven.ftpserver.ftpletcontainer.FtpletContainer;
import com.hx.eleven.ftpserver.ftpletcontainer.impl.DefaultFtpletContainer;
import com.hx.eleven.ftpserver.listener.Listener;
import com.hx.eleven.ftpserver.message.MessageResourceFactory;
import com.hx.eleven.ftpserver.usermanager.PropertiesUserManagerFactory;
import com.hx.nine.eleven.core.annotations.Component;
import com.hx.eleven.ftpserver.ConnectionConfigFactory;
import com.hx.eleven.ftpserver.command.CommandFactory;
import com.hx.eleven.ftpserver.ftplet.Authority;
import com.hx.eleven.ftpserver.ftplet.FileSystemFactory;
import com.hx.eleven.ftpserver.ftplet.FtpStatistics;
import com.hx.eleven.ftpserver.ftplet.Ftplet;
import com.hx.eleven.ftpserver.ftplet.UserManager;
import com.hx.eleven.ftpserver.observer.DefaultFtpStatistics;
import com.hx.eleven.ftpserver.listener.ListenerFactory;
import com.hx.eleven.ftpserver.message.MessageResource;
import com.hx.eleven.ftpserver.usermanager.impl.BaseUser;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ftp server 上下文对象
 * @author wml
 */
@Component
public class DefaultFtpServerContext implements FtpServerContext {

	private final Logger LOG = LoggerFactory.getLogger(DefaultFtpServerContext.class);

	private MessageResource messageResource = new MessageResourceFactory().createMessageResource();

	private UserManager userManager = new PropertiesUserManagerFactory().createUserManager();

	private FileSystemFactory fileSystemManager = new NativeFileSystemFactory();

	private FtpletContainer ftpletContainer = new DefaultFtpletContainer();

	private FtpStatistics statistics = new DefaultFtpStatistics();

	private CommandFactory commandFactory = new CommandFactoryFactory().createCommandFactory();

	private ConnectionConfig connectionConfig = new ConnectionConfigFactory().createConnectionConfig();

	private HSQLDatasource hsqlDatasource = new HSQLDatasource();

	private Map<String, Listener> listeners = new HashMap<>();
	/**
	 * 用户密码登录用户权限验证
	 */
	private static final List<Authority> ADMIN_AUTHORITIES = new ArrayList<>();
	/**
	 * 匿名用户
	 */
	private static final List<Authority> ANON_AUTHORITIES = new ArrayList<>();

	/**
	 * 线程池
	 */
	private ThreadPoolExecutor threadPoolExecutor = null;

	static {
		ADMIN_AUTHORITIES.add(new WritePermission());
		ANON_AUTHORITIES.add(new ConcurrentLoginPermission(20, 2));
		ANON_AUTHORITIES.add(new TransferRatePermission(4800, 4800));
	}


	public DefaultFtpServerContext() {
		// 默认的 Listener
		listeners.put("default", new ListenerFactory().createListener());
	}

	/**
	 * 创建一个默认用户
	 * @throws Exception
	 */
	@Deprecated
	public void createDefaultUsers() throws Exception {
		UserManager userManager = this.getUserManager();

		// create admin user
		String adminName = userManager.getAdminName();
		if (!userManager.doesExist(adminName)) {
			LOG.info("Creating user : " + adminName);
			BaseUser adminUser = new BaseUser();
			adminUser.setName(adminName);
			adminUser.setPassword(adminName);
			adminUser.setEnabled(true);
			adminUser.setAuthorities(ADMIN_AUTHORITIES);
			adminUser.setHomeDirectory("./res/home");
			adminUser.setMaxIdleTime(0);
			userManager.save(adminUser);
		}

		// 创建匿名用户
		if (!userManager.doesExist("anonymous")) {
			LOG.info("Creating user : anonymous");
			BaseUser anonUser = new BaseUser();
			anonUser.setName("anonymous");
			anonUser.setPassword("");
			anonUser.setAuthorities(ANON_AUTHORITIES);
			anonUser.setEnabled(true);
			anonUser.setHomeDirectory("./res/home");
			anonUser.setMaxIdleTime(300);
			userManager.save(anonUser);
		}
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public FileSystemFactory getFileSystemManager() {
		return fileSystemManager;
	}

	@Override
	public MessageResource getMessageResource() {
		return messageResource;
	}

	public FtpStatistics getFtpStatistics() {
		return statistics;
	}

	public void setFtpStatistics(FtpStatistics statistics) {
		this.statistics = statistics;
	}

	@Override
	public FtpletContainer getFtpletContainer() {
		return ftpletContainer;
	}

	@Override
	public CommandFactory getCommandFactory() {
		return commandFactory;
	}

	public Ftplet getFtplet(String name) {
		return ftpletContainer.getFtplet(name);
	}

	@Override
	public Listener getListener(String name) {
		return listeners.get(name);
	}

	public void setListener(String name, Listener listener) {
		listeners.put(name, listener);
	}

	@Override
	public Map<String, Listener> getListeners() {
		return listeners;
	}

	public void setListeners(Map<String, Listener> listeners) {
		this.listeners = listeners;
	}

	public void addListener(String name, Listener listener) {
		listeners.put(name, listener);
	}

	public Listener removeListener(String name) {
		return listeners.remove(name);
	}

	public void setCommandFactory(CommandFactory commandFactory) {
		this.commandFactory = commandFactory;
	}

	public void setFileSystemManager(FileSystemFactory fileSystemManager) {
		this.fileSystemManager = fileSystemManager;
	}

	public void setFtpletContainer(FtpletContainer ftpletContainer) {
		this.ftpletContainer = ftpletContainer;
	}

	public void setMessageResource(MessageResource messageResource) {
		this.messageResource = messageResource;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public ConnectionConfig getConnectionConfig() {
		return connectionConfig;
	}

	public void setConnectionConfig(ConnectionConfig connectionConfig) {
		this.connectionConfig = connectionConfig;
	}

	public HSQLDatasource getHsqlDatasource() {
		return hsqlDatasource;
	}

	public void setHsqlDatasource(HSQLDatasource hsqlDatasource) {
		this.hsqlDatasource = hsqlDatasource;
	}

	@Override
	public void dispose() {
		listeners.clear();
		ftpletContainer.getFtplets().clear();
		if (threadPoolExecutor != null) {
			LOG.debug("Shutting down the thread pool executor");
			threadPoolExecutor.shutdown();
			try {
				threadPoolExecutor.awaitTermination(5000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			} finally {
				// TODO: how to handle?
			}
		}
	}

	@Override
	public synchronized ThreadPoolExecutor getThreadPoolExecutor() {
		if (this.threadPoolExecutor == null) {
			int maxThreads = this.connectionConfig.getMaxThreads();
			if (maxThreads < 1) {
				int maxLogins = this.connectionConfig.getMaxLogins();
				if (maxLogins > 0) {
					maxThreads = maxLogins;
				} else {
					maxThreads = 16;
				}
			}
			LOG.debug("Intializing shared thread pool executor with max threads of {}", maxThreads);
			this.threadPoolExecutor = new OrderedThreadPoolExecutor(maxThreads);
		}
		return this.threadPoolExecutor;
	}
}
