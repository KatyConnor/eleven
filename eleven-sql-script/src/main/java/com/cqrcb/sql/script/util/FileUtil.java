package com.cqrcb.sql.script.util;

import com.cqrcb.sql.script.log.LogFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author wml
 * @Description
 * @data 2022-08-12
 */
public class FileUtil {

    /**
     * 读取文件，返回BufferedReader 流对象
     *
     * @param file
     * @param charsetName
     * @return
     */
    public static BufferedReader read(File file, String charsetName) {
        FileInputStream inStream = null;
        BufferedReader breader = null;
        try {
            inStream = new FileInputStream(file);
            breader = new BufferedReader(new InputStreamReader(inStream, charsetName));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return breader;
    }

    /**
     * 读取文件，返回BufferedReader 流对象
     *
     * @param filePath
     * @param charsetName
     * @return
     */
    public static BufferedReader read(String filePath, String charsetName) {
        File file = new File(filePath);
        FileInputStream inStream = null;
        BufferedReader breader = null;
        try {
            inStream = new FileInputStream(file);
            breader = new BufferedReader(new InputStreamReader(inStream, charsetName));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return breader;
    }

    /**
     * 文件写入
     *
     * @param
     * @param charsetName
     * @return
     */
    public static void write(String fileName,String appendStr, String charsetName) {
        File file = new File(fileName);
        try {
            BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));
            System.out.println(">>>>>>>>>>>> 生成临时文件,fileName ： " + fileName);
            LogFactory.log(">>>>>>>>>>>> 生成临时文件按,fileName={}",fileName);
            bwr.write(appendStr);
            bwr.flush();
            bwr.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedWriter getBufferedReader(String fileName, String charsetName) {
        File file = new File(fileName);
        BufferedWriter bwr = null;
        try {
            bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charsetName));
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bwr;
    }

    public static String getParentDir(String fileName){
        String osName = System.getProperty("os.name");
        System.out.println(">>>>>>>>>>>> 当前操作系统：osName="+osName);
        if (osName.startsWith("win") || osName.startsWith("Win") || osName.startsWith("WIN")) {
            return fileName.substring(0, fileName.lastIndexOf("\\")) + "\\";
        } else {
            return fileName.substring(0, fileName.lastIndexOf("/")) + "/";
        }
    }

    public static String mkdir(String fileName,String parentFile){
        String osName = System.getProperty("os.name");
        String temPFileName = null;
        if (osName.startsWith("win") || osName.startsWith("Win") || osName.startsWith("WIN")) {
            temPFileName = parentFile.substring(0, parentFile.lastIndexOf("\\")) + "\\"+fileName;
            File tempFile = new File(temPFileName);
            if (!tempFile.exists()) {
                System.out.println(">>>>>>>>>>>>  创建临时目录： " + temPFileName);
                LogFactory.log(">>>>>>>>>>>> 创建临时目录,[{}] ",temPFileName);
                tempFile.mkdir();
            }
        } else {
            temPFileName = parentFile.substring(0, parentFile.lastIndexOf("/")) + "/"+fileName;
            File tempFile = new File(temPFileName);
            if (!tempFile.exists()) {
                System.out.println(">>>>>>>>>>>> 创建临时目录 ： " + temPFileName);
                LogFactory.log(">>>>>>>>>>>> 创建临时目录,[{}] ",temPFileName);
                tempFile.mkdir();
            }
        }
        return temPFileName;
    }
}
