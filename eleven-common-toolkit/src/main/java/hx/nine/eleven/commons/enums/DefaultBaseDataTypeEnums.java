package hx.nine.eleven.commons.enums;

import java.util.Arrays;

/**
 * 数据类型（基本数据类型、常用数据类型）
 */
public enum DefaultBaseDataTypeEnums {

    STRING("String"),
    INTEGER("Integer"),
    LONG("Long"),
    BOOLEAN("Boolean"),
    OBJECT("Object"),
    BYTE("Byte"),
    DOUBLE("Double"),
    FLOAT("Float"),
    CHARACTER("Character"),
    SHORT("Short"),
    LIST("List"),
    CLASS("Class"),
    MAP("Map"),
    Date("Date"),
    BIGDECIMAL("BigDecimal");

    private String name;

    DefaultBaseDataTypeEnums(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean noneMatch(String name){
       return Arrays.stream(values()).noneMatch(v-> v.name.equals(name));
    }
}
