package com.hx.nine.eleven.mybatisflex;

import com.mybatisflex.core.exception.FlexAssert;
import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.mybatis.MappedStatementTypes;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.provider.EntitySqlProvider;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.FunctionQueryColumn;
import com.mybatisflex.core.query.Join;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.table.TableInfo;
import com.mybatisflex.core.table.TableInfoFactory;
import com.mybatisflex.core.util.ClassUtil;
import com.mybatisflex.core.util.CollectionUtil;
import com.mybatisflex.core.util.ConvertUtil;
import com.mybatisflex.core.util.MapperUtil;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *  数据库基础操作
 * @auth wml
 * @date 2024/9/25
 */
public interface ElevenFlexBaseMapper {

	int DEFAULT_BATCH_SIZE = 1000;

	/**
	 * 插入实体类数据，不忽略 null 值, 如果实体存在null属性也会被当做null写入数据库，如果数据库设置默认值，则不会生效
	 * @param entity  保存实体
	 * @param <T>    实体泛型类型
	 * @return
	 */
	default <T> int insert(T entity) {
		return this.insert(entity, false);
	}

	/**
	 * 插入实体类数据，但是忽略 null 的数据，只对有值的内容进行插入。这样的好处是数据库已经配置了一些默认值，这些默认值才会生效。
	 * @param entity  保存实体
	 * @param <T>     实体泛型类型
	 * @return
	 */
	default <T> int insertSelective(T entity) {
		return this.insert(entity, true);
	}

	/**
	 * 插入实体类数据
	 * @param entity       保存实体
	 * @param ignoreNulls  是否可以忽略null值
	 * @param <T>          实体泛型类型
	 * @return
	 */
	@InsertProvider(
			type = EntitySqlProvider.class,
			method = "insert"
	)
	<T> int insert(@Param("$$entity") T entity, @Param("$$ignoreNulls") boolean ignoreNulls);

	/**
	 * 插入带有主键的实体类，不忽略 null 值，如果主键没有赋值则会抛出
	 * IllegalArgumentException("Entity Primary Key value must not be null.")
	 * @param entity  保存实体
	 * @return
	 */
	default <T> int insertWithPk(T entity) {
		return this.insertWithPk(entity, false);
	}

	/**
	 * 插入带有主键的实体类，忽略 null 值，如果主键没有赋值则会抛出
	 * IllegalArgumentException("Entity Primary Key value must not be null.")
	 * @param entity  保存实体
	 * @return
	 */
	default  <T> int insertSelectiveWithPk(T entity) {
		return this.insertWithPk(entity, true);
	}

	/**
	 * 带有主键的插入，此时实体类不会经过主键生成器生成主键。
	 * @param entity        保存实体
	 * @param ignoreNulls   是否可以忽略null值
	 * @param <T>           实体泛型类型
	 * @return
	 */
	@InsertProvider(
			type = EntitySqlProvider.class,
			method = "insertWithPk"
	)
	<T> int insertWithPk(@Param("$$entity") T entity, @Param("$$ignoreNulls") boolean ignoreNulls);

	/**
	 * 批量插入实体类数据，只会根据第一条数据来构建插入的字段内容。
	 * @param var1
	 * @param <T>
	 * @return
	 */
	@InsertProvider(
			type = EntitySqlProvider.class,
			method = "insertBatch"
	)
	<T> int insertBatch(@Param("$$entities") Collection<T> var1);

	/**
	 * 批量插入实体类数据，按 size 切分,默认按照1000进行切分
	 * @param entities
	 * @param size
	 * @param <T>
	 * @return
	 */
	default <T> int insertBatch(Collection<T> entities, int size) {
		FlexAssert.notEmpty(entities, "entities");
		if (size <= 0) {
			size = DEFAULT_BATCH_SIZE;
		}

		List<T> entityList = entities instanceof List ? (List)entities : new ArrayList(entities);
		int sum = 0;
		int entitiesSize = entities.size();
		int maxIndex = entitiesSize / size + (entitiesSize % size == 0 ? 0 : 1);

		for(int i = 0; i < maxIndex; ++i) {
			List<T> list = ((List)entityList).subList(i * size, Math.min(i * size + size, entitiesSize));
			sum += this.insertBatch(list);
		}

		return sum;
	}

