package com.hx.nine.eleven.core.env;

public interface Environment extends PropertyResolver{

  /**
   * 激活环境变量
   * @return
   */
  String getActiveProfiles();
}
