package com.hx.eleven.ftpserver.properties;


import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;

import java.util.List;

/**
 * @auth wml
 * @date 2024/10/11
 */
@ConfigurationPropertiesBind(prefix = "eleven.ftp.server")
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

	/**
	 * 默认文件系统,
	 */
	private Boolean caseInsensitive = false;
	private Boolean createHome = true;
	/**
	 * 用户配置文件
	 */
	private String userFile;
	/**
	 * 是否使用默认的命令
	 */
	private Boolean useDefaultCommands = true;
	/**
	 * 自定义命令
	 */
	private String commands;

	/**
	 * 用户信息文件
	 */
	private String fileUserManager;
	/**
	 * 用户密码加密类型； true;md5;salted;非空（明文）
	 */
	private String encryptPasswords;

	private MessageProperties messageProperties;

	private List<ListenerProperties> listenerProperties;

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

	public Boolean getOpenSSL() {
		return openSSL;
	}

	public void setOpenSSL(Boolean openSSL) {
		this.openSSL = openSSL;
	}

	public Boolean getCaseInsensitive() {
		return caseInsensitive;
	}

	public void setCaseInsensitive(Boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	public Boolean getCreateHome() {
		return createHome;
	}

	public void setCreateHome(Boolean createHome) {
		this.createHome = createHome;
	}

	public List<ListenerProperties> getListenerProperties() {
		return listenerProperties;
	}

	public void setListenerProperties(List<ListenerProperties> listenerProperties) {
		this.listenerProperties = listenerProperties;
	}

	public String getUserFile() {
		return userFile;
	}

	public void setUserFile(String userFile) {
		this.userFile = userFile;
	}

	public Boolean getUseDefaultCommands() {
		return useDefaultCommands;
	}

	public void setUseDefaultCommands(Boolean useDefaultCommands) {
		this.useDefaultCommands = useDefaultCommands;
	}

	public String getCommands() {
		return commands;
	}

	public void setCommands(String commands) {
		this.commands = commands;
	}

	public String getFileUserManager() {
		return fileUserManager;
	}

	public void setFileUserManager(String fileUserManager) {
		this.fileUserManager = fileUserManager;
	}

	public String getEncryptPasswords() {
		return encryptPasswords;
	}

	public void setEncryptPasswords(String encryptPasswords) {
		this.encryptPasswords = encryptPasswords;
	}
}
