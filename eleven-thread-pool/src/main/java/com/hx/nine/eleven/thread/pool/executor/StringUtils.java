package com.hx.nine.eleven.thread.pool.executor;

public class StringUtils {

    private final static char CHAR_UNDERLINE = '-';
    private final static String EMPTITY = "";

    public static boolean isBlank(String str) {
        return null == str || str.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 下划线转驼峰格式,采用循环字符长度去字节判断转换，
     * 此种转换方式相对效率更高
     *
     * @param param
     * @return
     */
    public static String convertUnderlineToHump(String param) {
        if (isBlank(param)) {
            return EMPTITY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = param.charAt(i);
            if (ch == CHAR_UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

}
