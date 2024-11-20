package hx.nine.eleven.jdbc;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 用于事务管理中根据事务上下文获取数据源，再获取数据库连接
 * @auth wml
 * @date 2024/9/26
 */
public interface ElevenDataSource {

	/**
	 *  获取当前连接，
	 * 	1、首先从缓存池中获取当前线程的数据库连接池
	 * 	2、如果没获取到，重新重数据库连接池中获取，便于使用完毕之后好关闭连接释放资源
	 * @param dataSource
	 * @param dataSourceName
	 * @return
	 */
	Connection currentDataSourceConnection(DataSource dataSource,String dataSourceName)throws SQLException;

	/**
	 * 获取连接
	 *
	 * @param dataSource 数据源
	 * @param dataSource 数据源名称
	 * @param username   用户名
	 * @param password   连接秘密
	 * @return
	 * @throws SQLException
	 */
	Connection currentDataSourceConnection(DataSource dataSource,String dataSourceName,String username, String password) throws SQLException;

	<T> T unwrap(Class<T> iface) throws SQLException;

	boolean isWrapperFor(Class<?> iface) throws SQLException;

	PrintWriter getLogWriter() throws SQLException;

	void setLogWriter(PrintWriter out) throws SQLException;

	void setLoginTimeout(int seconds) throws SQLException;

	int getLoginTimeout() throws SQLException;

	Logger getParentLogger() throws SQLFeatureNotSupportedException;
}
