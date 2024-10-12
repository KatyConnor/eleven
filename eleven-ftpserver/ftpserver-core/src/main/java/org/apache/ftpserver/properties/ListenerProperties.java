package org.apache.ftpserver.properties;

/**
 * @auth wml
 * @date 2024/10/12
 */
public class ListenerProperties {
	private String serverAddress;
	private int port = 21;
	private Boolean implicitSsl = false;
	private int idleTimeout = 300;
	/**
	 * 黑名单配置
	 */
	private String[] blacklist;

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
}
