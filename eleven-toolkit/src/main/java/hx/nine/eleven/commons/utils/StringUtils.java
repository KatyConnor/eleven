package hx.nine.eleven.commons.utils;

import hx.nine.eleven.commons.exception.StringExpressException;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 类型基本操作
 *
 * @Author wml
 * @Date 2019-02-18
 */
public class StringUtils {

    private final static String STR_UNDERLINE = "_";
    private final static char CHAR_UNDERLINE = '_';
    private final static String EMPTITY = "";
    private final static char CHAR_BRACKETS = '$';
    private final static char CHAR_LEFT_BRACKETS = '{';
    private final static char CHAR_RIGHT_BRACKETS = '}';
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 把字符串中 {} 替换成对应的变量值
     * @param str   字符串
     * @param strs  变量值
     * @return      返回替换之后的字符串
     */
    public static String format(String str, String... strs) {
        if (isBlank(str)) {
            return EMPTITY;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        int j = 0;
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == CHAR_LEFT_BRACKETS) {
                sb.append(j>= strs.length?"":strs[j]);
                ++i;
                j++;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 把字符串中 {} 替换成对应的变量值
     * @param str   字符串
     * @param valueMap  变量值
     * @return      返回替换之后的字符串
     */
    public static String formatStr(String str, Map<String, Object> valueMap) {
        if (isBlank(str)) {
            return EMPTITY;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                break;
            }
            char c = str.charAt(i);
            if (c == CHAR_LEFT_BRACKETS) {
                i = findVariable(sb,i+1,str,valueMap);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 把字符串中 {} 替换成对应的变量值
     * @param str   字符串
     * @param props  变量值
     * @return      返回替换之后的字符串
     */
    public static String formatStr(String str, Properties props) {
        if (isBlank(str)) {
            return null;
        }

        int len = str.length();
        StringBuilder  source = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                break;
            }
            char c = str.charAt(i);
            if (c == CHAR_LEFT_BRACKETS) {
                i = findVariable(source,i+1,str,props);
            } else {
                source.append(c);
            }
        }
        return source.toString();
    }

    /**
     * 把字符串中${XXX} 替换成对应的变量值
     *
     * @param str      字符串
     * @param valueMap 变量值
     * @return 返回替换之后的字符串
     */
    public static String format(String str, Map<String, Object> valueMap) {
        if (isBlank(str)) {
            return EMPTITY;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                char c = str.charAt(i);
                sb.append(c);
                break;
            }
            char c = str.charAt(i);
            char brackets = str.charAt(i + 1);
            if (c == CHAR_BRACKETS && brackets == CHAR_LEFT_BRACKETS) {
                i = findVariable(sb,i+2,str,valueMap);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String initialLetterLowercase(String str) {
        if (isBlank(str)) {
            return EMPTITY;
        }
        StringBuilder sb = new StringBuilder(str.length());
        sb.append(Character.toLowerCase(str.charAt(0))).append(str.substring(1));
        return sb.toString();
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

    /**
     * 驼峰格式字符串转换为下划线格式字符串，采用循环字符长度取字节进行判断转换
     * 效率相对更高，耗时更少
     *
     * @param param
     * @return 小写字符串
     */
    public static String convertHumpToUnderline(String param) {
        if (param == null || EMPTITY.equals(param.trim())) {
            return EMPTITY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(STR_UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     *
     * @param param
     * @return
     */
    public static String convertFirstUpperCase(String param) {
        if (param == null || EMPTITY.equals(param.trim())) {
            return EMPTITY;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        char c = param.charAt(0);
        sb.append(Character.toUpperCase(c));
        sb.append(param.substring(1));
        return sb.toString();
    }

    /**
     * 驼峰转下划线，且转为大写
     *
     * @param param
     * @return 大写字符串
     */
    public static String convertHumpToUnderlineAndUpperCase(String param) {
        return convertHumpToUnderline(param).toUpperCase();
    }

    /**
     * 驼峰转换为下划线,效率convertHumpToUnderline差不多
     *
     * @param camelCaseName
     * @return
     */
    public static String convertHumpToUnderline1(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append(STR_UNDERLINE);
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转下划线,效率不是很稳定，比convertHumpToUnderline2效率稍高
     *
     * @param str
     * @return
     */
    public static String convertHumpToUnderline3(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, STR_UNDERLINE + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 截取字符串右边N位
     *
     * @param str
     * @param len
     * @return
     */
    public static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    public static boolean isNumeric(String s) {
        if (s == null || "".equals(s)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(s).matches();
    }


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


    /**
     * 改变字符串中的某一位的字符值
     *
     * @param source     需要改变的字符串
     * @param position   位置
     * @param changeChar 改变后的字符
     * @return 输入 ("abc",2,'e') 输出"abe"
     * @author git_kezhs
     */
    public static String changeCharInString(String source, int position, String changeChar) throws Exception {
        if (source == null) {
            return null;
        }
        char[] sourceList = source.toCharArray();

        if (source.length() <= position) {
            throw new Exception("输入参数不合法");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sourceList.length; i++) {
            if (i == position) {
                sb.append(changeChar);
            } else {
                sb.append(sourceList[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 改变字符串中的某一位的字符值
     *
     * @param source     需要改变的字符串
     * @param position   位置
     * @param changeChar 改变后的字符
     * @return 输入 ("abcde",2,2,'f') 输出"abfe"
     * @author git_kezhs
     */
    public static String changeCharInString(String source, int position,int length, String changeChar) throws Exception {
        if (source == null) {
            return null;
        }
        char[] sourceList = source.toCharArray();

        if (source.length() <= position) {
            throw new Exception("输入参数不合法");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sourceList.length; i++) {
            if (i == position) {
                sb.append(changeChar);
                i = i+length;
            } else {
                sb.append(sourceList[i]);
            }
        }
        return sb.toString();
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            if (cs1.length() != cs2.length()) {
                return false;
            } else {
                return cs1 instanceof String && cs2 instanceof String ? cs1.equals(cs2) : CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, cs1.length());
            }
        } else {
            return false;
        }
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        if (str1 != null && str2 != null) {
            if (str1 == str2) {
                return true;
            } else {
                return str1.length() != str2.length() ? false : CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, str1.length());
            }
        } else {
            return str1 == str2;
        }
    }

    /**
     * 判断为null 返回""
     *
     * @param str
     * @return
     */
    public static String nullToEmptyString(String str) {
        return str == null ? "" : str;
    }

    public static String toString(Object obj){
            return obj == null ? "" : obj.toString();
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 中文 转成 Unicode
     * @param str
     * @return
     */
    public static String chToUnicode(String str) {
        char[] chars = str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            stringBuilder.append("\\").append(Integer.toString(chars[i], 16));
        }
        return stringBuilder.toString();
    }

    /**
     * Unicode转成中文
     *
     * @param str
     */
    public static String unicodeToCH(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * 显示前四位和后四位
     * @param str
     * @return
     */
    public static String desenStr(String str){
        if (str == null || EMPTITY.equals(str.trim())) {
            return EMPTITY;
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(str.charAt(i));
            if (i == 3){
                i = len - 5;
                sb.append("******");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "2022062902033025";
        String result = desenStr(str);
        System.out.println(result);
    }

    private static int findVariable(StringBuilder source,int i,String str,Map<String, Object> valueMap) throws StringExpressException {
        boolean next = true;
        int index = i;
        StringBuilder value = new StringBuilder();
        while (next) {
            if (index == str.length()){
                throw new StringExpressException("没有正确的结束符号'}'");
            }
            char end = str.charAt(index);
            if (end == CHAR_LEFT_BRACKETS){
                throw new StringExpressException("没有正确的结束符号'}'");
            }

            if (end == CHAR_RIGHT_BRACKETS) {
                next = false;
                break;
            }
            value.append(end);
            index++;
        }
        source.append(valueMap.get(value.toString()));
        return index;
    }

    private static int findVariable(StringBuilder source,int i,String str,Properties props) throws StringExpressException {
        boolean next = true;
        int index = i;
        StringBuilder value = new StringBuilder();
        while (next) {
            if (index == str.length()){
                throw new StringExpressException("表达式没有正确的结束标志'}'");
            }
            char end = str.charAt(index);
            if (end == CHAR_LEFT_BRACKETS){
                throw new StringExpressException("表达式没有正确的结束标志'}'");
            }

            if (end == CHAR_RIGHT_BRACKETS) {
                next = false;
                break;
            }
            value.append(end);
            index++;
        }
        source.append(props.get(value.toString()));
        return index;
    }

    public static boolean hasText(CharSequence str) {
        return str != null && str.length() > 0 && containsText(str);
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    public static String valueOf(Object obj){
        return !Optional.ofNullable(obj).isPresent()?"":String.valueOf(obj);
    }

    public static String append(String sourceStr,String targetStr){
        return (isBlank(sourceStr)?"":sourceStr)+(isBlank(targetStr)?"":targetStr);
    }
}
