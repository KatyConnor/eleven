package com.hx.db.datasources.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 数据源key值
 * @Author wml
 *  * @Date 2019-02-15
 */
public enum DataBaseEnum {

    MYSQL("MYSQL","MySQL数据库"),
    ORACLE("ORACLE","Oracle数据库"),
    DB2("DB2","DB2数据库"),
    SQL_SERVER("SQL_SERVER","SQLserver数据库"),

    ;

    private String key;
    private String name;

    DataBaseEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static DataBaseEnum getByKey(final String keyCode){
        return getAll().stream().filter(o -> o.key.equals(keyCode)).findFirst().orElse(null);
    }

    public static List<DataBaseEnum> getAll(){
        return Arrays.asList(DataBaseEnum.values());
    }





}
