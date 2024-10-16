package com.hx.eleven.ftpserver.enums;

/**
 * 密码加密类型，目前支持MD5以及随机盐MD5加密
 * @auth wml
 * @date 2024/10/16
 */
public enum EncryptPasswordsEnum {

	/**
	 * 需要客户端身份验证
	 */
	MD5("MD5"),

	/**
	 * 请求客户端身份验证，但不是必需的
	 */
	MD5_SALTED("MD5_SALTED");

	String name;

	EncryptPasswordsEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
