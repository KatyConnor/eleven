package com.cqrcb.sql.script.log;

import com.cqrcb.sql.script.util.DateUtil;
import com.cqrcb.sql.script.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 任务触发执行的日志存储在文件中
 * @author wml
 * @date 2016-3-12 19:25:12
 */
public class LogFileAppender {

	private static String logBasePath;

	/**
	 * 初始化日志目录
	 * @param logPath
	 */
	public static void initLogPath(String logPath){
		if (StringUtil.isNotBlank(logPath)) {
			logBasePath = logPath;
		}

		File logPathDir = new File(logBasePath);
		if (!logPathDir.exists()) {
			logPathDir.mkdirs();
		}
	}


	/**
	 * 按照日期生成日期日志目录和文件
	 *
	 * @param triggerDate  日期
	 * @param logName      日志名
	 * @return
	 */
	public static String makeLogFileName(Date triggerDate, String logName) {
		File logFilePath = new File(logBasePath, DateUtil.format(triggerDate,"yyyy-MM-dd"));
		if (!logFilePath.exists()) {
			logFilePath.mkdir();
		}

		String logFileName = logFilePath.getPath()
				.concat(File.separator)
				.concat(logName)
				.concat("-")
				.concat(DateUtil.format(triggerDate,"yyyy-MM-dd"))
				.concat(".log");
		return logFileName;
	}

	/**
	 * 文件追加日志
	 *
	 * @param logFileName
	 * @param appendLog
	 */
	public static void appendLog(String logFileName, String appendLog) {

		if (StringUtil.isBlank(logFileName)) {
			System.out.println(">>>>>>>>>>>> 日志文件或者目录为空，");
			return;
		}
		File logFile = new File(logFileName);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				System.out.println(">>>>>>>>>>>> 日志写入异常，"+e.getMessage());
				return;
			}
		}

		if (StringUtil.isBlank(appendLog)) {
			appendLog = "";
		}
		appendLog += "\r\n";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(logFile, true);
			fos.write(appendLog.getBytes("utf-8"));
			fos.flush();
		} catch (Exception e) {
			System.out.println(">>>>>>>>>>>> 日志写入异常，"+e.getMessage());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					System.out.println(">>>>>>>>>>>> 关闭文件流异常，"+e.getMessage());
				}
			}
		}
		
	}

	/**
	 * 读取日志文件
	 *
	 * @param logFileName
	 * @return log content
	 */
//	public static LogResult readLog(String logFileName, int fromLineNum){
//
//		if (logFileName==null || logFileName.trim().length()==0) {
//            return new LogResult(fromLineNum, 0, "readLog fail, logFile not found", true);
//		}
//		File logFile = new File(logFileName);
//		if (!logFile.exists()) {
//            return new LogResult(fromLineNum, 0, "readLog fail, logFile not exists", true);
//		}
//
//		StringBuffer logContentBuffer = new StringBuffer();
//		int toLineNum = 0;
//		LineNumberReader reader = null;
//		try {
//			reader = new LineNumberReader(new InputStreamReader(new FileInputStream(logFile), "utf-8"));
//			String line = null;
//
//			while ((line = reader.readLine())!=null) {
//				toLineNum = reader.getLineNumber();
//				if (toLineNum >= fromLineNum) {
//					logContentBuffer.append(line).append("\n");
//				}
//			}
//		} catch (IOException e) {
//			LOGGER.error(e.getMessage(), e);
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e) {
//					LOGGER.error(e.getMessage(), e);
//				}
//			}
//		}
//
//		// result
//		LogResult logResult = new LogResult(fromLineNum, toLineNum, logContentBuffer.toString(), false);
//		return logResult;
//
//		/*
//        // it will return the number of characters actually skipped
//        reader.skip(Long.MAX_VALUE);
//        int maxLineNum = reader.getLineNumber();
//        maxLineNum++;	// 最大行号
//        */
//	}
//
//	/**
//	 * 一行一行读取
//	 * @param logFile
//	 * @return log line content
//	 */
//	public static String readLines(File logFile){
//		BufferedReader reader = null;
//		try {
//			reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), "utf-8"));
//			if (reader != null) {
//				StringBuilder sb = new StringBuilder();
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					sb.append(line).append("\n");
//				}
//				return sb.toString();
//			}
//		} catch (IOException e) {
//			LOGGER.error(e.getMessage(), e);
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e) {
//					LOGGER.error(e.getMessage(), e);
//				}
//			}
//		}
//		return null;
//	}

}
