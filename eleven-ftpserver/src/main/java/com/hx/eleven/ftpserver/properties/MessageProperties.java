package com.hx.eleven.ftpserver.properties;

import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;

/**
 * @auth wml
 * @date 2024/10/11
 */
@ConfigurationPropertiesBind(prefix = "eleven.ftp.server.message")
public class MessageProperties {

	/**
	 * message 语言类型
	 */
	private String languages;
	/**
	 * message 文件存放地址
	 */
	private String directory;

	public String[] getLanguages() {
		return languages != null && languages.indexOf(",") != -1 ?languages.split("[\\s,]+"):null;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
}
