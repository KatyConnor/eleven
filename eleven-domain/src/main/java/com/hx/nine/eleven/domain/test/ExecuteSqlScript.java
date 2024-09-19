package com.hx.nine.eleven.domain.test;

import org.apache.ibatis.jdbc.ScriptRunner;

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
public class ExecuteSqlScript {

    private static String userName = null;
    private static String password = null;
    private static String jdbcUrl = "";
    private static String jdbcDriver = "";

    public static void main(String[] args) {
        try {
            batchExecuteSql("D:\\yss-2022\\temp-T_DT_CASH.sql");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        File file = new File("D:\\yss-2022\\jdbc.properties");
        FileInputStream inStream = null;
        BufferedReader breader = null;
        try {
            inStream = new FileInputStream(file);
            breader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            String proper = null;
            while ((proper = breader.readLine()) != null) {
                String[] properties = proper.split("=");
                if ("userName".equals(properties[0].trim())){
                    userName = properties[1].trim();
                }
                if ("password".equals(properties[0].trim())){
                    password = properties[1].trim();
                }
                if ("jdbcUrl".equals(properties[0].trim())){
                    jdbcUrl = properties[1].trim();
                }
                if ("jdbcDriver".equals(properties[0].trim())){
                    jdbcDriver = properties[1].trim();
                }
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
     * SQL脚本分解执行
     * @param sqlFile SQL脚本资源
     * @throws SQLException
     * @throws IOException
     */
    public static void batchExecuteSql(String sqlFile) throws SQLException, IOException {
        Connection conn = null;
        Statement statement = null;
        FileInputStream inStream = null;
        BufferedReader breader = null;
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcUrl,userName,password);
            statement = conn.createStatement();
            File file = new File(sqlFile);
            inStream = new FileInputStream(file);
            breader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            String str = null;
            StringBuilder sql = new StringBuilder();
//            ScriptRunner scriptRunner = new ScriptRunner(conn);
//            scriptRunner.setSendFullScript(true);
//            InputStreamReader inputStreamReader = new InputStreamReader(inStream);
//            scriptRunner.runScript(inputStreamReader);
            int count = 1;
            while ((str = breader.readLine()) != null) {
                if (!"commit;".equals(str.trim()) && !str.trim().startsWith("set") && !str.trim().startsWith("prompt") && !"".equals(str.trim())){
                    sql.append(str).append(" ");
                    if (str.trim().endsWith(");")){
                        statement.addBatch(sql.toString());
                        System.out.println(sql.toString());
                        sql.delete(0, sql.length());
                        count++;
                    }
                }

                if (count % 100 == 0){
                    statement.executeBatch();
                    statement.clearBatch();
                    conn.commit();
                }
                if (str.trim().startsWith("prompt")){
                    System.out.println(str);
                }
            }
            statement.executeBatch();
            statement.clearBatch();
            conn.commit();
        }catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
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
    }

}