	/**
	 * 批量插入实体类数据，忽略 null 值，按照1000切分插入。
	 * @param entities
	 * @param <T>
	 * @return
	 */
	default <T> int insertBatchSelective(Collection<T> entities) {
		return this.insertBatchSelective(entities, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 批量插入实体类数据，忽略 null 值,默认按照1000切分插入。
	 * @param entities
	 * @param size
	 * @param <T>
	 * @return
	 */
	default <T> int insertBatchSelective(Collection<T> entities, int size) {
		FlexAssert.notEmpty(entities, "entities");
		if (size <= 0) {
			size = DEFAULT_BATCH_SIZE;
		}

		Class aClass = ClassUtil.getUsefulClass(this.getClass());
		int[] batchResults = Db.executeBatch(entities, size, aClass,(t,u) ->{((ElevenFlexBaseMapper)t).insertSelective(u);});
		int result = 0;
		int[] var6 = batchResults;
		int var7 = batchResults.length;

		for(int var8 = 0; var8 < var7; ++var8) {
			int anInt = var6[var8];
			if (anInt > 0) {
				result += anInt;
			}
		}

		return result;
	}

	/**
	 * 插入或者更新，若主键有值，则更新，若没有主键值，则插入，插入或者更新都不会忽略 null 值。
	 * @param entity
	 * @param <T>
	 * @return
	 */
	default  <T> int insertOrUpdate(T entity) {
		return this.insertOrUpdate(entity, false);
	}

	/**
	 * 插入或者更新，若主键有值，则更新，若没有主键值，则插入，插入或者更新都会忽略 null 值
	 * @param entity
	 * @param <T>
	 * @return
	 */
	default  <T> int insertOrUpdateSelective(T entity) {
		return this.insertOrUpdate(entity, true);
	}

	/**
	 * 插入或者更新，若主键有值，则更新，若没有主键值，则插入。
	 * @param entity
	 * @param ignoreNulls
	 * @param <T>
	 * @return
	 */
	default <T> int insertOrUpdate(T entity, boolean ignoreNulls) {
		TableInfo tableInfo = TableInfoFactory.ofEntityClass(entity.getClass());
		Object[] pkArgs = tableInfo.buildPkSqlArgs(entity);
		return pkArgs.length != 0 && pkArgs[0] != null ? this.update(entity, ignoreNulls) : this.insert(entity, ignoreNulls);
	}

	/**
	 * 根据实体主键来删除数据。相比deleteById(id)，此方法更便于对复合主键实体类的删除。
	 * @param entity
	 * @param <T>
	 * @return
	 */
	default <T> int delete(T entity) {
		FlexAssert.notNull(entity, "entity can not be null");
		TableInfo tableInfo = TableInfoFactory.ofEntityClass(entity.getClass());
		Object[] pkArgs = tableInfo.buildPkSqlArgs(entity);
		return this.deleteById(pkArgs);
	}

	/**
	 * 根据主键删除数据。如果是多个主键的情况下，需要传入数组，例如：new Integer[]{100,101}
	 * @param var1
	 * @return
	 */
	@DeleteProvider(
			type = EntitySqlProvider.class,
			method = "deleteById"
	)
	int deleteById(@Param("$$primaryValue") Serializable var1);

	/**
	 * 根据多个主键批量删除数据
	 * @param var1
	 * @return
	 */
	@DeleteProvider(
			type = EntitySqlProvider.class,
			method = "deleteBatchByIds"
	)
	int deleteBatchByIds(@Param("$$primaryValue") Collection<? extends Serializable> var1);

	/**
	 * 根据多个主键批量删除数据,多条数据时，根据指定数量切分删除，默认1000
	 * @param ids
	 * @param size
	 * @return
	 */
	default int deleteBatchByIds(Collection<? extends Serializable> ids, int size) {
		if (size <= 0) {
			size = DEFAULT_BATCH_SIZE;
		}

		int sum = 0;
		int entitiesSize = ids.size();
		int maxIndex = entitiesSize / size + (entitiesSize % size == 0 ? 0 : 1);
		List<? extends Serializable> idList = ids instanceof List ? (List)ids : new ArrayList(ids);

		for(int i = 0; i < maxIndex; ++i) {
			List<? extends Serializable> list = ((List)idList).subList(i * size, Math.min(i * size + size, entitiesSize));
			sum += this.deleteBatchByIds(list);
		}

		return sum;
	}

	/**
	 * 根据 Map 构建的条件来删除数据
	 * @param whereConditions
	 * @return
	 */
	default int deleteByMap(Map<String, Object> whereConditions) {
		FlexAssert.notEmpty(whereConditions, "whereConditions");
		return this.deleteByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件来删除数据
	 * @param whereConditions
	 * @return
	 */
	default int deleteByCondition(QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.deleteByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件来删除数据
	 * @param var1
	 * @return
	 */
	@DeleteProvider(
			type = EntitySqlProvider.class,
			method = "deleteByQuery"
	)
	int deleteByQuery(@Param("$$query") QueryWrapper var1);

	/**
	 * 根据主键来更新数据，若实体类属性数据为 null，该属性不会更新到数据库
	 * @param entity
	 * @param <T>
	 * @return
	 */
	default  <T> int update(T entity) {
		return this.update(entity, true);
	}

	/**
	 * 根据主键来更新数据到数据库,可指定实体类属性数据为 null，是否将该属性更新到数据库， true: 不更新；false: 更新
	 * @param var1
	 * @param var2
	 * @param <T>
	 * @return
	 */
	@UpdateProvider(
			type = EntitySqlProvider.class,
			method = "update"
	)
	<T> int update(@Param("$$entity") T var1, @Param("$$ignoreNulls") boolean var2);

	/**
	 * 根据 Map 构建的条件来更新数据
	 * @param entity
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> int updateByMap(T entity, Map<String, Object> whereConditions) {
		FlexAssert.notEmpty(whereConditions, "whereConditions");
		return this.updateByQuery(entity, QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据 Map 构建的条件来更新数据
	 * @param entity
	 * @param ignoreNulls
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> int updateByMap(T entity, boolean ignoreNulls, Map<String, Object> whereConditions) {
		FlexAssert.notEmpty(whereConditions, "whereConditions");
		return this.updateByQuery(entity, ignoreNulls, QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件来更新数据
	 * @param entity
	 * @param whereConditions
	 * @return
	 */
	default <T> int updateByCondition(T entity, QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.updateByQuery(entity, QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件来更新数据
	 * @param entity
	 * @param ignoreNulls
	 * @param whereConditions
	 * @return
	 */
	default <T> int updateByCondition(T entity, boolean ignoreNulls, QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.updateByQuery(entity, ignoreNulls, QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件来更新数据
	 * @param entity
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> int updateByQuery(T entity, QueryWrapper queryWrapper) {
		return this.updateByQuery(entity, true, queryWrapper);
	}

	/**
	 * 根据查询条件来更新数据
	 * @param var1
	 * @param var2
	 * @param var3
	 * @param <T>
	 * @return
	 */
	@UpdateProvider(
			type = EntitySqlProvider.class,
			method = "updateByQuery"
	)
	<T> int updateByQuery(@Param("$$entity") T var1, @Param("$$ignoreNulls") boolean var2, @Param("$$query") QueryWrapper var3);

	/**
	 * 根据实体主键查询数据，便于对复合主键实体类的查询。
	 * @param entity
	 * @param <T>
	 * @return
	 */
	default <T> T selectOneByEntityId(T entity) {
		FlexAssert.notNull(entity, "entity can not be null");
		TableInfo tableInfo = TableInfoFactory.ofEntityClass(entity.getClass());
		Object[] pkArgs = tableInfo.buildPkSqlArgs(entity);
		return this.selectOneById(pkArgs);
	}

	/**
	 * 根据主键查询数据
	 * @param var1
	 * @param <T>
	 * @return
	 */
	@SelectProvider(
			type = EntitySqlProvider.class,
			method = "selectOneById"
	)
	<T> T selectOneById(@Param("$$primaryValue") Serializable var1);

	/**
	 * 根据 Map 构建的条件来查询数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> T selectOneByMap(Map<String, Object> whereConditions) {
		FlexAssert.notEmpty(whereConditions, "whereConditions");
		return this.selectOneByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件查询数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> T selectOneByCondition(QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.selectOneByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件来查询 1 条数据
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> T selectOneByQuery(QueryWrapper queryWrapper) {
		List<Join> joins = CPI.getJoins(queryWrapper);
		if (CollectionUtil.isNotEmpty(joins)) {
			return MapperUtil.getSelectOneResult(this.selectListByQuery(queryWrapper));
		} else {
			Long limitRows = CPI.getLimitRows(queryWrapper);

			T res;
			try {
				queryWrapper.limit(1);
				res = MapperUtil.getSelectOneResult(this.selectListByQuery(queryWrapper));
			} finally {
				CPI.setLimitRows(queryWrapper, limitRows);
			}

			return res;
		}
	}

	/**
	 * 根据查询条件来查询 1 条数据
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> R selectOneByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		List<Join> joins = CPI.getJoins(queryWrapper);
		if (CollectionUtil.isNotEmpty(joins)) {
			return MapperUtil.getSelectOneResult(this.selectListByQueryAs(queryWrapper, asType));
		} else {
			Long limitRows = CPI.getLimitRows(queryWrapper);

			R res;
			try {
				queryWrapper.limit(1);
				res = MapperUtil.getSelectOneResult(this.selectListByQueryAs(queryWrapper, asType));
			} finally {
				CPI.setLimitRows(queryWrapper, limitRows);
			}

			return res;
		}
	}

	/**
	 * 根据 Map 构建的条件来查询 1 条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> T selectOneWithRelationsByMap(Map<String, Object> whereConditions) {
		FlexAssert.notEmpty(whereConditions, "whereConditions");
		return this.selectOneWithRelationsByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件查询 1 条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> T selectOneWithRelationsByCondition(QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.selectOneWithRelationsByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件来查询 1 条数据
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default  <T> T selectOneWithRelationsByQuery(QueryWrapper queryWrapper) {
		List<Join> joins = CPI.getJoins(queryWrapper);
		if (CollectionUtil.isNotEmpty(joins)) {
			return ElevenFlexMapperUtil.queryRelations(this, (T) MapperUtil.getSelectOneResult(this.selectListByQuery(queryWrapper)));
		} else {
			Long limitRows = CPI.getLimitRows(queryWrapper);

			T res;
			try {
				queryWrapper.limit(1);
				res = ElevenFlexMapperUtil.queryRelations(this,  (T)MapperUtil.getSelectOneResult(this.selectListByQuery(queryWrapper)));
			} finally {
				CPI.setLimitRows(queryWrapper, limitRows);
			}
			return res;
		}
	}

	default  <T> T selectOneWithRelationsById(Serializable id) {
		return ElevenFlexMapperUtil.queryRelations(this, (T) this.selectOneById(id));
	}

	default <R> R selectOneWithRelationsByIdAs(Serializable id, Class<R> asType) {
		R result;
		try {
			MappedStatementTypes.setCurrentType(asType);
			result = this.selectOneById(id);
		} finally {
			MappedStatementTypes.clear();
		}

		return ElevenFlexMapperUtil.queryRelations(this, result);
	}

	/**
	 * 根据查询条件来查询 1 条数据
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> R selectOneWithRelationsByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		List<Join> joins = CPI.getJoins(queryWrapper);
		if (CollectionUtil.isNotEmpty(joins)) {
			return ElevenFlexMapperUtil.queryRelations(this, MapperUtil.getSelectOneResult(this.selectListByQueryAs(queryWrapper, asType)));
		} else {
			Long limitRows = CPI.getLimitRows(queryWrapper);

			R res;
			try {
				queryWrapper.limit(1);
				res = ElevenFlexMapperUtil.queryRelations(this, MapperUtil.getSelectOneResult(this.selectListByQueryAs(queryWrapper, asType)));
			} finally {
				CPI.setLimitRows(queryWrapper, limitRows);
			}
			return res;
		}
	}

	/**
	 * 根据多个主键来查询多条数据
	 * @param var1
	 * @param <T>
	 * @return
	 */
	@SelectProvider(
			type = EntitySqlProvider.class,
			method = "selectListByIds"
	)
	<T> List<T> selectListByIds(@Param("$$primaryValue") Collection<? extends Serializable> var1);

	/**
	 * 根据 Map 来构建查询条件，查询多条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectListByMap(Map<String, Object> whereConditions) {
		FlexAssert.notEmpty(whereConditions, "whereConditions");
		return this.selectListByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据 Map 来构建查询条件，查询多条数据
	 * @param whereConditions
	 * @param count
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectListByMap(Map<String, Object> whereConditions, Long count) {
		FlexAssert.notEmpty(whereConditions, "whereConditions");
		return this.selectListByQuery(QueryWrapper.create().where(whereConditions).limit(count));
	}

	/**
	 * 根据查询条件查询多条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectListByCondition(QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.selectListByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 根据查询条件查询多条数据
	 * @param whereConditions
	 * @param count
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectListByCondition(QueryCondition whereConditions, Long count) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.selectListByQuery(QueryWrapper.create().where(whereConditions).limit(count));
	}

	/**
	 * 根据查询条件查询数据列表
	 * @param var1
	 * @param <T>
	 * @return
	 */
	@SelectProvider(
			type = EntitySqlProvider.class,
			method = "selectListByQuery"
	)
	<T> List<T> selectListByQuery(@Param("$$query") QueryWrapper var1);

	/**
	 * 根据查询条件查询数据列表
	 * @param queryWrapper
	 * @param consumers
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectListByQuery(QueryWrapper queryWrapper, Consumer<FieldQueryBuilder<T>>... consumers) {
		List<T> list = this.selectListByQuery(queryWrapper);
		if (list != null && !list.isEmpty()) {
			ElevenFlexMapperUtil.queryFields(this, list, consumers);
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * 使用游标查询
	 * 根据查询条件查询游标数据，该方法必须在事务中才能正常使用，非事务下无法获取数据
	 * @param var1
	 * @return
	 */
	@SelectProvider(
			type = EntitySqlProvider.class,
			method = "selectListByQuery"
	)
	<T> Cursor<T> selectCursorByQuery(@Param("$$query") QueryWrapper var1);

	default <R> Cursor<R> selectCursorByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		Cursor var3;
		try {
			MappedStatementTypes.setCurrentType(asType);
			var3 = this.selectCursorByQuery(queryWrapper);
		} finally {
			MappedStatementTypes.clear();
		}

		return var3;
	}

	/**
	 * 根据查询条件查询 Row 数据
	 * @param var1
	 * @return
	 */
	@SelectProvider(
			type = EntitySqlProvider.class,
			method = "selectListByQuery"
	)
	List<Row> selectRowsByQuery(@Param("$$query") QueryWrapper var1);

	/**
	 * 根据查询条件查询数据列表，要求返回的数据为 asType。这种场景一般用在 left join 时，有多出了实体类本身的字段内容，可以转换为 dto、vo 等场景
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> List<R> selectListByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		if (!Number.class.isAssignableFrom(asType) && String.class != asType) {
			if (Map.class.isAssignableFrom(asType)) {
				return (List<R>) this.selectRowsByQuery(queryWrapper);
			} else {
				List var3;
				try {
					MappedStatementTypes.setCurrentType(asType);
					var3 = this.selectListByQuery(queryWrapper);
				} finally {
					MappedStatementTypes.clear();
				}

				return var3;
			}
		} else {
			return this.selectObjectListByQueryAs(queryWrapper, asType);
		}
	}

	/**
	 * 根据查询条件查询数据列表，要求返回的数据为 asType 类型
	 * @param queryWrapper
	 * @param asType
	 * @param consumers
	 * @param <R>
	 * @return
	 */
	default <R> List<R> selectListByQueryAs(QueryWrapper queryWrapper, Class<R> asType, Consumer<FieldQueryBuilder<R>>... consumers) {
		List<R> list = this.selectListByQueryAs(queryWrapper, asType);
		if (list != null && !list.isEmpty()) {
			ElevenFlexMapperUtil.queryFields(this, list, consumers);
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * 查询实体类及其 Relation 注解字段
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectListWithRelationsByQuery(QueryWrapper queryWrapper) {
		return ElevenFlexMapperUtil.queryRelations(this, this.selectListByQuery(queryWrapper));
	}

	/**
	 * 查询实体类及其 Relation 注解字段
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> List<R> selectListWithRelationsByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		if (!Number.class.isAssignableFrom(asType) && String.class != asType) {
			if (Map.class.isAssignableFrom(asType)) {
				return (List<R>) this.selectRowsByQuery(queryWrapper);
			} else {
				List result;
				try {
					MappedStatementTypes.setCurrentType(asType);
					result = this.selectListByQuery(queryWrapper);
				} finally {
					MappedStatementTypes.clear();
				}

				return ElevenFlexMapperUtil.queryRelations(this, result);
			}
		} else {
			return this.selectObjectListByQueryAs(queryWrapper, asType);
		}
	}

	/**
	 * 查询实体类及其 Relation 注解字段
	 * @param queryWrapper
	 * @param asType
	 * @param consumers
	 * @param <R>
	 * @return
	 */
	default <R> List<R> selectListWithRelationsByQueryAs(QueryWrapper queryWrapper, Class<R> asType, Consumer<FieldQueryBuilder<R>>... consumers) {
		List<R> list = this.selectListByQueryAs(queryWrapper, asType);
		if (list != null && !list.isEmpty()) {
			ElevenFlexMapperUtil.queryRelations(this, list);
			ElevenFlexMapperUtil.queryFields(this, list, consumers);
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * 查询全部数据
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectAll() {
		return this.selectListByQuery(new QueryWrapper());
	}

	/**
	 * 查询全部数据，及其 Relation 字段内容
	 * @param <T>
	 * @return
	 */
	default <T> List<T> selectAllWithRelations() {
		return ElevenFlexMapperUtil.queryRelations(this, this.selectListByQuery(new QueryWrapper()));
	}

	/**
	 * 查询第一列返回的数据，QueryWrapper 执行的结果应该只有 1 列，例如：QueryWrapper.create().select(ACCOUNT.id).where(...);
	 * @param queryWrapper
	 * @return
	 */
	default Object selectObjectByQuery(QueryWrapper queryWrapper) {
		return MapperUtil.getSelectOneResult(this.selectObjectListByQuery(queryWrapper));
	}

	/**
	 * 查询第一列返回的数据，QueryWrapper 执行的结果应该只有 1 列，例如：QueryWrapper.create().select(ACCOUNT.id).where(...);
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> R selectObjectByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		return MapperUtil.getSelectOneResult(this.selectObjectListByQueryAs(queryWrapper, asType));
	}

	/**
	 * 查询第一列返回的数据集合，QueryWrapper 执行的结果应该只有 1 列，例如：QueryWrapper.create().select(ACCOUNT.id).where(...);
	 * @param var1
	 * @return
	 */
	@SelectProvider(
			type = EntitySqlProvider.class,
			method = "selectObjectByQuery"
	)
	List<Object> selectObjectListByQuery(@Param("$$query") QueryWrapper var1);

	/**
	 * 查询第一列返回的数据集合，QueryWrapper 执行的结果应该只有 1 列，例如：QueryWrapper.create().select(ACCOUNT.id).where(...);
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> List<R> selectObjectListByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		List<Object> queryResults = this.selectObjectListByQuery(queryWrapper);
		if (queryResults != null && !queryResults.isEmpty()) {
			List<R> results = new ArrayList(queryResults.size());
			Iterator var5 = queryResults.iterator();

			while(var5.hasNext()) {
				Object queryResult = var5.next();
				results.add((R) ConvertUtil.convert(queryResult, asType));
			}

			return results;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * 查询数据量
	 * @param queryWrapper
	 * @return
	 */
	default long selectCountByQuery(QueryWrapper queryWrapper) {
		List selectColumns = CPI.getSelectColumns(queryWrapper);

		long var4;
		try {
			List objects;
			if (CollectionUtil.isEmpty(selectColumns)) {
				queryWrapper.select(new QueryColumn[]{QueryMethods.count()});
				objects = this.selectObjectListByQuery(queryWrapper);
			} else if (selectColumns.get(0) instanceof FunctionQueryColumn) {
				if (!"COUNT".equalsIgnoreCase(((FunctionQueryColumn)selectColumns.get(0)).getFnName())) {
					CPI.setSelectColumns(queryWrapper, Collections.singletonList(QueryMethods.count()));
				}

				objects = this.selectObjectListByQuery(queryWrapper);
			} else if (MapperUtil.hasDistinct(selectColumns)) {
				objects = this.selectObjectListByQuery(MapperUtil.rawCountQueryWrapper(queryWrapper));
			} else {
				CPI.setSelectColumns(queryWrapper, Collections.singletonList(QueryMethods.count()));
				objects = this.selectObjectListByQuery(queryWrapper);
			}

			var4 = MapperUtil.getLongNumber(objects);
		} finally {
			CPI.setSelectColumns(queryWrapper, selectColumns);
		}

		return var4;
	}

	/**
	 * 根据条件查询数据总量
	 * @param whereConditions
	 * @return
	 */
	default long selectCountByCondition(QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		return this.selectCountByQuery(QueryWrapper.create().where(whereConditions));
	}

	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginate(Number pageNumber, Number pageSize, QueryWrapper queryWrapper) {
		Page<T> page = new Page(pageNumber, pageSize);
		return this.paginate(page, queryWrapper);
	}

	/**
	 * 分页查询，及其 Relation 字段内容
	 * @param pageNumber
	 * @param pageSize
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginateWithRelations(Number pageNumber, Number pageSize, QueryWrapper queryWrapper) {
		Page<T> page = new Page(pageNumber, pageSize);
		return this.paginateWithRelations(page, queryWrapper);
	}

	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginate(Number pageNumber, Number pageSize, QueryCondition whereConditions) {
		Page<T> page = new Page(pageNumber, pageSize);
		return this.paginate(page, (new QueryWrapper()).where(whereConditions));
	}

	/**
	 * 分页查询，及其 Relation 字段内容。
	 * @param pageNumber
	 * @param pageSize
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginateWithRelations(Number pageNumber, Number pageSize, QueryCondition whereConditions) {
		Page<T> page = new Page(pageNumber, pageSize);
		return this.paginateWithRelations(page, (new QueryWrapper()).where(whereConditions));
	}

	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param totalRow
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginate(Number pageNumber, Number pageSize, Number totalRow, QueryWrapper queryWrapper) {
		Page<T> page = new Page(pageNumber, pageSize, totalRow);
		return this.paginate(page, queryWrapper);
	}

	/**
	 * 分页查询，及其 Relation 字段内容。
	 * @param pageNumber
	 * @param pageSize
	 * @param totalRow
	 * @param queryWrapper
	 * @return
	 */
	default <T> Page<T> paginateWithRelations(Number pageNumber, Number pageSize, Number totalRow, QueryWrapper queryWrapper) {
		Page<T> page = new Page(pageNumber, pageSize, totalRow);
		return this.paginateWithRelations(page, queryWrapper);
	}

	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param totalRow
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginate(Number pageNumber, Number pageSize, Number totalRow, QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		Page<T> page = new Page(pageNumber, pageSize, totalRow);
		return this.paginate(page, (new QueryWrapper()).where(whereConditions));
	}

	/**
	 *  分页查询，及其 Relation 字段内容。
	 * @param pageNumber
	 * @param pageSize
	 * @param totalRow
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	default  <T> Page<T> paginateWithRelations(Number pageNumber, Number pageSize, Number totalRow, QueryCondition whereConditions) {
		FlexAssert.notNull(whereConditions, "whereConditions");
		Page<T> page = new Page(pageNumber, pageSize, totalRow);
		return this.paginateWithRelations(page, (new QueryWrapper()).where(whereConditions));
	}

	/**
	 * 分页查询
	 * @param page
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginate(Page<T> page, QueryWrapper queryWrapper) {
		return this.paginateAs(page, queryWrapper, (Class)null);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param queryWrapper
	 * @param consumers
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginate(Page<T> page, QueryWrapper queryWrapper, Consumer<FieldQueryBuilder<T>>... consumers) {
		return this.paginateAs((Page)page, (QueryWrapper)queryWrapper, (Class)null, (Consumer[])consumers);
	}

	/**
	 * 分页查询，及其 Relation 字段内容。
	 * @param page
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginateWithRelations(Page<T> page, QueryWrapper queryWrapper) {
		return this.paginateWithRelationsAs(page, queryWrapper, (Class)null);
	}

	/**
	 * 分页查询，及其 Relation 字段内容。
	 * @param page
	 * @param queryWrapper
	 * @param consumers
	 * @param <T>
	 * @return
	 */
	default <T> Page<T> paginateWithRelations(Page<T> page, QueryWrapper queryWrapper, Consumer<FieldQueryBuilder<T>>... consumers) {
		return this.paginateWithRelationsAs((Page)page, (QueryWrapper)queryWrapper, (Class)null, (Consumer[])consumers);
	}

	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateAs(Number pageNumber, Number pageSize, QueryWrapper queryWrapper, Class<R> asType) {
		Page<R> page = new Page(pageNumber, pageSize);
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, false, new Consumer[0]);
	}

	/**
	 * 分页查询
	 * @param pageNumber
	 * @param pageSize
	 * @param totalRow
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateAs(Number pageNumber, Number pageSize, Number totalRow, QueryWrapper queryWrapper, Class<R> asType) {
		Page<R> page = new Page(pageNumber, pageSize, totalRow);
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, false, new Consumer[0]);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateAs(Page<R> page, QueryWrapper queryWrapper, Class<R> asType) {
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, false, new Consumer[0]);
	}

	/**
	 * 分页查询
	 * @param page
	 * @param queryWrapper
	 * @param asType
	 * @param consumers
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateAs(Page<R> page, QueryWrapper queryWrapper, Class<R> asType, Consumer<FieldQueryBuilder<R>>... consumers) {
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, false, consumers);
	}

	/**
	 * 分页查询，及其 Relation 字段内容
	 * @param pageNumber
	 * @param pageSize
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateWithRelationsAs(Number pageNumber, Number pageSize, QueryWrapper queryWrapper, Class<R> asType) {
		Page<R> page = new Page(pageNumber, pageSize);
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, true, new Consumer[0]);
	}

	/**
	 * 分页查询，及其 Relation 字段内容
	 * @param pageNumber   当前页码，从 1 开始
	 * @param pageSize  每 1 页的数据量
	 * @param totalRow   非必须值，若传入该值，mybatis-flex 则不再去查询总数据量（若传入小于 0 的数值，也会去查询总量）
	 * @param queryWrapper  查询条件
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateWithRelationsAs(Number pageNumber, Number pageSize, Number totalRow, QueryWrapper queryWrapper, Class<R> asType) {
		Page<R> page = new Page(pageNumber, pageSize, totalRow);
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, true, new Consumer[0]);
	}

	/**
	 * 分页查询，及其 Relation 字段内容
	 * @param page
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateWithRelationsAs(Page<R> page, QueryWrapper queryWrapper, Class<R> asType) {
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, true, new Consumer[0]);
	}

	/**
	 * 分页查询，及其 Relation 字段内容
	 * @param page
	 * @param queryWrapper
	 * @param asType
	 * @param consumers
	 * @param <R>
	 * @return
	 */
	default <R> Page<R> paginateWithRelationsAs(Page<R> page, QueryWrapper queryWrapper, Class<R> asType, Consumer<FieldQueryBuilder<R>>... consumers) {
		return ElevenFlexMapperUtil.doPaginate(this, page, queryWrapper, asType, true, consumers);
	}

//	default <E> Page<E> xmlPaginate(String dataSelectId, Page<E> page, QueryWrapper queryWrapper) {
//		return this.xmlPaginate(dataSelectId, dataSelectId + "_COUNT", page, queryWrapper, (Map)null);
//	}
//
//	default <E> Page<E> xmlPaginate(String dataSelectId, Page<E> page, Map<String, Object> otherParams) {
//		return this.xmlPaginate(dataSelectId, dataSelectId + "_COUNT", page, (QueryWrapper)null, otherParams);
//	}
//
//	default <E> Page<E> xmlPaginate(String dataSelectId, Page<E> page, QueryWrapper queryWrapper, Map<String, Object> otherParams) {
//		return this.xmlPaginate(dataSelectId, dataSelectId + "_COUNT", page, queryWrapper, otherParams);
//	}

//	default <E> Page<E> xmlPaginate(String dataSelectId, String countSelectId, Page<E> page, QueryWrapper queryWrapper, Map<String, Object> otherParams) {
//		SqlSessionFactory sqlSessionFactory = FlexGlobalConfig.getDefaultConfig().getSqlSessionFactory();
//		ExecutorType executorType = FlexGlobalConfig.getDefaultConfig().getConfiguration().getDefaultExecutorType();
//		String mapperClassName = ClassUtil.getUsefulClass(this.getClass()).getName();
//		Map<String, Object> preparedParams = MapperUtil.preparedParams(this, page, queryWrapper, otherParams);
//		if (!dataSelectId.contains(".")) {
//			dataSelectId = mapperClassName + "." + dataSelectId;
//		}
//
//		SqlSession sqlSession = sqlSessionFactory.openSession(executorType, false);
//		Throwable var11 = null;
//
//		try {
//			if (page.getTotalRow() < 0L) {
//				if (!countSelectId.contains(".")) {
//					countSelectId = mapperClassName + "." + countSelectId;
//				}
//
//				Number number = (Number)sqlSession.selectOne(countSelectId, preparedParams);
//				page.setTotalRow(number == null ? -1L : number.longValue());
//			}
//
//			if (page.hasRecords()) {
//				List<E> entities = sqlSession.selectList(dataSelectId, preparedParams);
//				page.setRecords(entities);
//			}
//		} catch (Throwable var20) {
//			var11 = var20;
//			throw var20;
//		} finally {
//			if (sqlSession != null) {
//				if (var11 != null) {
//					try {
//						sqlSession.close();
//					} catch (Throwable var19) {
//						var11.addSuppressed(var19);
//					}
//				} else {
//					sqlSession.close();
//				}
//			}
//
//		}
//
//		return page;
//	}
}
