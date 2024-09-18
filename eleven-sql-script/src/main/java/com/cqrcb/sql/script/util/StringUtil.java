package com.cqrcb.sql.script.util;

/**
 * @author wml
 * @Description
 * @data 2022-08-12
 */
public class StringUtil {

    /**
     * 判断字符是否为空
     *
     * @params str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "null".equals(str) || str.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || "null".equals(obj.toString()) || obj.toString().isEmpty();
    }

    public static boolean isBlank(String str) {
        return null == str || "null".equals(str)  || str.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断字符是否为空
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 判断字符是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
