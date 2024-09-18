package com.cqrcb.sql.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author wml
 * @Description
 * @data 2022-08-11
 */
public class SqlScriptTest {

    public static void main(String[] args) {
        File file = new File("D:\\yss-2022\\处理后SQL脚本\\ysstg-fq-table-data\\T_FA_BALANCE.sql");
        System.out.println((file.length() / 1024 ) / 1024);
    }

//    public static void main(String[] args) {
//        initProperties("D:\\yss-2022\\jdbc.properties");
//        System.out.println(">>>>>>>>>>>> 开始执行SQL文件：[] ");
//        Connection conn = null;
//        Statement statement = null;
//        FileInputStream inStream = null;
//        BufferedReader breader = null;
//        try {
//            Class.forName(jdbcDriver);
//            conn = DriverManager.getConnection(jdbcUrl, userName, password);
//            statement = conn.createStatement();
//            File file = new File("D:\\yss-2022\\处理后SQL脚本\\ysstg-fq-table-data\\T_DT_CASH.sql");
//            inStream = new FileInputStream(file);
//            breader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
//            String proper = null;
//            StringBuilder sql = new StringBuilder();
//            int count = 1;
//            while ((proper = breader.readLine()) != null) {
//                sql.append(proper);
//                if (proper.endsWith(");")){
//                    statement.addBatch(sql.toString());
//                    //清除StringBuilder中的SQL语句
//                    sql.delete(0, sql.length());
//                    count++;
//                }
//
//                if (count % 1 == 0) {
//                    System.out.println("每" + 100 + "条语句批量执行开始......");
////                    sql.append("begin\n");
////                    statement.addBatch("end;");
//                    statement.executeBatch();
////                    statement.clearBatch();
//                    System.out.println("每" + 100 + "条语句批量执行结束......");
//                }
//            }
//            if (count % 1 != 0) {
//                System.out.println("执行最后不足" + 100 + "条语句开始！！！");
////                statement.addBatch("end;");
//                statement.executeBatch();
////                statement.clearBatch();
//                System.out.println("执行最后不足" + 100 + "条语句结束！！！");
//            }
//        } catch (SQLException sqlEx) {
//            sqlEx.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException exception) {
//            exception.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (breader != null) {
//                try {
//                    breader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (inStream != null) {
//                try {
//                    inStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            }
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//            }
//        }
//        System.out.println(">>>>>>>>>>>> SQL文件：[] 执行完毕。");
//
//
//    }
}
