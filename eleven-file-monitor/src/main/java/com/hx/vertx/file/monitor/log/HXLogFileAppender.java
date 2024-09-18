package com.hx.vertx.file.monitor.log;

import com.hx.vertx.file.monitor.utils.DateUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class HXLogFileAppender {
    private static String logBasePath;
    private static String logFile;

    public static String getLogFile(){return logFile;}

    /**
     * 初始化日志目录
     * @param logPath
     */
    public static void initLogPath(String logPath){
        if (logPath == null && logPath.isEmpty()){
           return;
        }
        logBasePath = logPath;
        File logPathDir = new File(logBasePath);
        if (!logPathDir.exists()){
            logPathDir.mkdirs();
        }

        logBasePath = logPathDir.getPath();
    }

    public static File getLogPath(){return new File(logBasePath);}

    public static String makeLogFileName(Date triggerDate,long logId){
        File logFilePath = new File(getLogPath(), DateUtil.formatDate(triggerDate));
        if (!logFilePath.exists()){
            logFilePath.mkdir();
        }
        String logFileName = logFilePath.getPath().concat(File.separator).concat(DateUtil.formatDate(triggerDate))
                .concat("_").concat(String.valueOf(logId)).concat(".log");
        logFile = logFileName;
        return logFile;
    }

    public static void appendLog(String logFileName,String appendLog){
        if (logFileName == null || logFileName.isEmpty()){
            return;
        }

        File logFile = new File(logFileName);
        if (!logFile.exists()){
            try {
                logFile.createNewFile();
            }catch (IOException ioEx){
                //
            }
        }

        if (appendLog == null){
            appendLog = "";
        }
        appendLog += "\r\n";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile,true);
            fos.write(appendLog.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        }catch (Exception e){

        }finally {
            if (fos != null){
                try {
                    fos.close();
                }catch (IOException e){

                }
            }
        }
    }

    public static String readLine(File logFile){
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8));
            if (reader != null){
                StringBuilder readStr = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
                    readStr.append(line).append("\n");
                }
                return readStr.toString();
            }
        }catch (IOException e){

        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException ex){

                }
            }
        }
        return null;
    }
}
