package hx.nine.eleven.mybatisflex.mapper;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 封装对数据库的简化操作
 * @auth wml
 * @date 2024/9/25
 */
public class ElevenFlexMapper {

	private static final int DEFAULT_BATCH_SIZE = 1000;

	private ElevenFlexBaseMapper mapper;

	public ElevenFlexMapper(){
		this.mapper = Mappers.ofMapperClass(ElevenFlexBaseMapper.class);
	}

	public static ElevenFlexMapper build(){
		return new ElevenFlexMapper();
	}

	public static <T extends ElevenBaseMapper> T getMapper(Class<T> mapperClass){
		return Mappers.ofMapperClass(mapperClass);
	}

	/**
	 * 批量插入数据
	 * @param entities  要写入的数据集合
	 * @param mapper    操作数据库的mapper class
	 * @param <T>
	 * @param <M>
	 * @return
	 */
	public static <T,M extends ElevenBaseMapper> int batchInsert(Collection<T> entities,Class<M> mapper) {
		M baseMapper = Mappers.ofMapperClass(mapper);
		return baseMapper.insertBatch(entities);
	}

	/**
	 * 批量修改数据
	 * @param entities 要修改的数据源
	 * @param mapper   操作数据库的mapper class
	 * @param <T>
	 * @param <M>
	 * @return
	 */
	public static <T,M extends ElevenBaseMapper> int batchUpdate(Collection<T> entities,Class<M> mapper) {
		M baseMapper = Mappers.ofMapperClass(mapper);
		int resCount = 0;
		for (T p :entities){
			int res  = baseMapper.update(p);
			resCount = res > 0?resCount++:resCount;
		}
		return resCount;
	}

	/**
	 * 插入实体类数据，不忽略 null 值
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insert(T entity) {
		return this.mapper.insert(entity, false);
	}

	/**
	 * 插入实体类数据，但是忽略 null 的数据，只对有值的内容进行插入。这样的好处是数据库已经配置了一些默认值，这些默认值才会生效。
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insertSelective(T entity) {
		return this.mapper.insert(entity, true);
	}

	/**
	 * 插入带有主键的实体类，不忽略 null 值，如果主键没有赋值则会抛出
	 * IllegalArgumentException("Entity Primary Key value must not be null.")
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insertWithPk(T entity) {
		return this.mapper.insertWithPk(entity, false);
	}

	/**
	 * 插入带有主键的实体类，忽略 null 值，如果主键没有赋值则会抛出
	 * IllegalArgumentException("Entity Primary Key value must not be null.")
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insertSelectiveWithPk(T entity) {
		return this.mapper.insertWithPk(entity, true);
	}

	/**
	 * 批量插入实体类数据，只会根据第一条数据来构建插入的字段内容。
	 * @param entities
	 * @param <T>
	 * @return
	 */
	public <T> int insertBatch(Collection<T> entities) {
		return this.mapper.insertBatch(entities);
	}

	/**
	 * 批量插入实体类数据，忽略 null 值,默认按照1000切分插入。
	 * @param entities
	 * @param <T>
	 * @return
	 */
	public <T> int insertBatchSelective(Collection<T> entities) {
		return this.mapper.insertBatchSelective(entities, DEFAULT_BATCH_SIZE);
	}

	/**
	 * 插入或者更新，若主键有值，则更新，若没有主键值，则插入，插入或者更新都不会忽略 null 值。
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insertOrUpdate(T entity){
		return this.mapper.insertOrUpdate(entity);
	}

	/**
	 * 插入或者更新，若主键有值，则更新，若没有主键值，则插入，插入或者更新都会忽略 null 值
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insertOrUpdateSelective(T entity){
		return this.mapper.insertOrUpdateSelective(entity);
	}

	/**
	 * 根据实体主键来删除数据。相比deleteById(id)，此方法更便于对复合主键实体类的删除。
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int delete(T entity) {
		return this.mapper.delete(entity);
	}

	/**
	 * 根据主键删除数据。如果是多个主键的情况下，需要传入数组，例如：new Integer[]{100,101}
	 * @param id
	 * @return
	 */
	public int deleteById(Serializable id){
		return this.mapper.deleteById(id);
	}

	/**
	 * 根据多个主键批量删除数据
	 * @param ids
	 * @return
	 */
	public int deleteBatchByIds(Collection<? extends Serializable> ids){
		return this.mapper.deleteBatchByIds(ids);
	}

