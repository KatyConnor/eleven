package com.hx.nine.eleven.doop.jdbc;

import com.hx.nine.eleven.doop.PageParam;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @auth wml
 * @date 2024/3/22
 */
public interface SqlSession extends Closeable {

	/**
	 * 根据主键查询单条记录
	 * @param <R>
	 * @return
	 */
	<R> R selectOne();

	/**
	 * 根据查询条件匹配单条记录
	 * @param param  查询参数
	 * @param <R>    返回结果集
	 * @param <P>    参数对象泛型类型
	 * @return
	 */
	<R,P> R selectOne(P param);

	/**
	 * 根据查询条件匹配多条记录
	 * @param param  查询参数
	 * @param <R>    返回结果集
	 * @param <P>    参数对象泛型类型
	 * @return
	 */
	<R,P> List<R> selectList(P param);

	/**
	 * 分页查询数据
	 * @param param  查询参数
	 * @param <R>    返回结果集
	 * @param <P>    参数对象泛型类型
	 * @return
	 */
	<R,P extends PageParam> List<R> selectPage(P param);

	<R,P> Cursor<R> selectCursor(P param) throws SQLException;

	/**
	 * 插入一条数据
	 * @param po   table数据映射对象
	 * @param <T>  table数据对象泛型类型
	 * @return
	 * @throws SQLException
	 */
	<T> int insertOne(T po) throws SQLException;

	/**
	 *  批量插入数据
	 * @param poList  table数据映射对象集合
	 * @param <T>     table数据对象泛型类型
	 * @return
	 * @throws SQLException
	 */
	<T> int batchInsert(List<T> poList) throws SQLException;

	/**
	 * 根据主键修改单条记录
	 * @param po   table数据映射对象
	 * @param <T>  table数据对象泛型类型
	 * @return
	 * @throws SQLException
	 */
	<T> int update(T po) throws SQLException;

	/**
	 *  根据主键批量修改数据
	 * @param poList  table数据映射对象集合
	 * @param <T>     table数据对象泛型类型
	 * @return
	 * @throws SQLException
	 */
	<T> int update(List<T>  poList) throws SQLException;

	/**
	 * 根据条件进行数据修改，可修改满足条件的多条数据
	 * @param po     table数据映射对象
	 * @param param  参数对象
	 * @param <T>    table数据对象泛型类型
	 * @param <P>    参数对象泛型类型
	 * @return
	 * @throws SQLException
	 */
	<T,P> int executeUpdate(T  po,P param) throws SQLException;

	/**
	 * 进行数据对比和合并
	 * @param poList table数据映射对象集合
	 * @param param  参数对象
	 * @param <T>    table数据对象泛型类型
	 * @param <P>    参数对象泛型类型
	 * @return
	 */
	<T,P> boolean merge(List<T> poList,P param);

	/**
	 * 根据主键删除数据
	 * @return
	 */
	int delete();

	/**
	 * 根据匹配条件批量删除数据
	 * @param param  参数对象
	 * @param <P>
	 * @return
	 */
	<P> int delete(P param);

	/**
	 * 删除表
	 * @param name 表名
	 * @return
	 */
	boolean drop(String name);

	<T> Cursor<T> selectCursor(String statement);

	<T> Cursor<T> selectCursor(String statement, Object parameter);

	<T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds);

	/**
	 * 提交事务
	 */
	void commit();

	/**
	 * 提交事务
	 * @param force
	 */
	void commit(boolean force);

	/**
	 * 回滚事务
	 */
	void rollback();

	/**
	 * 回滚事务
	 * @param force
	 */
	void rollback(boolean force);

	/**
	 * 数据库连接
	 * @return
	 */
	Connection getConnection();

	@Override
	void close();


}
