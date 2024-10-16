package com.hx.eleven.ftpserver.properties;

/**
 * @auth wml
 * @date 2024/10/12
 */
public class ListenerProperties {

	/**
	 * Listener 名称
	 */
	private String name;
	private String serverAddress;
	private int port = 21;
	private Boolean implicitSsl = false;
	private int idleTimeout = 300;
	/**
	 * 黑名单配置
	 */
	private String[] blacklist;
	/**
	 * IP地址黑白名单控制
	 */
	private String[] remoteIpFilter;
	/**
	 * 远程IP地址控制类型 ；ALLOW : 白名单 ；DENY ： 黑名单
	 */
	private String remoteIpFilterType;

	private SslConfigurationProperties sslConfigurationProperties;

	private DataConnectionProperties dataConnectionProperties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Boolean getImplicitSsl() {
		return implicitSsl;
	}

	public void setImplicitSsl(Boolean implicitSsl) {
		this.implicitSsl = implicitSsl;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public String[] getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(String[] blacklist) {
		this.blacklist = blacklist;
	}

	public String[] getRemoteIpFilter() {
		return remoteIpFilter;
	}

	public void setRemoteIpFilter(String[] remoteIpFilter) {
		this.remoteIpFilter = remoteIpFilter;
	}

	public String getRemoteIpFilterType() {
		return remoteIpFilterType;
	}

	public void setRemoteIpFilterType(String remoteIpFilterType) {
		this.remoteIpFilterType = remoteIpFilterType;
	}

	public SslConfigurationProperties getSslConfigurationProperties() {
		return sslConfigurationProperties;
	}

	public void setSslConfigurationProperties(SslConfigurationProperties sslConfigurationProperties) {
		this.sslConfigurationProperties = sslConfigurationProperties;
	}

	public DataConnectionProperties getDataConnectionProperties() {
		return dataConnectionProperties;
	}

	public void setDataConnectionProperties(DataConnectionProperties dataConnectionProperties) {
		this.dataConnectionProperties = dataConnectionProperties;
	}
}
