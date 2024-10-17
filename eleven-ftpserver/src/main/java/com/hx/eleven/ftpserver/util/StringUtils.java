/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.hx.eleven.ftpserver.util;

import java.io.File;
import java.util.Map;

/**
 *
 * String 处理工具类
 *
 * @author
 */
public class StringUtils {

    /**
     * 字符串替换
     * @param source 源字符串
     * @param oldStr 源字符串中被替换的字符串
     * @param newStr 新字符串，用于替换指定的字符串
     * @return
     */
    public static String replaceString(String source, String oldStr, String newStr) {
        StringBuilder sb = new StringBuilder(source.length());
        int sind = 0;
        int cind = 0;
        while ((cind = source.indexOf(oldStr, sind)) != -1) {
            sb.append(source.substring(sind, cind));
            sb.append(newStr);
            sind = cind + oldStr.length();
        }
        sb.append(source.substring(sind));
        return sb.toString();
    }

    /**
     * 字符串 {} 按照数组顺序替换对应值
     * <br>
     * <code> replaceString( "This {} a {}", ["is', "string"]) </code> <br>
     * 执行后结果：
     * <code>"This is a string"</code><br>
     *
     * @param source 源字符串
     * @param args   参数值（数组）
     * @return
     */
    public static String replaceString(String source, Object[] args) {
        int startIndex = 0;
        int openIndex = source.indexOf('{', startIndex);
        if (openIndex == -1) {
            return source;
        }

        int closeIndex = source.indexOf('}', startIndex);
        if ((closeIndex == -1) || (openIndex > closeIndex)) {
            return source;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(source.substring(startIndex, openIndex));
        while (true) {
            String intStr = source.substring(openIndex + 1, closeIndex);
            int index = Integer.parseInt(intStr);
            sb.append(args[index]);

            startIndex = closeIndex + 1;
            openIndex = source.indexOf('{', startIndex);
            if (openIndex == -1) {
                sb.append(source.substring(startIndex));
                break;
            }

            closeIndex = source.indexOf('}', startIndex);
            if ((closeIndex == -1) || (openIndex > closeIndex)) {
                sb.append(source.substring(startIndex));
                break;
            }
            sb.append(source.substring(startIndex, openIndex));
        }
        return sb.toString();
    }

    /**
     * 字符串 {} 按照数组顺序替换对应值
     * <br>
     * <code>replaceString( "This {} a {}", Map("is', "string"))</code><br>
     * 执行后结果：
     * <code>"This is a string"</code><br>
     *
     * @param source 源字符串
     * @param args  参数值（数组）
     * @return
     */
    public static String replaceString(String source, Map<String, Object> args) {
        int startIndex = 0;
        int openIndex = source.indexOf('{', startIndex);
        if (openIndex == -1) {
            return source;
        }

        int closeIndex = source.indexOf('}', startIndex);
        if ((closeIndex == -1) || (openIndex > closeIndex)) {
            return source;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(source.substring(startIndex, openIndex));
        while (true) {
            String key = source.substring(openIndex + 1, closeIndex);
            Object val = args.get(key);
            if (val != null) {
                sb.append(val);
            }

            startIndex = closeIndex + 1;
            openIndex = source.indexOf('{', startIndex);
            if (openIndex == -1) {
                sb.append(source.substring(startIndex));
                break;
            }

            closeIndex = source.indexOf('}', startIndex);
            if ((closeIndex == -1) || (openIndex > closeIndex)) {
                sb.append(source.substring(startIndex));
                break;
            }
            sb.append(source.substring(startIndex, openIndex));
        }
        return sb.toString();
    }

    /**
     *
     * 这个方法用于动态地插入HTML块
     *
     * @param source HTML 字符串
     * @param bReplaceNl  true: '\n' 替换成： <br>
     * @param bReplaceTag true: '<' 替换成 &lt; 并且 '>' 替换成： &gt;
     * @param bReplaceQuote true: '\"' 替换成 &quot;
     */
    public static String formatHtml(String source, boolean bReplaceNl, boolean bReplaceTag, boolean bReplaceQuote) {

        StringBuilder sb = new StringBuilder();
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char c = source.charAt(i);
            switch (c) {
                case '\"':
                    if (bReplaceQuote) {
                        sb.append("&quot;");
                    } else {
                        sb.append(c);
                    }
                    break;
                case '<':
                    if (bReplaceTag) {
                        sb.append("&lt;");
                    } else {
                        sb.append(c);
                    }
                    break;
                case '>':
                    if (bReplaceTag) {
                        sb.append("&gt;");
                    } else {
                        sb.append(c);
                    }
                    break;
                case '\n':
                    if (bReplaceNl) {
                        if (bReplaceTag) {
                            sb.append("&lt;br&gt;");
                        } else {
                            sb.append("<br>");
                        }
                    } else {
                        sb.append(c);
                    }
                    break;
                case '\r':
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 字符串填充，默认从左边开始填充
     * @param src          源字符串
     * @param padChar      填充字符串
     * @param rightPad     是否右边填充
     * @param totalLength  填充后字符总长度
     * @return
     */
    public static String pad(String src, char padChar, boolean rightPad, int totalLength) {

        int srcLength = src.length();
        if (srcLength >= totalLength) {
            return src;
        }

        int padLength = totalLength - srcLength;
        StringBuilder sb = new StringBuilder(padLength);
        for (int i = 0; i < padLength; ++i) {
            sb.append(padChar);
        }

        if (rightPad) {
            return src + sb.toString();
        } else {
            return sb.toString() + src;
        }
    }

    /**
     *  从字节数组中获取十六进制字符串
     * @param res
     * @return
     */
    public static String toHexString(byte[] res) {
        StringBuilder sb = new StringBuilder(res.length << 1);
        for (int i = 0; i < res.length; i++) {
            String digit = Integer.toHexString(0xFF & res[i]);
            if (digit.length() == 1) {
                sb.append('0');
            }
            sb.append(digit);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 从十六进制字符串中获取字节数组。
     * @param hexString
     * @return
     */
    public static byte[] toByteArray(String hexString) {
        int arrLength = hexString.length() >> 1;
        byte buff[] = new byte[arrLength];
        for (int i = 0; i < arrLength; i++) {
            int index = i << 1;
            String digit = hexString.substring(index, index + 2);
            buff[i] = (byte) Integer.parseInt(digit, 16);
        }
        return buff;
    }

    /**
     * 根据文件路径返回 file 对象
     * @param filePath 4、目前生产部署并未
     * @return
     */
    public static File parseFile(String filePath) {
        if (hasText(filePath)) {
            return new File(filePath);
        }
        return null;
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
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

}
