package hx.nine.eleven.http.client.properties;

import hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;

/**
 * @Author mingliang
 * @Date 2019-01-30
 */
@ConfigurationPropertiesBind(prefix = "eleven.boot.http.ssl.keystore")
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
