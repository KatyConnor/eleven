package com.hx.eleven.ftpserver.properties;

import com.hx.eleven.ftpserver.ssl.SslConfiguration;

/**
 * @auth wml
 * @date 2024/10/12
 */
public class DataConnectionProperties {

	/**
	 * 单位：秒
	 */
	private int idleTime = 300;
	private Boolean implicitSsl = true;
	private SslConfiguration ssl;

	private Boolean activeEnabled = true;
	private String activeLocalAddress;
	private int activeLocalPort = 0;
	private Boolean activeIpCheck = false;

	private String passiveAddress;
	private String passiveExternalAddress;
	private String passivePorts;
	private Boolean passiveIpCheck = false;

	public int getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}

	public Boolean getImplicitSsl() {
		return implicitSsl;
	}

	public void setImplicitSsl(Boolean implicitSsl) {
		this.implicitSsl = implicitSsl;
	}

	public SslConfiguration getSsl() {
		return ssl;
	}

	public void setSsl(SslConfiguration ssl) {
		this.ssl = ssl;
	}

	public Boolean getActiveEnabled() {
		return activeEnabled;
	}

	public void setActiveEnabled(Boolean activeEnabled) {
		this.activeEnabled = activeEnabled;
	}

	public String getActiveLocalAddress() {
		return activeLocalAddress;
	}

	public void setActiveLocalAddress(String activeLocalAddress) {
		this.activeLocalAddress = activeLocalAddress;
	}

	public int getActiveLocalPort() {
		return activeLocalPort;
	}

	public void setActiveLocalPort(int activeLocalPort) {
		this.activeLocalPort = activeLocalPort;
	}

	public Boolean getActiveIpCheck() {
		return activeIpCheck;
	}

	public void setActiveIpCheck(Boolean activeIpCheck) {
		this.activeIpCheck = activeIpCheck;
	}

	public String getPassiveAddress() {
		return passiveAddress;
	}

	public void setPassiveAddress(String passiveAddress) {
		this.passiveAddress = passiveAddress;
	}

	public String getPassiveExternalAddress() {
		return passiveExternalAddress;
	}

	public void setPassiveExternalAddress(String passiveExternalAddress) {
		this.passiveExternalAddress = passiveExternalAddress;
	}

	public String getPassivePorts() {
		return passivePorts;
	}

	public void setPassivePorts(String passivePorts) {
		this.passivePorts = passivePorts;
	}

	public Boolean getPassiveIpCheck() {
		return passiveIpCheck;
	}

	public void setPassiveIpCheck(Boolean passiveIpCheck) {
		this.passiveIpCheck = passiveIpCheck;
	}
}
