package com.hx.vertx.file.monitor.enums;

import java.util.Arrays;

public enum EnvironmentEnums {

  ENV("environment", "环境变量");
  private String code;
  private String name;

  EnvironmentEnums(String code, String name) {
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

  public static EnvironmentEnums getByCode(String code){
    return Arrays.stream(EnvironmentEnums.values()).filter(e -> e.getCode().equals(code)).findFirst().orElseGet(null);
  }
}
