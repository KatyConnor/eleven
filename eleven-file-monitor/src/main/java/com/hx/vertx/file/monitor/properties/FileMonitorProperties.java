package com.hx.vertx.file.monitor.properties;

import com.hx.vertx.file.monitor.annotations.ConfigurationPropertisBind;

@ConfigurationPropertisBind(prefix = "file.monitor")
public class FileMonitorProperties {
    /**
     * 日志文件目录
     */
    private String logPath;
    /**
     * 执行脚本路径
     */
    private String shell;
    /**
     * 监控文件目录
     */
    private String monitorDir;

    /**
     * 远程主机IP地址
     */
    private String serverIP;

    /**
     * 访问端口
     */
    private Integer port;

    /**
     * 传输方式,目前支持ftp 、http两种方式传输
     */
    private String transmissionMode;

    /**
     * 远程服务存储目录,如果是脚本传输,忽略此配置
     */
    private String targetDir;

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getMonitorDir() {
        return monitorDir;
    }

    public void setMonitorDir(String monitorDir) {
        this.monitorDir = monitorDir;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(String transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
}
