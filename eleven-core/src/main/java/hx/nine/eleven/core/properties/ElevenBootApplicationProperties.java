package hx.nine.eleven.core.properties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import hx.nine.eleven.core.convert.FieldStringToArraysDeserializer;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-19
 */
@ConfigurationPropertiesBind(prefix = "eleven.boot")
public class ElevenBootApplicationProperties {

    /**
     * 指定绝对路径配置文件
     */
    private String configPath;
    /**
     * 激活环境
     */
    private String active;

    /**
     * 服务路由访问主路径
     */
    private String servletPath;

    /**
     * 端口
     */
    private Integer port = 20808;

    /**
     *  静态文件拦截地址
     * @TODO  应该移入对应的web框架中
     */
    private String staticPath = "/*";
    /**
     * 静态文件地址
     *  @TODO  应该移入对应的web框架中
     */
    private String staticRoot = "webroot";

    /**
     * 指定项目扫描路径
     */
    @JsonDeserialize(using = FieldStringToArraysDeserializer.class)
    private String[] scanPackages;

    /**
     * 会话过期时长，单位默认秒
     */
    private int expires;

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStaticPath() {
        return staticPath;
    }

    public void setStaticPath(String staticPath) {
        this.staticPath = staticPath;
    }

    public String getStaticRoot() {
        return staticRoot;
    }

    public void setStaticRoot(String staticRoot) {
        this.staticRoot = staticRoot;
    }

    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }
}
