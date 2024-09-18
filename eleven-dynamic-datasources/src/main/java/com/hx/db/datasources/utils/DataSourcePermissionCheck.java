package com.hx.db.datasources.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataSourcePermissionCheck {

    private static String defaultDataSource;

    /**
     * 数据源只读权限
     */
    private final static Map<String,Boolean> dbReadOnlyMap = new HashMap<>();

    /**
     * 可执行权限
     */
    private final static Map<String, Set<String>> permissionMap = new HashMap<>();

    public static void setDefaultDataSource(String dataSource){
        defaultDataSource = dataSource;
    }

    public static boolean checkDefaultDataSource(String dataSource){
        return defaultDataSource.equals(dataSource);
    }

    public static void put(String dataSource,Boolean permission){
        dbReadOnlyMap.put(dataSource,permission);
    }

    public static Boolean get(String dataSource){
        return dbReadOnlyMap.get(dataSource);
    }

    public static void addPermission(String dataSource,String[] permissions){
        if (permissions != null){
            Set<String> permissionSet =new HashSet<>();
            for (String str : permissions){
                permissionSet.add(str);
            }
            permissionMap.put(dataSource,permissionSet);
        }
    }

    public static boolean checkPermission(String dataSource,String sqlCommandType){
        Set<String> permissionSet = permissionMap.get(dataSource);
        if (permissionSet == null){
            return false;
        }
        return permissionSet.contains(sqlCommandType);
    }
}
