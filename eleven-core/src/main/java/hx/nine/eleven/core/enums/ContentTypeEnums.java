package hx.nine.eleven.core.enums;

import java.util.Arrays;

/**
 * @Discription contentType类型枚举
 * @author wml
 * @Date 2023-03-14
 */
public enum ContentTypeEnums {
  // 常用类型
  APPLICATION_JSON("application/json;charset=UTF-8"),
  MULTIPART_FORM_DATA("multipart/form-data; charset=UTF-8"),
  TEXT_XML_UTF_8("text/xml; charset=UTF-8"),
  TEXT_PLAIN_UTF_8("text/plain; charset=UTF-8"),
  APPLICATION_FORM_URLENCODED_UTF_8("application/x-www-form-urlencoded; charset=UTF-8"),
  APPLICATION_XHTML("application/xhtml+xml; charset=UTF-8"),
  APPLICATION_XML("application/xml; charset=UTF-8"),
  APPLICATION_ATOM_XML("application/atom+xml; charset=UTF-8"),
  APPLICATION_PDF("application/pdf; charset=UTF-8"),
  APPLICATION_MSWORD("application/msword; charset=UTF-8"),
  APPLICATION_OCTET_STREAM("application/octet-stream; charset=UTF-8");

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
    return Arrays.stream(ContentTypeEnums.values()).filter(e -> e.getCode().equals(code) || e.getCode().startsWith(code)).findFirst().orElseGet(null);
  }
}
