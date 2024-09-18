package com.cqrcb.sql.script;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.cqrcb.sql.script.log.LogFileAppender;
import com.cqrcb.sql.script.log.LogFactory;
import com.cqrcb.sql.script.util.FileUtil;
import com.cqrcb.sql.script.util.SqlFileAnalysisUtil;
import org.apache.ibatis.jdbc.ScriptRunner;

/**
 * @author wml
 * @Description
 * @data 2022-08-11
 */
public class ExecuteSqlScript {

    private static String userName = null;
    private static String password = null;
    private static String jdbcUrl = null;
    private static String jdbcDriver = null;
    private static String logPath = null;
    private static String sqlFile = null;
    private static String charSet = "UTF-8";
    private static String logName = null;
    private static String logFileName = null;
    private static boolean sendFullScript = true;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            if (args != null && args.length >= 0) {
                // 初始化配置文件
                initProperties(args[0]);
                // 初始化日志
                LogFileAppender.initLogPath(logPath);
                logFileName = LogFileAppender.makeLogFileName(new Date(), logName);
                LogFactory.setLogFileName(logFileName);
                LogFactory.log("开始执行SQL文件导入");
                // 检查SQL文件
                List<String> sqlFileList = checkSqlFile();
                if (sqlFileList.size() <= 0) {
                    batchExecuteSql(sqlFile);
                } else {
                    // 获取SQL文件路径
                    String sqlFileDir = FileUtil.getParentDir(sqlFile);
                    Iterator iter = sqlFileList.iterator();
                    while (iter.hasNext()) {
                        String fileName = sqlFileDir + iter.next();
                        // 检查文件大小，如果文件过大进行拆分，默认拆分成30M左右大小的文件
                        List<String> checkFiles = SqlFileAnalysisUtil.checkFileSize(fileName,charSet);
                        // 执行文件
                        if (checkFiles != null && checkFiles.size() > 0) {
                            Iterator iterTemp = checkFiles.iterator();
                            while (iterTemp.hasNext()) {
                                String nextFile = String.valueOf(iterTemp.next());
                                System.out.println(">>>>>>>>>>>> 分批次执行大文件 ： " + nextFile);
                                LogFactory.log(">>>>>>>>>>>> 分批次执行大文件",nextFile);
                                batchExecuteSql(nextFile);
                            }
                        } else {
                            batchExecuteSql(fileName);
                        }
                    }
                }
            } else {
                System.out.println(">>>>>>>>>>>> 请指定数据库连接配置文件：arg[0]，执行SQL脚本文件，arg[1]");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>> 执行耗时："+(((endTime - startTime) / 1000) / 60) + " 分");
    }

    /**
     * 初始化数据库配置
     *
     * @param propertiesFile
     */
    private static void initProperties(String propertiesFile) {
        System.out.println(">>>>>>>>>>>> 加载数据库配置信息");
        File file = new File(propertiesFile);
        FileInputStream inStream = null;
        BufferedReader breader = null;
        try {
            inStream = new FileInputStream(file);
            breader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            String proper = null;
            while ((proper = breader.readLine()) != null) {
                String[] properties = proper.split("=");
                if ("userName".equals(properties[0].trim())) {
                    userName = properties[1].trim();
                }
                if ("password".equals(properties[0].trim())) {
                    password = properties[1].trim();
                }
                if ("jdbcUrl".equals(properties[0].trim())) {
                    jdbcUrl = properties[1].trim();
                }
                if ("jdbcDriver".equals(properties[0].trim())) {
                    jdbcDriver = properties[1].trim();
                }
                if ("logPath".equals(properties[0].trim())) {
                    logPath = properties[1].trim();
                }
                if ("sqlFile".equals(properties[0].trim())) {
                    sqlFile = properties[1].trim();
                }
                if ("charSet".equals(properties[0].trim())) {
                    charSet = properties[1].trim();
                }
                if ("logName".equals(properties[0].trim())) {
                    logName = properties[1].trim();
                }
                if("sendFullScript".equals(properties[0].trim())){
                    sendFullScript =  Boolean.valueOf(properties[1].trim());
                }
            }
            System.out.println(">>>>>>>>>>>> 数据库配置信息：jdbcDriver=" + jdbcDriver + ",jdbcUrl=" + jdbcUrl + ",userName=" + userName + ",password=" + password);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检查SQL文件是否是批量文件，如果是批量文件，一次按照顺序执行，如果是单个文件，直接执行
     */
    private static List<String> checkSqlFile() {
        FileInputStream inStream = null;
        BufferedReader breader = null;
        List<String> sqlFileList = new ArrayList<>();
        File file = new File(sqlFile);
        try {
            inStream = new FileInputStream(file);
            breader = new BufferedReader(new InputStreamReader(inStream, charSet));
            String str = null;
            while ((str = breader.readLine()) != null) {
                if (str.trim().startsWith("@@")) {
                    sqlFileList.add(str.substring(2));
                }
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlFileList;
    }

    /**
     * SQL脚本执行
     *
     * @param sqlFile SQL脚本资源
     * @throws SQLException
     * @throws IOException
     */
    private static void batchExecuteSql(String sqlFile) throws SQLException, IOException {
        System.out.println(">>>>>>>>>>>> 开始执行SQL文件：[" + sqlFile + "] ");
        Connection conn = null;
        Statement statement = null;
        FileInputStream inStream = null;
        BufferedReader breader = null;
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcUrl, userName, password);
            statement = conn.createStatement();
            File file = new File(sqlFile);
            inStream = new FileInputStream(file);
            ScriptRunner scriptRunner = new ScriptRunner(conn);
            scriptRunner.setSendFullScript(sendFullScript);
            InputStreamReader inputStreamReader = new InputStreamReader(inStream, "UTF-8");
            scriptRunner.runScript(inputStreamReader);
//            conn.commit();
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (breader != null) {
                breader.close();
            }

            if (inStream != null) {
                inStream.close();
            }

            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        System.out.println(">>>>>>>>>>>> SQL文件：[" + sqlFile + "] 执行完毕。");
    }

}
