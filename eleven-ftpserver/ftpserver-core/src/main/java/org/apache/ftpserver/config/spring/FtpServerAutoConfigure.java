package org.apache.ftpserver.config.spring;

import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.impl.PassivePorts;
import org.apache.ftpserver.ipfilter.IpFilterType;
import org.apache.ftpserver.ipfilter.RemoteIpFilter;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.message.MessageResource;
import org.apache.ftpserver.message.MessageResourceFactory;
import org.apache.ftpserver.properties.DataConnectionProperties;
import org.apache.ftpserver.properties.FtpServerProperties;
import org.apache.ftpserver.properties.ListenerProperties;
import org.apache.ftpserver.properties.MessageProperties;
import org.apache.ftpserver.properties.SslConfigurationProperties;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @auth wml
 * @date 2024/10/11
 */
@Configuration
@EnableConfigurationProperties(FtpServerProperties.class)
@ConditionalOnProperty(prefix = "eleven.ftp.server", name = "enabled", havingValue = "true", matchIfMissing = true)
public class FtpServerAutoConfigure {

	private FtpServerProperties ftpServerProperties;

	public FtpServerAutoConfigure(FtpServerProperties ftpServerProperties) {
		this.ftpServerProperties = ftpServerProperties;
	}

	@Bean
	public FtpServerFactory initFtpServerFactory(){
		FtpServerFactory ftpServerFactory = new FtpServerFactory();
		MessageResourceFactory messageResourceFactory = new MessageResourceFactory();
		MessageProperties messageProperties = ftpServerProperties.getMessageProperties();
		if (messageProperties.getLanguages() !=null ){
			messageResourceFactory.setLanguages(Arrays.asList(messageProperties.getLanguages()));
		}
		if (messageProperties.getDirectory() != null){
			messageResourceFactory.setCustomMessageDirectory(new File(messageProperties.getDirectory()));
		}
		MessageResource messageResource = messageResourceFactory.createMessageResource();
		ftpServerFactory.setMessageResource(messageResource);

		ListenerFactory listenerFactory = new ListenerFactory();
		ListenerProperties listenerProperties = ftpServerProperties.getListenerProperties();
		listenerFactory.setPort(listenerProperties.getPort());
		listenerFactory.setIdleTimeout(listenerProperties.getIdleTimeout());
		listenerFactory.setServerAddress(listenerProperties.getServerAddress());
		listenerFactory.setImplicitSsl(listenerProperties.getImplicitSsl());
		String[] blacklist = listenerProperties.getBlacklist();
		if(blacklist != null){
			try {
				RemoteIpFilter remoteIpFilter = new RemoteIpFilter(IpFilterType.DENY,blacklist);
				listenerFactory.setSessionFilter(remoteIpFilter);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException("Invalid IP address or subnet in the 'blacklist' element", e);
			}
		}
		if (ftpServerProperties.getOpenSSL()){
			SslConfigurationFactory ssl = new SslConfigurationFactory();
			SslConfigurationProperties sslConfigurationProperties = ftpServerProperties.getSslConfigurationProperties();
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
			listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
		}

		DataConnectionProperties dataConnectionProperties = ftpServerProperties.getDataConnectionProperties();
		if (dataConnectionProperties != null){
			new PassivePorts(Collections.emptySet(), true);
			DataConnectionConfigurationFactory factory = new DataConnectionConfigurationFactory();
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
			listenerFactory.setDataConnectionConfiguration(factory.createDataConnectionConfiguration());
		}
		return ftpServerFactory;
	}
}
