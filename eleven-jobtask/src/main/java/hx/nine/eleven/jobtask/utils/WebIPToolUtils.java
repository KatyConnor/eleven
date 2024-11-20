package hx.nine.eleven.jobtask.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP地址获取
 * @author wml
 * 2022-05-05
 */
public class WebIPToolUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebIPToolUtils.class);

    private static final String SYSTEM_NAME= "os.name";
    private static final String WINDOWS= "windows";
    private static final String DOCKER= "docker";
    private static final String OTHER= "lo";




    /**
     * 获取本地IP地址
     *
     * @throws Exception
     */
    public static String getLocalIP() throws Exception {
        if (isWindowsOS()) {
            return InetAddress.getLocalHost().getHostAddress();
        } else {
            return getLinuxLocalIp();
        }
    }


    /**
     * 获取Linux 服务器IP地址
     *
     * @return IP地址
     * @throws Exception
     */
    private static String getLinuxLocalIp(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains(DOCKER) && !name.contains(OTHER)) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                LOGGER.info("this mch IP is [{}]",ipaddress);
                                return ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("have get mch ip exception,{}",ex);
        }
        return null;
    }


    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty(SYSTEM_NAME);
        LOGGER.info("sys name is [{}]",osName);
        if (osName.toLowerCase().indexOf(WINDOWS) > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(getLocalIP());
    }
}
