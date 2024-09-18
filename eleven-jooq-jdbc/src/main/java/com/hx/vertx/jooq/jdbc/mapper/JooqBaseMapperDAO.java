package com.hx.vertx.jooq.jdbc.mapper;

import com.hx.vertx.jooq.jdbc.page.PageResult;
import org.jooq.*;

import java.util.List;
import java.util.Optional;

/**
 * JOOQ Mapper DAO  通用接口，扩展分页查询
 * @param <R>
 * @param <P>
 * @param <T>
 */
public interface JooqBaseMapperDAO<R extends UpdatableRecord<R>, P, T> extends DAO<R, P, T> {

	/**
	 * 获取 DSLContext
	 *
	 * @return DSLContext
	 */
	DSLContext getDSLContext();

	/**
	 * 插入一条数据，插入数据对象属性值和表column必输保持一致
	 * @param po
	 * @return
	 */
	int insertColumnPO(P po);

	/**
	 * 插入一条数据，根据table column删选出满足条件的value进行insert操作
	 * @param po
	 * @return
	 */
	int insertPO(P po);

	/**
	 * 插入一条数据，根据table column删选出满足条件的value进行insert操作
	 * insert完毕立即提交数据
	 * @param po
	 * @return
	 */
	int transactionInsertPO(P po);

	/**
	 * 批量插入数据
	 * @param po
	 * @return
	 */
	int batchInsert(List<P> po);

	/**
	 * 批量插入数据，insert完毕立即提交数据
	 * @param po
	 * @return
	 */
	int transactionBatchInsert(List<P> po);

	/**
	 * 根据条件修改某条记录
	 * @param po
	 * @param condition
	 * @return
	 */
	int updateOnePO(P po,Condition condition);

	/**
	 * 根据主键和version字段修改数据
	 * @param po
	 * @return
	 */
	int updateOnePO(P po);

	/**
	 * 根据主键和version字段修改数据
	 * @param po
	 * @return
	 */
	int batchUpdate(List<P> po);

	/**
	 * 根据条件修改某条记录，update完毕立即提交数据
	 * @param po
	 * @param condition
	 * @return
	 */
	int batchUpdate(List<P> po,Condition condition);

	/**
	 * 根据条件修改某条记录，update完毕立即提交数据
	 * @param po
	 * @param condition
	 * @return
	 */
	int transactionUpdateOnePO(P po,Condition condition);

	/**
	 * 根据主键和version字段修改数据
	 * @param po
	 * @return
	 */
	int transactionUpdateOnePO(P po);

	/**
	 * 根据条件批量修改记录,update完毕立即提交数据
	 * @param po
	 * @return
	 */
	int transactionBatchUpdate(List<P> po);

	/**
	 * 根据主键和version字段修改数据
	 * @param po
	 * @param condition
	 * @return
	 */
	int transactionBatchUpdate(List<P> po,Condition condition);

	/**
	 *  根据ID删除记录
	 * @param id
	 * @param <I>
	 * @return
	 */
	int deleteById(T id);

	/**
	 * 根据ID查询一条记录
	 * @param id
	 * @param <I>
	 * @return
	 */
	P selectOneById(T id);

	/**
	 * 根据ID集合查询多条记录
	 * @param id
	 * @param <I>
	 * @return
	 */
	List<P> selectListByIds(List<T> id);

	/**
	 * 条件查询单条记录
	 *
	 * @param condition 约束条件
	 * @return <p>
	 */
	P selectOne(Condition condition);

	/**
	 * 条件查询单条记录,如果没数据返回Optional
	 *
	 * @param condition 约束条件
	 * @return Optional<P>
	 */
	Optional<P> selectOneOptional(Condition condition);

	/**
	 * 条件查询多条记录数据，并排序
	 * @param condition  约束条件
	 * @param sortFields 排序字段
	 * @return POJO 集合
	 */
	List<P> selectListSort(Condition condition, SortField<?>... sortFields);

	/**
	 * 读取分页数据,并且对数据排序
	 * @param pageResult 分页参数
	 * @param condition  约束条件
	 * @param sortFields 排序字段
	 * @return 分页结果集
	 */
	PageResult<P> selectPageSort(PageResult<P> pageResult, Condition condition, SortField<?>... sortFields);

	/**
	 * 分页查询数据，不排序
	 *
	 * @param pageResult      分页参数
	 * @param selectLimitStep 查询语句
	 * @return 分页结果集
	 */
	PageResult<P> selectPage(PageResult<P> pageResult, SelectLimitStep<?> selectLimitStep,Condition condition);

	/**
	 * 任意类型读取分页数据
	 *
	 * @param pageResult      分页参数
	 * @param selectLimitStep 查询语句
	 * @param mapper          结果映射方式
	 * @param <O>             返回类型的泛型
	 * @return 分页结果集
	 */
	<O> PageResult<O> selectPage(PageResult<O> pageResult,Condition condition, SelectLimitStep<?> selectLimitStep,
								RecordMapper<? super Record, O> mapper);

	/**
	 * 任意类型读取分页数据
	 *
	 * @param pageResult      分页参数
	 * @param selectLimitStep 查询语句
	 * @param pojoType        POJO类型
	 * @param <O>             返回类型的泛型
	 * @return 分页结果集
	 */
	<O> PageResult<O> selectPage(PageResult<O> pageResult,Condition condition, SelectLimitStep<?> selectLimitStep,
								Class<O> pojoType);
}
