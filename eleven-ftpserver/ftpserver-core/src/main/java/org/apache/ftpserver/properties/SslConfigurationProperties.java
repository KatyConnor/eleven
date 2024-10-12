package org.apache.ftpserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @auth wml
 * @date 2024/10/11
 */
@ConfigurationProperties(prefix = "eleven.ftp.server.ssl")
public class SslConfigurationProperties {

	private String keystoreFile;
	private String keystorePassword;
	private String keystoreType;

	private String keystoreKeyAlias;
	private String keystoreKeyPassword;
	private String keystoreAlgorithm;

	private String truststoreFile;
	private String truststorePassword;
	private String truststoreType;
	private String truststoreAlgorithm;

	private String clientAuthentication;
	private String[] enabledCipherSuites;
	private String protocol;

	public String getKeystoreFile() {
		return keystoreFile;
	}

	public void setKeystoreFile(String keystoreFile) {
		this.keystoreFile = keystoreFile;
	}

	public String getKeystoreType() {
		return keystoreType;
	}

	public void setKeystoreType(String keystoreType) {
		this.keystoreType = keystoreType;
	}

	public String getKeystoreKeyAlias() {
		return keystoreKeyAlias;
	}

	public void setKeystoreKeyAlias(String keystoreKeyAlias) {
		this.keystoreKeyAlias = keystoreKeyAlias;
	}

	public String getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	public String getKeystoreKeyPassword() {
		return keystoreKeyPassword;
	}

	public void setKeystoreKeyPassword(String keystoreKeyPassword) {
		this.keystoreKeyPassword = keystoreKeyPassword;
	}

	public String getKeystoreAlgorithm() {
		return keystoreAlgorithm;
	}

	public void setKeystoreAlgorithm(String keystoreAlgorithm) {
		this.keystoreAlgorithm = keystoreAlgorithm;
	}

	public String getTruststoreFile() {
		return truststoreFile;
	}

	public void setTruststoreFile(String truststoreFile) {
		this.truststoreFile = truststoreFile;
	}

	public String getTruststorePassword() {
		return truststorePassword;
	}

	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}

	public String getTruststoreType() {
		return truststoreType;
	}

	public void setTruststoreType(String truststoreType) {
		this.truststoreType = truststoreType;
	}

	public String getTruststoreAlgorithm() {
		return truststoreAlgorithm;
	}

	public void setTruststoreAlgorithm(String truststoreAlgorithm) {
		this.truststoreAlgorithm = truststoreAlgorithm;
	}

	public String getClientAuthentication() {
		return clientAuthentication;
	}

	public void setClientAuthentication(String clientAuthentication) {
		this.clientAuthentication = clientAuthentication;
	}

	public String[] getEnabledCipherSuites() {
		return enabledCipherSuites;
	}

	public void setEnabledCipherSuites(String[] enabledCipherSuites) {
		this.enabledCipherSuites = enabledCipherSuites;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}
