package hx.nine.eleven.mybatisflex.mapper;

import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.mybatis.Mappers;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
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


	public ElevenFlexMapper(){
	}

	public static ElevenFlexMapper build(){
		return new ElevenFlexMapper();
	}

	public static <T> T of(Class<T> mapperClass){
		return ElevenApplicationContextAware.getBean(mapperClass);
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
		M baseMapper = ElevenApplicationContextAware.getBean(mapper);//Mappers.ofMapperClass(mapper);
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
		M baseMapper =  ElevenApplicationContextAware.getBean(mapper);
		int resCount = 0;
		for (T p :entities){
			int res  = baseMapper.update(p);
			resCount = res > 0?resCount++:resCount;
		}
		return resCount;
	}
}
