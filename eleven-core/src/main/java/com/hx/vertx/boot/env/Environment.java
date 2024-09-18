package com.hx.vertx.boot.env;

public interface Environment extends PropertyResolver{

  /**
   * 激活环境变量
   * @return
   */
  String getActiveProfiles();
}
