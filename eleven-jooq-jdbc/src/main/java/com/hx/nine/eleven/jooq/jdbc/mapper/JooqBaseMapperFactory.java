package com.hx.nine.eleven.jooq.jdbc.mapper;

import  com.hx.nine.eleven.commons.utils.DateUtils;
import  com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.jooq.jdbc.AbstractRoutingDataSource;
import com.hx.nine.eleven.jooq.jdbc.HXDSL;
import com.hx.nine.eleven.jooq.jdbc.enums.SQLDialectEnums;
import com.hx.nine.eleven.jooq.jdbc.page.PageResult;
import com.hx.nine.eleven.jooq.jdbc.utils.JdbcUrlUtils;
import org.jooq.*;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;

import javax.sql.DataSource;
import java.util.*;

import static org.jooq.impl.DSL.row;

public class JooqBaseMapperFactory<R extends UpdatableRecord<R>, P, T> extends JooqDAOImpl<R, P, T>
		implements JooqBaseMapperDAO<R, P, T> {

	private DSLContext dslContext;

	public JooqBaseMapperFactory(Table<R> table, Class<P> type) {
		super(table, type);
		this.dslContext = this.getDSLContext();
	}

	public JooqBaseMapperFactory(Table<R> table, Class<P> type, String dataSourceName) {
		super(table, type);
		this.dslContext = this.getDSLContext(dataSourceName);
	}

	@Override
	public int insertColumnPO(P po) {
		return dslContext.insertInto(super.getTable()).columns(super.getTable().fields()).values(po).execute();
	}

	@Override
	public int insertPO(P po) {
		R record = this.newRecord(super.getTable(), po, false);
		return dslContext.executeInsert(record);
	}

	@Override
	public int transactionInsertPO(P po) {
		R record = this.newRecord(super.getTable(), po, false);
		return this.dslContext.transactionResult(ctx -> {
			return ctx.dsl().executeInsert(record);
		});
	}

	@Override
	public int batchInsert(List<P> po) {
		List<R> records = this.records(super.getTable(), po, false);
		return this.dslContext.batchInsert(records).execute().length;
	}

	@Override
	public int transactionBatchInsert(List<P> po) {
		List<R> records = this.records(super.getTable(), po, false);
		return this.dslContext.transactionResult(ctx -> {
			return ctx.dsl().batchInsert(records).execute().length;
		});
	}

	@Override
	public int updateOnePO(P po, Condition condition) {
		UpdatableRecord record = this.dslContext.newRecord(super.getTable(), po);
		return this.dslContext.executeUpdate(record, condition);
	}

	@Override
	public int updateOnePO(P po) {
		Table<R> table = super.getTable();
		R record = this.dslContext.newRecord(table, po);
		Condition condition = checkCondition(table,record);
		return this.dslContext.executeUpdate(record, condition);
	}

	@Override
	public int batchUpdate(List<P> po) {
		Table<R> table = super.getTable();
		List<R> records = this.records(table, po, true);
		int result = 0;
		for (int i = 0; i < records.size(); i++) {
			R record = records.get(i);
			Condition condition = checkCondition(table,record);
			result = result + this.dslContext.executeUpdate(record,condition);
		}
		return result;
	}

	@Override
	public int batchUpdate(List<P> po, Condition condition) {
		List<R> records = this.records(super.getTable(), po, true);
		int result = 0;
		for (int i = 0; i < records.size(); i++) {
			result = result + this.dslContext.executeUpdate(records.get(i),condition);
		}
		return result;
	}

	@Override
	public int transactionUpdateOnePO(P po, Condition condition) {
		UpdatableRecord record = this.dslContext.newRecord(super.getTable(), po);
		return this.dslContext.transactionResult(ctx -> {
			return ctx.dsl().executeUpdate(record, condition);
		});
	}

	@Override
	public int transactionUpdateOnePO(P po) {
		Table<R> table = super.getTable();
		R record = this.dslContext.newRecord(table, po);
		Condition condition = checkCondition(table,record);
		return this.dslContext.transactionResult(ctx -> {
			return ctx.dsl().executeUpdate(record, condition);
		});
	}

	@Override
	public int transactionBatchUpdate(List<P> po) {
		Table<R> table = super.getTable();
		List<R> records = this.records(table, po, true);
		return this.dslContext.transactionResult(ctx -> {
			int result = 0;
			for (int i = 0; i < records.size(); i++) {
				R record = records.get(i);
				Condition condition = checkCondition(table,record);
				result = result + ctx.dsl().executeUpdate(record,condition);
			}
			return result;
		});
	}

	@Override
	public int transactionBatchUpdate(List<P> po, Condition condition) {
		List<R> records = this.records(super.getTable(), po, true);
		return this.dslContext.transactionResult(ctx -> {
			int result = 0;
			for (int i = 0; i < records.size(); i++) {
				result = result + ctx.dsl().executeUpdate(records.get(i),condition);
			}
			return result;
		});
	}

	@Override
	public int deleteById(T id) {
		Field<?>[] pk = this.pk(super.getTable());
		if (pk != null) {
			return this.dslContext.delete(super.getTable()).where(equal(pk, id)).execute();
		}
		return 0;
	}

	@Override
	public P selectOneById(T id) {
		Field<?>[] pk = this.pk(super.getTable());
		if (pk != null){
			return this.dslContext.selectFrom(super.getTable()).where(equal(pk, id)).fetchOne(mapper());
		}
		return null;
	}

	@Override
	public List<P> selectListByIds(List<T> ids) {
		Field<?>[] pk = this.pk(super.getTable());
		if (pk != null){
			return this.dslContext.selectFrom(super.getTable()).where(equal(pk, ids)).fetch(mapper());
		}
		return null;
	}

	@Override
	public P selectOne(Condition condition) {
		return this.dslContext.selectFrom(this.getTable()).where(condition).fetchOne(this.mapper());
	}

	@Override
	public Optional<P> selectOneOptional(Condition condition) {
		return Optional.ofNullable(selectOne(condition));
	}

	@Override
	public List<P> selectListSort(Condition condition, SortField<?>... sortFields) {
		return this.dslContext.selectFrom(this.getTable()).where(condition).orderBy(sortFields).fetch(this.mapper());
	}

	@Override
	public PageResult<P> selectPageSort(PageResult<P> pageResult, Condition condition, SortField<?>... sortFields) {
		return selectPage(pageResult,  this.dslContext.selectFrom(this.getTable())
				.where(condition)
				.orderBy(sortFields),condition);
	}

	@Override
	public PageResult<P> selectPage(PageResult<P> pageResult, SelectLimitStep<?> selectLimitStep,Condition condition) {
		return selectPage(pageResult,condition, selectLimitStep, r -> r.into(getType()));
	}

	@Override
	public <O> PageResult<O> selectPage(PageResult<O> pageResult, Condition condition, SelectLimitStep<?> selectLimitStep,
										RecordMapper<? super Record, O> mapper) {
		int size = pageResult.getPageSize();
		int start = (pageResult.getCurrentPage() - 1) * size;
		// 在页数为零的情况下小优化，不查询数据库直接返回数据为空集合的分页包装类
		if (size == 0) {
			return new PageResult<>(Collections.emptyList(), start, 0, 0);
		}
		String pageSql = selectLimitStep.getSQL(ParamType.INLINED);
		String jdbcUrl = JdbcUrlUtils.jdbcUrl(this.getAbstractRoutingDataSource());
		boolean isOracle = ":oracle:".equals(SQLDialectEnums.containsByCode(jdbcUrl).getCode());
		boolean ismysql = ":mysql:".equals(SQLDialectEnums.containsByCode(jdbcUrl).getCode());
		String SELECT = "select";
		// "set @i = 0;select * from ( select @i := @i + 1 as rn ,"pageSql.substring(pageSql.indexOf(SELECT) + SELECT.length())+") temp WHERE RN > ? AND RN <= ?"
		String SELECTSTART = isOracle?"select * from ( select rownum as rn,temp.*  from ( ":ismysql?
				"select * from ( select @i := @i + 1 as rn ,":"select SQL_CALC_FOUND_ROWS ";
		//pageSql.substring(pageSql.indexOf(SELECT) + SELECT.length())
		if (isOracle){
			pageSql = SELECTSTART + pageSql + ") temp ) WHERE RN > ? AND RN <= ?";
		}
		if (ismysql){
			String mysql = pageSql.substring(pageSql.indexOf(SELECT) + SELECT.length());
			pageSql = SELECTSTART + mysql + ") temp,(SELECT @i:=0) AS iTable WHERE RN > ? AND RN <= ?";
		}
		List<O> resultList = this.dslContext.fetch(pageSql, start, isOracle || ismysql?pageResult.getCurrentPage() * size:size).map(mapper);
		Long total = this.dslContext.selectCount().from(getTable()).where(condition).fetchOne().into(Long.class);
		PageResult<O> result = pageResult.into(new PageResult<>());
		result.setData(resultList);
		result.setTotal(total);
		return result;
	}

	@Override
	public <O> PageResult<O> selectPage(PageResult<O> pageResult,Condition condition, SelectLimitStep<?> selectLimitStep, Class<O> pojoType) {
		return selectPage(pageResult,condition, selectLimitStep, r -> r.into(pojoType));
	}

	public static <R extends UpdatableRecord<R>, P> JooqBaseMapperFactory build(Table<R> table, Class<P> type) {
		return new JooqBaseMapperFactory(table, type);
	}

	public static <R extends UpdatableRecord<R>, P> JooqBaseMapperFactory build(Table<R> table, Class<P> type, String dataSourceName) {
		return new JooqBaseMapperFactory(table, type, dataSourceName);
	}

	public JooqBaseMapperFactory current() {
		this.dslContext = this.getDSLContextByCurrent();
		return this;
	}

	/**
	 * 获取当前默认主数据源DSLContext
	 *
	 * @return
	 */
	@Override
	public DSLContext getDSLContext() {
		AbstractRoutingDataSource routingDataSource = this.getAbstractRoutingDataSource();
		return HXDSL.using(routingDataSource);
	}

	/**
	 * 获取指定数据源DSLContext
	 *
	 * @param dataSourceName
	 * @return
	 */
	private DSLContext getDSLContext(String dataSourceName) {
		AbstractRoutingDataSource routingDataSource = this.getAbstractRoutingDataSource();
		DataSource currentDataSource = routingDataSource.getResolvedDataSources().get(dataSourceName);
		return HXDSL.using(currentDataSource);
	}

	/**
	 * 获取当前线程所使用的数据源DSLContext
	 *
	 * @return
	 */
	private DSLContext getDSLContextByCurrent() {
		AbstractRoutingDataSource routingDataSource = this.getAbstractRoutingDataSource();
		DataSource currentDataSource = routingDataSource.currentDataSource();
		return HXDSL.using(currentDataSource);
	}

	/**
	 * 获取AbstractRoutingDataSource 数据源
	 * @return
	 */
	private AbstractRoutingDataSource getAbstractRoutingDataSource() {
		DataSource dataSource = super.getTargetDataSource();
		// 设置数据库类型（需要先判断当前类是否开启事务）
		if (!(dataSource instanceof AbstractRoutingDataSource)) {
			throw new DataAccessException(StringUtils.format("不支持的数[{}]据源类型,数据源需要继承com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource",
					dataSource.getClass().getName()));
		}
		return (AbstractRoutingDataSource) dataSource;
	}

	/**
	 * 获取主键PK
	 * @param table
	 * @param <R>
	 * @return
	 */
	private <R extends UpdatableRecord<R>> Field<?>[] pk(Table<R> table) {
		UniqueKey<?> key = table.getPrimaryKey();
		return key == null ? null : key.getFieldsArray();
	}

	/**
	 * 根据Collection<P> 生成UpdatableRecord<R> 集合
	 * @param table
	 * @param objects
	 * @param forUpdate
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	private <R extends UpdatableRecord<R>, P> List<R> records(Table<R> table, Collection<P> objects, boolean forUpdate) {
		List<R> result = new ArrayList(objects.size());
		Field<?>[] pk = this.pk(table);
		Iterator<P> iterator = objects.iterator();
		while (iterator.hasNext()) {
			P object = iterator.next();
			R record = this.newRecord(table,object,forUpdate,pk);
			result.add(record);
		}
		return result;
	}

	/**
	 *  生成一个UpdatableRecord对象
	 * @param table
	 * @param object
	 * @param forUpdate
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	private  <R extends UpdatableRecord<R>, P> R newRecord(Table<R> table, P object, boolean forUpdate){
		Field<?>[] pk = this.pk(table);
		return this.newRecord(table,object,forUpdate,pk);
	}

	/**
	 * 生成一个UpdatableRecord对象
	 * @param table
	 * @param object
	 * @param forUpdate
	 * @param pk
	 * @param <R>
	 * @param <P>
	 * @return
	 */
	private  <R extends UpdatableRecord<R>, P> R newRecord(Table<R> table, P object, boolean forUpdate,Field<?>[] pk){
		R record = this.dslContext.newRecord(table, object);
		if (forUpdate && pk != null) {
			Field[] fields = pk;
			int length = pk.length;
			for (int i = 0; i < length; ++i) {
				Field<?> field = fields[i];
				record.changed(field, false);
			}
		}
		return record;
	}

	private Condition equal(Field<?>[] pk, T id) {
		if (pk.length == 1) {
			return ((Field<Object>) pk[0]).equal(pk[0].getDataType().convert(id));
		} else {
			return row(pk).equal((Record) id);
		}
	}

	private Condition equal(Field<?>[] pk, Collection<T> ids) {
		if (pk.length == 1) {
			if (ids.size() == 1) {
				return equal(pk, ids.iterator().next());
			} else {
				return pk[0].in(pk[0].getDataType().convert(ids));
			}
		}else {
			return row(pk).in(ids.toArray(EMPTY_RECORD));
		}
	}

	private <R extends UpdatableRecord<R>> Condition checkCondition(Table<R> table,R record){
		Field[] pk = this.pk(table);
		Condition condition = HXDSL.noCondition();
		condition= condition.and(pk[0].eq(record.getValue(pk[0])));
		record.changed(pk[0],false);
		Field versionField = table.field("version");
		Field updateTimeField = table.field("update_time");
		if (versionField == null){
			versionField = table.field("VERSION");
		}
		if (updateTimeField == null){
			updateTimeField = table.field("UPDATE_TIME");
		}
		if (updateTimeField != null){
			record.changed(updateTimeField, true);
			record.set(updateTimeField,DateUtils.getTimeStampAsString());
		}
		if (versionField != null){
			Object version = record.getValue(versionField);
			condition = condition.and(versionField.eq(version.toString()));
			record.set(versionField,StringUtils.valueOf(Integer.valueOf(version.toString())+1));
		}
		return condition;
	}

	@Override
	public T getId(P object) {
		return null;
	}
}
