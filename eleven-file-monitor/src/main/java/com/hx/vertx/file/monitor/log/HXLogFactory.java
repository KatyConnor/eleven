package com.hx.vertx.file.monitor.log;

import com.hx.vertx.file.monitor.utils.DateUtil;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.*;
import java.util.Date;

/**
 *
 */
public class HXLogFactory {
    public static boolean info(String appendLogPatter, Object... appendLogArguments) {
        return log("[INFO] " + appendLogPatter,null, appendLogArguments);
    }

    public static boolean debug(String appendLogPatter, Object... appendLogArguments) {
        return log("[DEBUG] " + appendLogPatter,null, appendLogArguments);
    }

    public static boolean warn(String appendLogPatter, Object... appendLogArguments) {
        return log("[WARN] " + appendLogPatter, null,appendLogArguments);
    }

    public static boolean error(String appendLogPatter, Object... appendLogArguments) {
        return log("[ERROR] " + appendLogPatter,null, appendLogArguments);
    }

    public static boolean error(String appendLogPatter,Throwable e, Object... appendLogArguments) {
        return log("[ERROR] " + appendLogPatter,e, appendLogArguments);
    }

    public static boolean error(Throwable e) {
        return log(e);
    }

    public static boolean log(String appendLogPatter,Throwable e, Object... appendLogArguments) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat(appendLogPatter, appendLogArguments);
        String appendLog = formattingTuple.getMessage();
        StackTraceElement element = e==null?new Throwable().getStackTrace()[1]:e.getStackTrace()[1];
        return logDetail(element, appendLog);
    }

    public static boolean log(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.append("[ERROR] ");
        e.printStackTrace(new PrintWriter(stringWriter));
        StackTraceElement element = e.getStackTrace()[1];
        return logDetail(element, stringWriter.toString());
    }

    public static boolean logDetail(StackTraceElement element, String appendLog) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtil.formatDateTime(new Date())).append(" ")
                .append("[ " + element.getClassName() + "#" + element.getMethodName() + "]").append("_")
                .append("[" + element.getLineNumber() + "]").append("_")
                .append("[" + Thread.currentThread().getName() + "]").append(" ")
                .append(appendLog != null ? appendLog : "");
        String formatAppendLog = stringBuilder.toString();
        String logFileName = HXLogFileAppender.getLogFile();
        if (logFileName != null && !logFileName.isEmpty()) {
            HXLogFileAppender.appendLog(logFileName, formatAppendLog);
        } else {
            return false;
        }
        return true;
    }
}
