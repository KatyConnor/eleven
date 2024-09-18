package com.cqrcb.sql.script.log;

import com.cqrcb.sql.script.util.DateUtil;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * @author wml
 * @Description
 * @data 2022-08-02
 */
public class LogFactory {

    private static String logFileName;

    public static void setLogFileName(String logName){
        logFileName = logName;
    }

    /**
     * 写入日志
     * @param appendLogPattern
     * @param appendLogArguments
     * @return
     */
    public static boolean log(String appendLogPattern, Object... appendLogArguments) {
        FormattingTuple ft = MessageFormatter.arrayFormat(appendLogPattern, appendLogArguments);
        String appendLog = ft.getMessage();
        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        return logDetail(callInfo, appendLog);
    }

    /**
     * append exception stack
     *
     * @param e
     */
    public static boolean log(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String appendLog = stringWriter.toString();

        StackTraceElement callInfo = new Throwable().getStackTrace()[1];
        return logDetail(callInfo, appendLog);
    }

    /**
     * append log
     *
     * @param callInfo
     * @param appendLog
     */
    private static boolean logDetail(StackTraceElement callInfo, String appendLog) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(DateUtil.formatDateTime(new Date())).append(" ")
                .append("["+ callInfo.getClassName() + "#" + callInfo.getMethodName() +"]").append("-")
                .append("["+ callInfo.getLineNumber() +"]").append("-")
                .append("["+ Thread.currentThread().getName() +"]").append(" ")
                .append(appendLog!=null?appendLog:"");
        String formatAppendLog = stringBuffer.toString();
        // appendlog
        if (logFileName!=null && logFileName.trim().length()>0) {
            LogFileAppender.appendLog(logFileName, formatAppendLog);
            return true;
        } else {
            System.out.println(">>>>>>>>>>>> 日志写入异常，"+formatAppendLog);
            return false;
        }
    }


}
