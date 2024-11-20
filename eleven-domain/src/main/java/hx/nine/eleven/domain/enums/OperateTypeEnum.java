package hx.nine.eleven.domain.enums;

/**
 * @author wml
 * 2020-06-29
 */
public enum  OperateTypeEnum {

    UPDATE("UPDATE","修改"),
    INSERT("INSERT","插入"),
    SELECT("SELECT","查询"),
    DELETE("DELETE","删除");

    private String code;
    private String value;

    OperateTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
