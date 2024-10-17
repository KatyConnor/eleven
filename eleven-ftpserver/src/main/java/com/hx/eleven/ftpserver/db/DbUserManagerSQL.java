package com.hx.eleven.ftpserver.db;

import java.util.HashMap;
import java.util.Map;

/**
 * @auth wml
 * @date 2024/10/17
 */
public interface DbUserManagerSQL {

	String column = "userid, user_password, home_directory,enable_flag, write_permission, idle_time, upload_rate, download_rate,max_current_login_number, max_current_login_per_ip";
	String insertColumn = "userid, user_password, home_directory,enable_flag, write_permission, idle_time, upload_rate,download_rate,max_current_login_number, max_current_login_per_ip";
	String insertUserStmt = "INSERT INTO FTP_USER ("+insertColumn+") VALUES (?,?,?,?,?,?,?,?,?,?)";
	String updateUserStmt = "UPDATE FTP_USER SET user_password=?,home_directory=?,enable_flag=?,write_permission=?,idle_time=?,upload_rate=?,download_rate=? WHERE userid=?";
	String deleteUserStmt = "DELETE FROM FTP_USER WHERE userid = ?";
	String selectUserStmt = "SELECT "+column+" FROM FTP_USER WHERE userid = ?";
	String selectAllStmt = "SELECT userid FROM FTP_USER ORDER BY userid";
//	String isAdminStmt = "SELECT userid FROM FTP_USER WHERE userid='admin'";
	String authenticateStmt = "SELECT user_password from FTP_USER WHERE userid=?";




}
