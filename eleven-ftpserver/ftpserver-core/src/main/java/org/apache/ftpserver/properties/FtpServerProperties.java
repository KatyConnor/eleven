package org.apache.ftpserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @auth wml
 * @date 2024/10/11
 */
@ConfigurationProperties(prefix = "eleven.ftp.server")
public class FtpServerProperties {

	/**
	 * 服务端口
	 */
	private int port;

	/**
	 * 最大并发登录用户数
	 */
	private int maxLogins;

	/**
	 * 是否允许匿名登录，默认不允许匿名登录
	 */
	private boolean anonymousLoginEnabled;

	/**
	 *  最大匿名登录数
	 */
	private int maxAnonymousLogins;

	/**
	 * 用户登录失败的最大允许次数
	 */
	private int maxLoginFailures;

	/**
	 * 登录失败最大等待时间
	 */
	private int loginFailureDelay;

	/**
	 * 最大线程数
	 */
	private int maxThreads;

	/**
	 * 是否开启 ssl 安全协议
	 */
	private Boolean openSSL = false;

	private MessageProperties messageProperties;

	private SslConfigurationProperties sslConfigurationProperties;

	private DataConnectionProperties dataConnectionProperties;

	private ListenerProperties listenerProperties;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxLogins() {
		return maxLogins;
	}

	public void setMaxLogins(int maxLogins) {
		this.maxLogins = maxLogins;
	}

	public boolean isAnonymousLoginEnabled() {
		return anonymousLoginEnabled;
	}

	public void setAnonymousLoginEnabled(boolean anonymousLoginEnabled) {
		this.anonymousLoginEnabled = anonymousLoginEnabled;
	}

	public int getMaxAnonymousLogins() {
		return maxAnonymousLogins;
	}

	public void setMaxAnonymousLogins(int maxAnonymousLogins) {
		this.maxAnonymousLogins = maxAnonymousLogins;
	}

	public int getMaxLoginFailures() {
		return maxLoginFailures;
	}

	public void setMaxLoginFailures(int maxLoginFailures) {
		this.maxLoginFailures = maxLoginFailures;
	}

	public int getLoginFailureDelay() {
		return loginFailureDelay;
	}

	public void setLoginFailureDelay(int loginFailureDelay) {
		this.loginFailureDelay = loginFailureDelay;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public MessageProperties getMessageProperties() {
		return messageProperties;
	}

	public void setMessageProperties(MessageProperties messageProperties) {
		this.messageProperties = messageProperties;
	}

	public SslConfigurationProperties getSslConfigurationProperties() {
		return sslConfigurationProperties;
	}

	public void setSslConfigurationProperties(SslConfigurationProperties sslConfigurationProperties) {
		this.sslConfigurationProperties = sslConfigurationProperties;
	}

	public Boolean getOpenSSL() {
		return openSSL;
	}

	public void setOpenSSL(Boolean openSSL) {
		this.openSSL = openSSL;
	}

	public DataConnectionProperties getDataConnectionProperties() {
		return dataConnectionProperties;
	}

	public void setDataConnectionProperties(DataConnectionProperties dataConnectionProperties) {
		this.dataConnectionProperties = dataConnectionProperties;
	}

	public ListenerProperties getListenerProperties() {
		return listenerProperties;
	}

	public void setListenerProperties(ListenerProperties listenerProperties) {
		this.listenerProperties = listenerProperties;
	}
}