	/**
	 * 根据 Map 构建的条件来删除数据
	 * @param whereConditions
	 * @return
	 */
	public int deleteByMap(Map<String, Object> whereConditions) {
		return this.mapper.deleteByMap(whereConditions);
	}

	/**
	 * 根据查询条件来删除数据
	 * @param whereConditions
	 * @return
	 */
	public int deleteByCondition(QueryCondition whereConditions) {
		return this.mapper.deleteByCondition(whereConditions);
	}

	/**
	 * 根据查询条件来删除数据
	 * @param queryWrapper
	 * @return
	 */
	public int deleteByQuery(QueryWrapper queryWrapper){
		return this.mapper.deleteByQuery(queryWrapper);
	}

	/**
	 * 根据主键来更新数据，若实体类属性数据为 null，该属性不会更新到数据库
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public  <T> int update(T entity){
		return this.mapper.update(entity);
	}

	/**
	 * 根据 Map 构建的条件来更新数据
	 * @param entity
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> int updateByMap(T entity, Map<String, Object> whereConditions){
		return this.mapper.updateByMap(entity,whereConditions);
	}

	/**
	 *  根据 Map 构建的条件来更新数据,可通过 ignoreNulls 参数指定是否忽略 null字段更新
	 * @param entity
	 * @param ignoreNulls
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> int updateByMap(T entity, boolean ignoreNulls, Map<String, Object> whereConditions){
		return this.mapper.updateByMap(entity,ignoreNulls,whereConditions);
	}

	/**
	 *  根据查询条件来更新数据
	 * @param entity
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> int updateByCondition(T entity, QueryCondition whereConditions){
		return this.mapper.updateByCondition(entity,whereConditions);
	}

	/**
	 * 根据查询条件来更新数据,可通过 ignoreNulls 参数指定是否忽略 null字段更新
	 * @param entity
	 * @param ignoreNulls
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> int updateByCondition(T entity, boolean ignoreNulls, QueryCondition whereConditions){
		return this.mapper.updateByCondition(entity,ignoreNulls,whereConditions);
	}

	/**
	 * 根据查询条件来更新数据
	 * @param entity
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	public <T> int updateByQuery(T entity, QueryWrapper queryWrapper){
		return this.mapper.updateByQuery(entity,queryWrapper);
	}

	/**
	 * 根据查询条件来更新数据
	 * @param entity
	 * @param ignoreNulls
	 * @param query
	 * @param <T>
	 * @return
	 */
	public <T> int updateByQuery( T entity, boolean ignoreNulls, QueryWrapper query){
		return this.mapper.updateByQuery(entity,ignoreNulls,query);
	}

	/**
	 * 根据实体主键查询数据，便于对复合主键实体类的查询
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> T selectOneByEntityId(T entity) {
		return this.mapper.selectOneByEntityId(entity);
	}

	/**
	 * 根据主键查询数据
	 * @param primaryValue
	 * @param <T>
	 * @return
	 */
	public <T> T selectOneById(Serializable primaryValue){
		return this.mapper.selectOneById(primaryValue);
	}

