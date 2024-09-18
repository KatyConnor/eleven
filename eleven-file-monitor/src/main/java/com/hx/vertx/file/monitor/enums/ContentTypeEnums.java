package com.hx.vertx.file.monitor.enums;

import java.util.Arrays;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-14
 */
public enum ContentTypeEnums {

  APPLICATION_JSON("application/json;charset=UTF-8"),
  MULTIPART_FORM_DATA("multipart/form-data");
  private String code;

  ContentTypeEnums(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public static ContentTypeEnums getByCode(String code){
    return Arrays.stream(ContentTypeEnums.values()).filter(e -> e.getCode().equals(code)).findFirst().orElseGet(null);
  }
}
