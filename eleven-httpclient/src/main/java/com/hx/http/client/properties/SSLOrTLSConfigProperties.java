package com.hx.http.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

/**
 * @Author mingliang
 * @Date 2019-01-30
 */
@Component
@ConfigurationProperties(prefix = "cqrcb.ssl.keystore")
@ConfigurationPropertiesBinding
public class SSLOrTLSConfigProperties {

    private String filePath;
    private String passWord;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
