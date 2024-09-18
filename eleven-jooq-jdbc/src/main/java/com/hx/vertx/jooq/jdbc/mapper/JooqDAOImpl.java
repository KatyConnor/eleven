package com.hx.vertx.jooq.jdbc.mapper;

import co.paralleluniverse.fibers.jdbc.FiberDataSource;
import com.hx.lang.commons.utils.Builder;
import com.hx.lang.commons.utils.CollectionUtils;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.boot.utils.HXLogger;
import com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource;
import com.hx.vertx.jooq.jdbc.HXDSL;
import com.hx.vertx.jooq.jdbc.HXDataSourceConnectionProvider;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManager;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManagerEntity;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManagerHolder;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordListener;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.row;

public abstract class JooqDAOImpl<R extends UpdatableRecord<R>, P, T> implements DAO<R, P, T> {

	private static final JooqLogger LOGGER = JooqLogger.getLogger(JooqDAOImpl.class);

	static final Record[] EMPTY_RECORD = {};
	private final Table<R> table;
	private final Class<P> type;
	private RecordMapper<R, P> mapper;
	private Configuration configuration;
	private DataSource targetDataSource;

	public DataSource getTargetDataSource() {
		return targetDataSource;
	}

	public void setTargetDataSource(DataSource targetDataSource) {
		this.targetDataSource = targetDataSource;
	}

	protected JooqDAOImpl(Table<R> table, Class<P> type) {
		this(table, type, null);
	}

	protected JooqDAOImpl(Table<R> table, Class<P> type, Configuration configuration) {
		this.table = table;
		this.type = type;
		this.targetDataSource = VertxApplicationContextAware.getBean("dataSource");
		DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
		HXDataSourceConnectionProvider connectionProvider = new HXDataSourceConnectionProvider(this.targetDataSource);
		defaultConfiguration.set(connectionProvider);
		this.setConfiguration(defaultConfiguration);
	}

	/**
	 * Inject a configuration.
	 * <p>
	 * This method is maintained to be able to configure a <code>DAO</code>
	 * using Spring. It is not exposed in the public API.
	 */
	public /* non-final */ void setConfiguration(Configuration configuration) {
		this.configuration = configuration != null ? configuration : new DefaultConfiguration();
		this.mapper = this.configuration.recordMapperProvider().provide(table.recordType(), type);
	}

	public final DSLContext ctx() {
		return HXDSL.using(this.targetDataSource);
	}

	@Override
	public Configuration configuration() {
		try {
			// 设置数据库类型（需要先判断当前类是否开启事务）
			if (!(this.targetDataSource instanceof AbstractRoutingDataSource)) {
				throw new DataAccessException(StringUtils.format("不支持的数[{}]据源类型,数据源需要继承com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource",
						this.targetDataSource.getClass().getName()));
			}

			String dataSourceName = ((AbstractRoutingDataSource) this.targetDataSource).currentDataSourceName();
			JooqTransactionManagerEntity entity = JooqTransactionManagerHolder.getTransactionManager(dataSourceName);
			entity = ObjectUtils.isEmpty(entity) ? Builder.of(JooqTransactionManagerEntity::new).
					with(JooqTransactionManagerEntity::setKey, dataSourceName).
					with(JooqTransactionManagerEntity::setTransaction, false).build() : entity;

			this.configuration.set(JDBCUtils.dialect(ObjectUtils.isEmpty(entity.getConnection()) ?
					FiberDataSource.wrap(this.targetDataSource).getConnection() : entity.getConnection()));
			entity.setConfiguration(this.configuration);
			// 开启事务
			JooqTransactionManagerHolder.addTransactionManager(entity);
			// 开启事务
			if (JooqTransactionManagerHolder.getTransaction(dataSourceName)) {
				JooqTransactionManager jooqTransactionManager = VertxApplicationContextAware.getBean(JooqTransactionManager.class);
				jooqTransactionManager.begin();
			}
		} catch (SQLException e) {
			LOGGER.error("获取 Configuration 异常", e);
		}
		return this.configuration;
	}

