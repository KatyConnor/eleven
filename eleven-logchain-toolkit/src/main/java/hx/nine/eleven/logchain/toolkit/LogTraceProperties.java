package hx.nine.eleven.logchain.toolkit;

import hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;

@ConfigurationPropertiesBind(prefix = "eleven.boot.log")
public class LogTraceProperties {

    private String[] includePath;
    private String[] excludePath;
    private Boolean enable = false;

    public String[] getIncludePath() {
        return includePath == null?new String[]{"/**"}:includePath;
    }

    public void setIncludePath(String[] includePath) {
        this.includePath = includePath;
    }

    public String[] getExcludePath() {
        return excludePath == null?new String[]{"/static/**"}:excludePath;
    }

    public void setExcludePath(String[] excludePath) {
        this.excludePath = excludePath;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
