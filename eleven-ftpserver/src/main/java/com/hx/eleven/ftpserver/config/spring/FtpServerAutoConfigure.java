package com.hx.eleven.ftpserver.config.spring;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.hx.eleven.ftpserver.DataConnectionConfiguration;
import com.hx.eleven.ftpserver.DataConnectionConfigurationFactory;
import com.hx.eleven.ftpserver.FtpServerConfigurationException;
import com.hx.eleven.ftpserver.command.Command;
import com.hx.eleven.ftpserver.command.CommandFactory;
import com.hx.eleven.ftpserver.command.CommandFactoryFactory;
import com.hx.eleven.ftpserver.context.DefaultFtpServerContext;
import com.hx.eleven.ftpserver.db.HSQLDatasource;
import com.hx.eleven.ftpserver.enums.EncryptPasswordsEnum;
import com.hx.eleven.ftpserver.ftpletcontainer.impl.DefaultFtpletContainer;
import com.hx.eleven.ftpserver.impl.DefaultConnectionConfig;
import com.hx.eleven.ftpserver.impl.PassivePorts;
import com.hx.eleven.ftpserver.ipfilter.IpFilterType;
import com.hx.eleven.ftpserver.ipfilter.RemoteIpFilter;
import com.hx.eleven.ftpserver.ipfilter.SessionFilter;
import com.hx.eleven.ftpserver.listener.Listener;
import com.hx.eleven.ftpserver.listener.nio.NioListener;
import com.hx.eleven.ftpserver.message.MessageResource;
import com.hx.eleven.ftpserver.message.MessageResourceFactory;
import com.hx.eleven.ftpserver.properties.DataConnectionProperties;
import com.hx.eleven.ftpserver.properties.DataSourceProperties;
import com.hx.eleven.ftpserver.properties.FtpServerProperties;
import com.hx.eleven.ftpserver.properties.ListenerProperties;
import com.hx.eleven.ftpserver.properties.MessageProperties;
import com.hx.eleven.ftpserver.properties.SslConfigurationProperties;
import com.hx.eleven.ftpserver.ssl.SslConfiguration;
import com.hx.eleven.ftpserver.ssl.SslConfigurationFactory;
import com.hx.eleven.ftpserver.usermanager.DbUserManagerFactory;
import com.hx.eleven.ftpserver.usermanager.Md5PasswordEncryptor;
import com.hx.eleven.ftpserver.usermanager.PropertiesUserManagerFactory;
import com.hx.eleven.ftpserver.usermanager.SaltedPasswordEncryptor;
import com.hx.eleven.ftpserver.usermanager.UserManagerFactory;
import com.hx.eleven.ftpserver.util.StringUtils;
import com.hx.nine.eleven.core.annotations.Component;
import com.hx.nine.eleven.core.annotations.ConditionalOnBean;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.eleven.ftpserver.ftplet.Ftplet;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth wml
 * @date 2024/10/11
 */
@Component(init = "initFtpServerConfig")
@ConditionalOnBean(DefaultFtpServerContext.class)
public class FtpServerAutoConfigure {

	private FtpServerProperties ftpServerProperties;

	public FtpServerAutoConfigure() {
		this.ftpServerProperties = ElevenApplicationContextAware.getProperties(FtpServerProperties.class);
	}

