package hx.nine.eleven.sftserver;

import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class Test {

    private static SFTPUtil sftpUtil = new SFTPUtil();

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
                stringBuilder.append(line).append("\n");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        if(stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '\n'){
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws IOException, SftpException {
        boolean connected = sftpUtil.isConnected();
        if (!connected) {
            System.out.println("连接SFTP");
            // 折里可以采用配置文件 @Value 注解
            connected = sftpUtil.login("127.0.0.1", 2990, "nfms", "nfms");
        }
        if (connected) {
            System.out.println("连接成功");
            System.out.println(sftpUtil.getSFTPHost() + " : " + sftpUtil.getSFTPPort());
//            InputStream inputStream = sftpUtil.getFile("/rjw/wind.txt");
//            String s = convertInputStreamToString(inputStream);
//            System.out.println(s);
//            File file = new File("C:\\Users\\wen\\Desktop\\sun.txt");
//            InputStream inputStream1 = Files.newInputStream(file.toPath());
//            boolean dir = sftpUtil.upLoadFile(inputStream1, "/rjw", "big.txt");
//            System.out.println("添加文件成功： " + dir);
            sftpUtil.logout();
        }
    }
}