	@Override
	public Settings settings() {
		return this.configuration.settings();
	}

	@Override
	public SQLDialect dialect() {
		return this.configuration.dialect();
	}

	@Override
	public SQLDialect family() {
		return dialect().family();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Subclasses may override this method to provide custom implementations.
	 */
	@Override
	public RecordMapper<R, P> mapper() {
		return mapper;
	}

	// -------------------------------------------------------------------------
	// DAO API implement
	// -------------------------------------------------------------------------
	@Override
	public void insert(P object) {
		insert(singletonList(object));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void insert(P... objects) {
		insert(asList(objects));
	}

	@Override
	public void insert(Collection<P> objects) {
		if (CollectionUtils.isEmpty(objects)){
			HXLogger.build(this).info("数据对象为空，暂不执行 insert 操作");
			return;
		}
		int result = 0;
		if (objects.size() > 1){
			List<R> records = this.records(objects, false);
			if (records.size() <= 0){
				HXLogger.build(this).info("生成record失败");
				return;
			}
			if (!FALSE.equals(settings().isReturnRecordToPojo())){
				for (R record : records){
					result = result + ctx().executeInsert(record);
				}
			} else{
				result = ctx().batchInsert(records).execute().length;
			}

			if (result == records.size()){
				HXLogger.build(this).info("数据对象写入数据库成功，执行insert操作[{}]条",result);
			}
		}
		if (objects.size() == 1){
			R record = this.records(objects, false).get(0);
			result = ctx().executeInsert(record);
			if (result == 1){
				HXLogger.build(this).info("数据对象写入数据库成功，执行insert操作[{}]条",result);
			}
		}
	}

	/**
	 * 根据主键修改表记录
	 * @param object
	 */
	@Override
	public void update(P object) {
		update(singletonList(object));
	}

	/**
	 *  根据主键修改表记录
	 * @param objects
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(P... objects) {
		update(asList(objects));
	}

	/**
	 *  根据主键修改表记录
	 * @param objects
	 */
	@Override
	public void update(Collection<P> objects) {
		if (CollectionUtils.isEmpty(objects)){
			HXLogger.build(this).info("数据对象为空，暂不执行 update 操作");
			return;
		}
		int result = 0;
		if (objects.size() > 1) {
			List<R> records = this.records(objects, true);
			if (records.size() <= 0){
				HXLogger.build(this).info("生成record失败");
				return;
			}
			if (!FALSE.equals(settings().isReturnRecordToPojo()) &&
					TRUE.equals(settings().isReturnAllOnUpdatableRecord())) {
				for (R record : records) {
					result = result + ctx().executeUpdate(record);
				}
			} else {
				ctx().batchUpdate(records).execute();
			}
			if (result == records.size()){
				HXLogger.build(this).info("数据对象进行数据库更新成功，执行update操作[{}]条",result);
			}
		} else if (objects.size() == 1) {
			R record = this.records(objects, false).get(0);
			result = ctx().executeUpdate(record);
			if (result == 1){
				HXLogger.build(this).info("数据对象进行数据库更新成功，执行update操作[{}]条",result);
			}
		}
	}

	@Override
	public void merge(P object) {
		merge(singletonList(object));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void merge(P... objects) {
		merge(asList(objects));
	}

	@Override
	public void merge(Collection<P> objects) {
		if (CollectionUtils.isEmpty(objects)){
			HXLogger.build(this).info("数据对象为空，暂不执行 merge 操作");
			return;
		}
		int result = 0;
		if (objects.size() > 1) {
			List<R> records = this.records(objects, false);
			if (records.size() <= 0){
				HXLogger.build(this).info("生成record失败");
				return;
			}
			if (!FALSE.equals(settings().isReturnRecordToPojo()) &&
					TRUE.equals(settings().isReturnAllOnUpdatableRecord())) {
				for (R record : records(objects, false)) {
					result = result + record.merge();
				}
			} else {
				result = ctx().batchMerge(records).execute().length;
			}
			if (result == records.size()){
				HXLogger.build(this).info("数据对象进行merge合并成功，执行 merge 操作[{}]条",result);
			}
		} else if (objects.size() == 1) {
			result = this.records(objects, false).get(0).merge();
			if (result == 1){
				HXLogger.build(this).info("数据对象进行merge合并成功，执行 merge 操作[{}]条",result);
			}
		}
	}

	/**
	 * 根据数据本体进行删除，物理删除数据
	 * @param object
	 */
	@Override
	public void delete(P object) {
		delete(singletonList(object));
	}

	/**
	 * 根据数据本体进行删除，物理删除数据
	 * @param objects
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void delete(P... objects) {
		delete(asList(objects));
	}

	/**
	 * 根据数据本体进行删除，物理删除数据
	 * @param objects
	 */
	@Override
	public void delete(Collection<P> objects) {
		if (CollectionUtils.isEmpty(objects)){
			HXLogger.build(this).info("数据对象为空，暂不执行 delete 操作");
			return;
		}
		int result = 0;
		if (objects.size() > 1) {
			List<R> records = this.records(objects, true);
			if (records.size() <= 0){
				HXLogger.build(this).info("生成record失败");
				return;
			}
			if (!FALSE.equals(settings().isReturnRecordToPojo()) &&
					TRUE.equals(settings().isReturnAllOnUpdatableRecord())) {
				for (R record : records) {
					result = result + ctx().executeDelete(record);
				}
			} else {
				result = ctx().batchDelete(records).execute().length;
			}
			if (result == records.size()){
				HXLogger.build(this).info("数据对象进行数据库物理删除成功，执行 delete 操作[{}]条",result);
			}
		} else if (objects.size() == 1) {
			R record = this.records(objects, true).get(0);
			ctx().executeDelete(record);
			if (result == 1){
				HXLogger.build(this).info("数据对象进行数据库物理删除成功，执行 delete 操作[{}]条",result);
			}
		}
	}

	/**
	 * 根据主键进行删除，物理删除
	 * @param ids
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteById(T... ids) {
		deleteById(asList(ids));
	}

	/**
	 * 根据主键进行删除，物理删除
	 * @param ids
	 */
	@Override
	public void deleteById(Collection<T> ids) {
		Field<?>[] pk = pk();
		if (pk != null) {
			ctx().delete(table).where(equal(pk, ids)).execute();
		}
	}

	/**
	 * 判断数据是否已经存在
	 * @param object
	 * @return
	 */
	@Override
	public boolean exists(P object) {
		return existsById(getId(object));
	}

	/**
	 * 根据ID判断数据是否已经存在
	 * @param id
	 * @return
	 */
	@Override
	public boolean existsById(T id) {
		Field<?>[] pk = pk();
		if (pk != null) {
			return ctx()
					.selectCount()
					.from(table)
					.where(equal(pk, id))
					.fetchOne(0, Integer.class) > 0;
		}
		return false;
	}

	/**
	 * 查询当前表总条数
	 * @return
	 */
	@Override
	public long count() {
		return ctx()
				.selectCount()
				.from(table)
				.fetchOne(0, Long.class);
	}

	/**
	 * 查询当前表所有数据记录
	 * @return
	 */
	@Override
	public List<P> findAll() {
		return ctx().selectFrom(table).fetch(mapper());
	}

	/**
	 * 根据ID查询数据
	 * @param id
	 * @return
	 */
	@Override
	public P findById(T id) {
		Field<?>[] pk = pk();
		if (pk != null){
			return ctx().selectFrom(table).where(equal(pk, id)).fetchOne(mapper());
		}
		return null;
	}

	@Override
	public <Z> List<P> fetchRange(Field<Z> field, Z lowerInclusive, Z upperInclusive) {
		return ctx()
				.selectFrom(table)
				.where(
						lowerInclusive == null
								? upperInclusive == null
								? noCondition()
								: field.le(upperInclusive)
								: upperInclusive == null
								? field.ge(lowerInclusive)
								: field.between(lowerInclusive, upperInclusive))
				.fetch(mapper());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Z> List<P> fetch(Field<Z> field, Z... values) {
		return ctx()
				.selectFrom(table)
				.where(field.in(values))
				.fetch(mapper());
	}

	@Override
	public <Z> P fetchOne(Field<Z> field, Z value) {
		return ctx()
				.selectFrom(table)
				.where(field.equal(value))
				.fetchOne(mapper());
	}


	@Override
	public <Z> Optional<P> fetchOptional(Field<Z> field, Z value) {
		return Optional.ofNullable(fetchOne(field, value));
	}


	@Override
	public  Table<R> getTable() {
		return table;
	}

	@Override
	public Class<P> getType() {
		return type;
	}

	// ------------------------------------------------------------------------
	// XXX: Template methods for generated subclasses
	// ------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	protected T compositeKeyRecord(Object... values) {
		UniqueKey<R> key = table.getPrimaryKey();
		if (key == null){
			return null;
		}

		TableField<R, Object>[] fields = (TableField<R, Object>[]) key.getFieldsArray();
		Record result = this.ctx().newRecord(fields);

		for (int i = 0; i < values.length; i++){
			result.set(fields[i], fields[i].getDataType().convert(values[i]));
		}
		return (T) result;
	}

	// ------------------------------------------------------------------------
	// XXX: Private utility methods
	// ------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	private Condition equal(Field<?>[] pk, T id) {
		if (pk.length == 1) {
			return ((Field<Object>) pk[0]).equal(pk[0].getDataType().convert(id));
		} else {
			return row(pk).equal((Record) id);
		}
	}

	@SuppressWarnings("unchecked")
	private Condition equal(Field<?>[] pk, Collection<T> ids) {
		if (pk.length == 1) {
			if (ids.size() == 1) {
				return equal(pk, ids.iterator().next());
			} else {
				return ((Field<Object>) pk[0]).in(pk[0].getDataType().convert(ids));
			}
		}else {
			return row(pk).in(ids.toArray(EMPTY_RECORD));
		}
	}

	private Field<?>[] pk() {
		UniqueKey<?> key = table.getPrimaryKey();
		return key == null ? null : key.getFieldsArray();
	}

	private List<R> records(Collection<P> objects, boolean forUpdate) {
		List<R> result = new ArrayList<>(objects.size());
		Field<?>[] pk = pk();
		DSLContext ctx;
		IdentityHashMap<R, Object> mapping = null;
		if (!FALSE.equals(settings().isReturnRecordToPojo())) {
			mapping = new IdentityHashMap<>();
			Configuration configuration = this.configuration();
			ctx = configuration.derive(providers(configuration.recordListenerProviders(), mapping)).dsl();
		} else{
			ctx = ctx();
		}

		for (P object : objects) {
			R record = ctx.newRecord(table, object);
			if (mapping != null){
				mapping.put(record, object);
			}
			if (forUpdate && pk != null){
				for (Field<?> field : pk){
					record.changed(field, false);
				}
			}
			this.resetChangedOnNotNull(record);
			result.add(record);
		}
		return result;
	}

	private void resetChangedOnNotNull(Record record) {
		int size = record.size();
		for (int i = 0; i < size; i++){
			if (record.get(i) == null){
				if (!record.field(i).getDataType().nullable()){
					record.changed(i, false);
				}
			}
		}
	}

	private RecordListenerProvider[] providers(final RecordListenerProvider[] providers, final IdentityHashMap<R, Object> mapping) {
		RecordListenerProvider[] result = Arrays.copyOf(providers, providers.length + 1);
		result[providers.length] = new DefaultRecordListenerProvider(new DefaultRecordListener() {
			private final void end(RecordContext ctx) {
				Record record = ctx.record();

				// TODO: [#2536] Use mapper()
				if (record != null) {
					record.into(mapping.get(record));
				}
			}

			@Override
			public final void storeEnd(RecordContext ctx) {
				end(ctx);
			}

			@Override
			public final void insertEnd(RecordContext ctx) {
				end(ctx);
			}

			@Override
			public final void updateEnd(RecordContext ctx) {
				end(ctx);
			}

			@Override
			public final void deleteEnd(RecordContext ctx) {
				end(ctx);
			}
		});

		return result;
	}
}