	public void initFtpServerConfig() {
		// 初始化 DefaultFtpServerContext
		DefaultFtpServerContext serverContext = new DefaultFtpServerContext();

		MessageResourceFactory messageResourceFactory = new MessageResourceFactory();
		MessageProperties messageProperties = ftpServerProperties.getMessageProperties();
		if (messageProperties.getLanguages() != null) {
			messageResourceFactory.setLanguages(Arrays.asList(messageProperties.getLanguages()));
		}
		if (messageProperties.getDirectory() != null) {
			messageResourceFactory.setCustomMessageDirectory(new File(messageProperties.getDirectory()));
		}
		MessageResource messageResource = messageResourceFactory.createMessageResource();
		serverContext.setMessageResource(messageResource);

		// 初始化 Listener
		Map<String, Listener> listeners = initNioListener();
		serverContext.setListeners(listeners);

		Map<String, Ftplet> ftplets = new HashMap<>();
		ftplets.put("", null);
		serverContext.setFtpletContainer(new DefaultFtpletContainer(ftplets));

		// 初始化user-manager
		String fileUserManager = ftpServerProperties.getFileUserManager();
		UserManagerFactory userManagerFactory = null;
		if (StringUtils.hasText(fileUserManager)) {
			userManagerFactory = new PropertiesUserManagerFactory();
			((PropertiesUserManagerFactory) userManagerFactory).setUserDataFile(fileUserManager);
		} else {
			userManagerFactory = new DbUserManagerFactory();
			DataSourceProperties dataSourceProperties = ftpServerProperties.getDataSourceProperties();
			if (dataSourceProperties == null) {
				throw new RuntimeException("请配置数据源");
			}
			// 设置DataSource
			HSQLDatasource hsqlDatasource = new HSQLDatasource();
			hsqlDatasource.setJdbcUrl(dataSourceProperties.getJdbcUrl());
			hsqlDatasource.setUserName(dataSourceProperties.getUserName());
			hsqlDatasource.setPassword(dataSourceProperties.getPassword());
			hsqlDatasource.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
			serverContext.setHsqlDatasource(hsqlDatasource);
		}
		String encryptPasswords = ftpServerProperties.getEncryptPasswords();
		if (StringUtils.hasText(encryptPasswords)) {
			if (EncryptPasswordsEnum.MD5_SALTED == EncryptPasswordsEnum.valueOf(encryptPasswords)) {
				userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
			} else {
				userManagerFactory.setPasswordEncryptor(new Md5PasswordEncryptor());
			}
		}

		// 读取数据库文件
		serverContext.setUserManager(userManagerFactory.createUserManager());
		DefaultConnectionConfig connectionConfig = new DefaultConnectionConfig();
		serverContext.setConnectionConfig(connectionConfig);

		// commands 命令设置
		Map<String, Command> commandMap = initCommandMap();
		if (commandMap != null && commandMap.size() > 0) {
			CommandFactoryFactory commandFactoryFactory = new CommandFactoryFactory();
			commandFactoryFactory.setUseDefaultCommands(ftpServerProperties.getUseDefaultCommands());
			commandFactoryFactory.setCommandMap(commandMap);
			CommandFactory commandFactory = commandFactoryFactory.createCommandFactory();
			serverContext.setCommandFactory(commandFactory);
		}


		DefaultElevenApplicationContext.build().addBean(serverContext);

		// 初始化
	}

