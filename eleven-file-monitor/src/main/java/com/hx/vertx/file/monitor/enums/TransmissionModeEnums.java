package com.hx.vertx.file.monitor.enums;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public enum TransmissionModeEnums {
    FTP("FTP", "FTP传输"),
    RSYNC("RSYNC", "RSYNC传输"),
    HTTP("HTTP","HTTP传输");

    private String code;
    private String name;

    TransmissionModeEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static TransmissionModeEnums getByCode(String code){
        return Arrays.stream(TransmissionModeEnums.values()).filter(e -> e.getCode().equals(code)).findFirst().orElseGet(null);
    }
}
