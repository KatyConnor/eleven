package com.hx.nine.eleven.commons.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**   
 * @ClassName:  DatabaseUtil   
 * @Description:数据库直接连接，获取conn
 * @author: yangming 
 * @date:   2018年9月20日 下午4:55:05   
 *     
 * 
 */
public class DatabaseUtil {
	
	
	/**
	 *  DB2:
　　	 *  driverClassName:com.ibm.db2.jcc.DB2Driver
　　        *  url:jdbc:db2://localhost:50000/sample

　　        * 　　Oracle:
　　        * 　　driverClassName:oracle.jdbc.driver.OracleDriver
　　        * 　　url:jdbc:oracle:thin:@localhost:1521:orcl

　　        * 　　MySql:
　　        * 　　driverClassName:com.mysql.jdbc.Driver
　　        * 　　url:jdbc:mysql://localhost:3306/test
	 * 
	 * 
	 * 
	 */
	
	
	private static String DRIVERCLASSNAME = "oracle.jdbc.driver.OracleDriver";
	private static String URL = "jdbc:oracle:thin:@10.181.137.26:1521:assetrisk";
	private static String USERNAME = "cimstdp";
	private static String PWD = "cimstdp";
	

	/**   
	 * @Title: getConnection   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param: @return      
	 * @return: Connection      
	 * @throws   
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {

			Class.forName(DRIVERCLASSNAME);
			String url = URL;
			conn = DriverManager.getConnection(url, USERNAME,PWD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**   
	 * @Title: closeConnection   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param: @param conn      
	 * @return: void      
	 * @throws   
	 */
	public static void closeConnection(Connection conn) {
		
		
		
		
		
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
