package hx.nine.eleven.commons.enums;

import java.util.Arrays;

/**
 * 数据类型（基本数据类型、常用数据类型）
 */
public enum FundamentalDataTypeEnums {

    STRING("java.lang.String"),
    INTEGER("java.lang.Integer"),
    LONG("java.lang.Long"),
    BOOLEAN("java.lang.Boolean"),
    OBJECT("java.lang.Object"),
    BYTE("java.lang.Byte"),
    DOUBLE("java.lang.Double"),
    FLOAT("java.lang.Float"),
    CHARACTER("java.lang.Character"),
    SHORT("java.lang.Short"),
    LIST("java.util.List"),
    CLASS("java.lang.Class"),
    MAP("java.util.Map"),
    DATE("java.util.Date"),
    SQL_DATE("java.sql.Date"),
    BIGDECIMAL("java.math.BigDecimal");

    private String name;

    FundamentalDataTypeEnums(String name) {
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

    public static boolean findMatch(String name){
        return Arrays.stream(values()).filter(v-> v.name.equals(name)).findFirst() != null;
    }
}
