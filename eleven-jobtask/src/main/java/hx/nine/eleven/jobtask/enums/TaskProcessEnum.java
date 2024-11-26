package hx.nine.eleven.jobtask.enums;

import java.util.Arrays;

/**
 * 任务处理状态
 * @author wml
 * @Discription
 * @Date 2023-06-08
 */
public enum TaskProcessEnum {

    PROCESSED("PROCESSED","已处理"),
    UNTREATED("UNTREATED","未处理"),
    IN_PROCESS("IN_PROCESS","处理中");

    private String code;
    private String name;

    TaskProcessEnum(String code, String name) {
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

    public static TaskProcessEnum getByCode(String code) {
        return Arrays.stream(values()).filter((o) -> {
            return o.code.equals(code);
        }).findFirst().orElse(null);
    }
}
