package com.hx.lang.commons.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author wml
 * @Description
 * @data 2022-05-20
 */
public class FileUtil {

    /**
     * 文件内容 Unicode 转 中文
     * @param path
     */
    public static void unicodeCH(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0,path.lastIndexOf("/")+1)+"temp"+path.substring(path.lastIndexOf("/")+1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        while ((str = br.readLine()) != null) {
            String result = StringUtils.unicodeToCH(str);
            bwr.write(result);
            bwr.newLine();
        }
        bwr.flush();
        bwr.close();
        br.close();
        // 临时文件写入源文件
        File fileread = new File(tempPath);
        File filewrite = new File(path);
        BufferedReader bread = new BufferedReader(new InputStreamReader(new FileInputStream(fileread), "UTF-8"));
        BufferedWriter bwrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filewrite)));
        String str1 = null;
        while ((str1 = bread.readLine()) != null) {
            bwrite.write(str1);
            bwrite.newLine();
        }
        bwrite.flush();
        bwrite.close();
        bread.close();
        fileTemp.delete();
        fileread.delete();
    }
}
