package com.hx.eleven.ftpserver.db;

//import org.hsqldb.server.Server;

import org.hsqldb.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hsqldb 内嵌服务启动类
 * @auth wml
 * @date 2024/10/16
 */
public class HSQLServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(HSQLServer.class);

	public static void runStart(String[] args) {
		// 启动HSQLDB服务器
		Server server = new Server();
		try {
			server.setDatabaseName(0, "ftpDB");
			server.setDatabasePath(0, "file:ftpDB");
			server.setPort(9008);
//			server.setNoSystemExit(true);
			server.start();
		} catch (Exception e) {
			// hsqldb 数据库启动失败
			LOGGER.error("启动内嵌hsqlDB失败",e);
		}
	}

}
