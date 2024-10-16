package com.hx.eleven.ftpserver.db;

//import org.hsqldb.server.Server;

import org.hsqldb.Server;

/**
 * hsqldb 内嵌服务启动类
 * @auth wml
 * @date 2024/10/16
 */
public class HSQLServer {

	public static void main(String[] args) {
		// 启动HSQLDB服务器
		Server server = new Server();
		try {
			server.setDatabaseName(0, "ftpDB");
			server.setDatabasePath(0, "file:ftpDB");
			server.setPort(9001);
			server.setNoSystemExit(true);
			server.start();
		} catch (Exception e) {
			// hsqldb 数据库启动失败
		}
	}

}
