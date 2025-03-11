package hx.nine.eleven.commons.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密算法
 * @Author wml
 * @Date 2019-02-19
 */
public class MD5Utils {

    private final static Logger LOGGER = LoggerFactory.getLogger(MD5Utils.class);
    private static final char[] hexDigitsLowerCase= {'0','1','2','3','4','5','6','7','8','9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] hexDigitsCapital = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F' };
    
    /**
     *  digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符，
     *  BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值，
     *  一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
     * @param str
     * @return
     */
    public static String getMD5String(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            LOGGER.error("exception:{}",e);
            return null;
        }
    }

    public static String stringMD5(String input) {
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(input.getBytes());
            // 转换并返回结果，也是字节数组，包含16个元素，字符数组转换成字符串返回
            return byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("exceptiuon:{}",e);
            return null;
        }
    }

    public static String stringMD5(String input,String charset) {
        try {
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes(charset));
            return byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOGGER.error("exceptiuon:{}",e);
            return null;
        }
    }

    /**
     * 下面这个函数用于将字节数组换成成16进制的字符串，首先初始化一个字符数组，用来存放每个16进制字符  hexDigits，
     * new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
     * @param byteArray
     * @return
     */
    public static String byteArrayToHex(byte[] byteArray) {

        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigitsCapital[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigitsCapital[b& 0xf];
        }
        return new String(resultCharArray);
    }

    /**
     * 下面这个函数用于将字节数组换成成16进制的字符串，首先初始化一个字符数组，用来存放每个16进制字符  hexDigits，
     * new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
     * @param byteArray
     * @return
     */
    public static String byteArrayToHexLow(byte[] byteArray) {

        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigitsLowerCase[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigitsLowerCase[b& 0xf];
        }
        return new String(resultCharArray);
    }


    /**
     * MD5方法
     *
     * @param text 明文
     * @param key 密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key){
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text + key);
        return encodeStr;
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text){
        //加密后的字符串
        String encodeStr= DigestUtils.md5Hex(text);
        return encodeStr;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key 密钥
     * @param md5 密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String key, String md5) throws Exception {
        //根据传入的密钥进行验证
        String md5Text = md5(text, key);
        if(md5Text.equalsIgnoreCase(md5))
        {
            System.out.println("MD5验证通过");
            return true;
        }

        return false;
    }


    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param md5 密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text,String md5) throws Exception {
        //根据传入的密钥进行验证
        String md5Text = md5(text);
        if(md5Text.equalsIgnoreCase(md5))
        {
            System.out.println("MD5验证通过");
            return true;
        }

        return false;
    }

    /**
     * 获取文件的md5
     * @param path 文件地址
     * @return
     */
    public static String fileMd5(String path)
    {
        String ctyptStr = "";
        return ctyptStr;
    }

    public static String stringSHA256(String input,String charset) {
        try {
            MessageDigest messageDigest =MessageDigest.getInstance("SHA-256");
            messageDigest.update(input.getBytes(charset));
            return byteArrayToHexLow(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOGGER.error("exceptiuon:{}",e);
            return null;
        }
    }

}