	private Map<String, Listener> initNioListener() {
		Map<String, Listener> listeners = new HashMap<>();
		List<ListenerProperties> listenerPropertiesList = ftpServerProperties.getListenerProperties();
		for (ListenerProperties listenerProperties : listenerPropertiesList) {
			RemoteIpFilter remoteIpListFilter = null;
			String[] blacklist = listenerProperties.getBlacklist();
			if (blacklist != null) {
				try {
					remoteIpListFilter = new RemoteIpFilter(IpFilterType.DENY, blacklist);
				} catch (UnknownHostException e) {
					throw new IllegalArgumentException("Invalid IP address or subnet in the 'blacklist' element", e);
				}
			}

			String[] remoteIpFilters = listenerProperties.getRemoteIpFilter();
			String remoteIpFilterType = listenerProperties.getRemoteIpFilterType();
			// blacklist和remoteIpFilter只能配置一个
			if (blacklist != null && remoteIpFilters != null) {
				throw new FtpServerConfigurationException("Properties 'remoteIpFilter' may not be used when 'blacklist' element is specified. ");
			}
			// blacklist == null, remoteIpFilter != null 时，直接配置 remoteIpFilter
			if (remoteIpFilters != null) {
				try {
					remoteIpListFilter = new RemoteIpFilter(IpFilterType.parse(remoteIpFilterType), remoteIpFilters);
				} catch (UnknownHostException e) {
					throw new IllegalArgumentException("Invalid IP address or subnet in the 'remote-ip-filter' element");
				}
			}

			SslConfigurationProperties sslConfigurationProperties = listenerProperties.getSslConfigurationProperties();
			SslConfigurationFactory ssl = null;
			if (sslConfigurationProperties != null) {
				ssl = new SslConfigurationFactory();
				ssl.setKeystoreFile(StringUtils.parseFile(sslConfigurationProperties.getKeystoreFile()));
				ssl.setKeystorePassword(sslConfigurationProperties.getKeystorePassword());
				ssl.setKeystoreType(sslConfigurationProperties.getKeystoreType());
				ssl.setKeyAlias(sslConfigurationProperties.getKeystoreKeyAlias());
				ssl.setKeyPassword(sslConfigurationProperties.getKeystoreKeyPassword());
				ssl.setKeystoreAlgorithm(sslConfigurationProperties.getKeystoreAlgorithm());

				ssl.setTruststoreFile(StringUtils.parseFile(sslConfigurationProperties.getTruststoreFile()));
				ssl.setTruststorePassword(sslConfigurationProperties.getTruststorePassword());
				ssl.setTruststoreType(sslConfigurationProperties.getTruststoreType());
				ssl.setTruststoreAlgorithm(sslConfigurationProperties.getTruststoreAlgorithm());

				ssl.setClientAuthentication(sslConfigurationProperties.getClientAuthentication());
				ssl.setEnabledCipherSuites(sslConfigurationProperties.getEnabledCipherSuites());
				ssl.setSslProtocol(sslConfigurationProperties.getProtocol());
			}

			DataConnectionProperties dataConnectionProperties = listenerProperties.getDataConnectionProperties();
			DataConnectionConfigurationFactory factory = null;
			if (dataConnectionProperties != null) {
				new PassivePorts(Collections.emptySet(), true);
				factory = new DataConnectionConfigurationFactory();
				factory.setImplicitSsl(dataConnectionProperties.getImplicitSsl());
				factory.setSslConfiguration(dataConnectionProperties.getSsl());
				factory.setIdleTime(dataConnectionProperties.getIdleTime());
				factory.setActiveEnabled(dataConnectionProperties.getActiveEnabled());
				factory.setActiveIpCheck(dataConnectionProperties.getActiveIpCheck());
				factory.setActiveLocalPort(dataConnectionProperties.getActiveLocalPort());
				factory.setActiveLocalAddress(dataConnectionProperties.getActiveLocalAddress());
				factory.setPassiveAddress(dataConnectionProperties.getPassiveAddress());
				factory.setPassiveExternalAddress(dataConnectionProperties.getPassiveExternalAddress());
				factory.setPassivePorts(dataConnectionProperties.getPassivePorts());
				factory.setPassiveIpCheck(dataConnectionProperties.getPassiveIpCheck());
			}

			Listener listener = createListener(listenerProperties.getServerAddress(), listenerProperties.getPort(),
					listenerProperties.getImplicitSsl(), listenerProperties.getIdleTimeout(), remoteIpListFilter,
					ssl.createSslConfiguration(), factory.createDataConnectionConfiguration());
			listeners.put(listenerProperties.getName(), listener);
		}
		return listeners;
	}

	/**
	 * 创建一个 Listener
	 *
	 * @return The created listener
	 */
	private Listener createListener(String serverAddress, int port, boolean implicitSsl, int idleTimeout,
									SessionFilter sessionFilter, SslConfiguration ssl,
									DataConnectionConfiguration dataConnectionConfig) {
		try {
			InetAddress.getByName(serverAddress);
		} catch (UnknownHostException e) {
			throw new FtpServerConfigurationException("Unknown host", e);
		}

		if (sessionFilter == null) {
			throw new IllegalStateException("sessionFilter can not null ");
		}
		return new NioListener(serverAddress, port, implicitSsl, ssl,
				dataConnectionConfig, idleTimeout, sessionFilter);
	}

	private Map<String, Command> initCommandMap() {
		String commandStr = ftpServerProperties.getCommands();
		if (StringUtils.hasText(commandStr)) {
			Map<String, Command> commandMap = new HashMap<>();
			String[] commands = commandStr.split("\\|");
			for (String str : commands) {
				String[] command = str.split(":");
				try {
					ConstructorAccess constructorAccess = ConstructorAccess.get(Class.forName(command[1]));
					Object obj = constructorAccess.newInstance();
					commandMap.put(command[0], (Command) obj);
				} catch (ClassNotFoundException e) {

				}
			}
			return commandMap;
		}
		return null;
	}
}
