package com.cqrcb.sql.script.util;

import com.cqrcb.sql.script.log.LogFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wml
 * @Description
 * @data 2022-08-12
 */
public class SqlFileAnalysisUtil {

    public static List<String> checkFileSize(String sqlFilePath,String charSet) {
        File file = new File(sqlFilePath);
        long size = (file.length() / 1024) / 1024; // 单位: M
        List<String> tempFiles = null;
        if (size > 6) {
            System.out.println(">>>>>>>>>>>> [" + sqlFilePath + "] 文件太大,拆分成小文件执行, 文件大小：size: "+size+" M");
            LogFactory.log(">>>>>>>>>>>> [{}] 文件太大,拆分成小文件执行",sqlFilePath);
            String tempDir = mkTempDir(sqlFilePath);
            tempFiles = new ArrayList<>();
            BufferedReader breader = FileUtil.read(file,charSet);
            try {
                String proper = null;
                int temp = 1;
                int count = 1;
                System.out.println(">>>>>>>>>>>> 读取文件 ： " + sqlFilePath);
                LogFactory.log(">>>>>>>>>>>> 读取文件,[{}]",sqlFilePath);
                File tempSqlFile = null;
                BufferedWriter bwr = null;
                while ((proper = breader.readLine()) != null) {
                    if (tempSqlFile == null){
                        String temPFileName = getTempSqlFile(tempDir,sqlFilePath,temp);
                        tempSqlFile = new File(temPFileName);
                        tempFiles.add(temPFileName);
                        System.out.println(">>>>>>>>>>>> 生成临时文件 ： " + tempSqlFile);
                    }

                    if (bwr == null){
                        bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempSqlFile), charSet));
                        bwr.write("begin");
                        bwr.newLine();
                    }
                    if (proper.trim().startsWith("prompt") || proper.trim().startsWith("begin") || proper.trim().startsWith("end")){
                        System.out.println(">>>>>>>>>>>> 内容存在语法错误，不写入执行 ： "+ proper.trim() );
                        continue;
                    }
                    bwr.write(proper);
                    bwr.newLine();

                    if (proper.endsWith(");")) {
                        count++;
                        // 每100 行进行一次文件大小判断
                        if (count % 50 == 0) {
                            long fileSize = (tempSqlFile.length()/ 1024) / 1024;
                            if (fileSize > 5) {
                                System.out.println(">>>>>>>>>>>> 文件大小 ："+ fileSize + " M");
                                StringBuilder endStr = new StringBuilder();
                                endStr.append("end;\r\n");
                                bwr.write(endStr.toString());
                                bwr.newLine();
                                bwr.flush();
                                bwr.close();
                                bwr = null;
                                tempSqlFile = null;
                                temp++;
                            }
                        }
                    }
                }
                if(count % 50 != 0){
                    System.out.println(">>>>>>>>>>>> 文件大小 ："+ ((tempSqlFile.length()/ 1024) / 1024) + " M");
                    StringBuilder endStr = new StringBuilder();
                    endStr.append("end;\r\n");
                    bwr.write(endStr.toString());
                    bwr.newLine();
                    bwr.flush();
                    bwr.close();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (breader != null) {
                    try {
                        breader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return tempFiles;
    }

    private static String mkTempDir(String sqlFilePath) {
        return FileUtil.mkdir("temp",sqlFilePath);
    }

    private static String getTempSqlFile(String tempDir,String sqlParentFile,int tempcount){
        String osName = System.getProperty("os.name");
        StringBuilder temPFileName = new StringBuilder();
        if (osName.startsWith("win") || osName.startsWith("Win") || osName.startsWith("WIN")) {
            String sqlTemp = sqlParentFile.substring(sqlParentFile.lastIndexOf("\\") + 1, sqlParentFile.lastIndexOf("."));
            temPFileName.append(tempDir).append("\\").append(sqlTemp).append("_").append(tempcount).append(".sql");
           return temPFileName.toString();
        } else {
            String sqlTemp = sqlParentFile.substring(sqlParentFile.lastIndexOf("/") + 1, sqlParentFile.lastIndexOf("."));
            temPFileName.append(tempDir).append("/").append(sqlTemp).append("_").append(tempcount).append(".sql");
            return temPFileName.toString();
        }
    }
}