	/**
	 * 根据 Map 构建的条件来查询数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> T selectOneByMap(Map<String, Object> whereConditions){
		return this.mapper.selectOneByMap(whereConditions);
	}

	/**
	 * 根据查询条件查询数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> T selectOneByCondition(QueryCondition whereConditions) {
		return this.mapper.selectOneByCondition(whereConditions);
	}

	/**
	 * 根据查询条件来查询 1 条数据
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	public <R> R selectOneByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		return this.mapper.selectOneByQueryAs(queryWrapper,asType);
	}

	/**
	 * 根据 Map 构建的条件来查询 1 条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> T selectOneWithRelationsByMap(Map<String, Object> whereConditions) {
		return this.mapper.selectOneWithRelationsByMap(whereConditions);
	}

	/**
	 * 根据查询条件查询 1 条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> T selectOneWithRelationsByCondition(QueryCondition whereConditions) {
		return this.mapper.selectOneWithRelationsByCondition(whereConditions);
	}

	public  <T> T selectOneWithRelationsById(Serializable id){
		return this.mapper.selectOneWithRelationsById(id);
	}

	public <R> R selectOneWithRelationsByIdAs(Serializable id, Class<R> asType){
		return this.mapper.selectOneWithRelationsByIdAs(id,asType);
	}

	public <R> R selectOneWithRelationsByQueryAs(QueryWrapper queryWrapper, Class<R> asType){
		return this.mapper.selectOneWithRelationsByQueryAs(queryWrapper,asType);
	}

	/**
	 * 根据多个主键来查询多条数据
	 * @param ids
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListByIds(Collection<? extends Serializable> ids){
		return this.mapper.selectListByIds(ids);
	}

	/**
	 * 根据 Map 来构建查询条件，查询多条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListByMap(Map<String, Object> whereConditions){
		return this.mapper.selectListByMap(whereConditions);
	}

	/**
	 * 根据 Map 来构建查询条件，查询多条数据
	 * @param whereConditions
	 * @param count
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListByMap(Map<String, Object> whereConditions, Long count){
		return this.mapper.selectListByMap(whereConditions,count);
	}

	/**
	 * 根据查询条件查询多条数据
	 * @param whereConditions
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListByCondition(QueryCondition whereConditions){
		return this.mapper.selectListByCondition(whereConditions);
	}

	/**
	 * 根据查询条件查询多条数据
	 * @param whereConditions
	 * @param count
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListByCondition(QueryCondition whereConditions, Long count){
		return this.mapper.selectListByCondition(whereConditions,count);
	}

	/**
	 * 根据查询条件查询数据列表
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListByQuery(QueryWrapper queryWrapper){
		return this.mapper.selectListByQuery(queryWrapper);
	}

	/**
	 * 根据查询条件查询数据列表
	 * @param queryWrapper
	 * @param consumers
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListByQuery(QueryWrapper queryWrapper, Consumer<FieldQueryBuilder<T>>... consumers){
		return this.mapper.selectListByQuery(queryWrapper,consumers);
	}

	/**
	 * 使用游标查询
	 * 根据查询条件查询游标数据，该方法必须在事务中才能正常使用，非事务下无法获取数据
	 * @param query
	 * @param <T>
	 * @return
	 */
	public <T> Cursor<T> selectCursorByQuery(QueryWrapper query){
		return this.mapper.selectCursorByQuery(query);
	}

	/**
	 *
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	public <R> Cursor<R> selectCursorByQueryAs(QueryWrapper queryWrapper, Class<R> asType){
		return this.mapper.selectCursorByQueryAs(queryWrapper,asType);
	}

	/**
	 * 根据查询条件查询 Row 数据
	 * @param queryWrapper
	 * @return
	 */
	public List<Row> selectRowsByQuery(QueryWrapper queryWrapper){
		return this.mapper.selectRowsByQuery(queryWrapper);
	}

	/**
	 * 根据查询条件查询数据列表，要求返回的数据为 asType。这种场景一般用在 left join 时，有多出了实体类本身的字段内容，可以转换为 dto、vo 等场景
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	public <R> List<R> selectListByQueryAs(QueryWrapper queryWrapper, Class<R> asType){
		return this.mapper.selectListByQueryAs(queryWrapper,asType);
	}

	/**
	 * 根据查询条件查询数据列表，要求返回的数据为 asType 类型
	 * @param queryWrapper
	 * @param asType
	 * @param consumers
	 * @param <R>
	 * @return
	 */
	public <R> List<R> selectListByQueryAs(QueryWrapper queryWrapper, Class<R> asType, Consumer<FieldQueryBuilder<R>>... consumers) {
		return this.mapper.selectListByQueryAs(queryWrapper,asType,consumers);
	}

	/**
	 * 查询实体类及其 Relation 注解字段
	 * @param queryWrapper
	 * @param <T>
	 * @return
	 */
	public <T> List<T> selectListWithRelationsByQuery(QueryWrapper queryWrapper) {
		return this.mapper.selectListWithRelationsByQuery(queryWrapper);
	}

	/**
	 * 查询实体类及其 Relation 注解字段
	 * @param queryWrapper
	 * @param asType
	 * @param <R>
	 * @return
	 */
	public <R> List<R> selectListWithRelationsByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
		return this.mapper.selectListWithRelationsByQueryAs(queryWrapper,asType);
	}

	/**
	 * 查询实体类及其 Relation 注解字段
	 * @param queryWrapper
	 * @param asType
	 * @param consumers
	 * @param <R>
	 * @return
	 */
	public <R> List<R> selectListWithRelationsByQueryAs(QueryWrapper queryWrapper, Class<R> asType, Consumer<FieldQueryBuilder<R>>... consumers) {
		return this.mapper.selectListWithRelationsByQueryAs(queryWrapper,asType,consumers);
	}

	public <T> List<T> selectAll() {
		return this.mapper.selectAll();
	}
}
