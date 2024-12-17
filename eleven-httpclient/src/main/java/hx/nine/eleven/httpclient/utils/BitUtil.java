package hx.nine.eleven.httpclient.utils;

/**
 * @author wml
 * 2020-06-05
 */
public class BitUtil {
    public BitUtil() {
    }

    /**
     * byte转为String
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer ret = new StringBuffer("");

        for (int i = 0; i < b.length; ++i) {
            String hex = Integer.toHexString(b[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            ret.append(hex);
            if (i != 0 && (i + 1) % 4 == 0) {
                ret.append(" ");
            }
        }

        return ret.toString();
    }

    /**
     * byte转为int
     * @param src
     * @param index
     * @return
     */
    public static int byte2Int2(byte[] src, int index) {
        int value = (src[index * 4] & 255) << 24 | (src[index * 4 + 1] & 255) << 16 | (src[index * 4 + 2] & 255) << 8 | src[index * 4 + 3] & 255;
        return value;
    }

    /**
     * int转为byte
     * @param num
     * @param b
     * @param index
     */
    public static void ints2Bytes(int num, byte[] b, int index) {
        for (int i = 0; i < 4; ++i) {
            b[index * 4 + 4 - i - 1] = (byte) (num >> 8 * i & 255);
        }

    }

    /**
     * long 转为 byte
     * @param num
     * @return
     */
    public static byte[] long2bytes(long num) {
        byte[] b = new byte[8];

        for (int i = 0; i < 8; ++i) {
            b[i] = (byte) ((int) (num >>> 56 - i * 8));
        }

        return b;
    }
}
