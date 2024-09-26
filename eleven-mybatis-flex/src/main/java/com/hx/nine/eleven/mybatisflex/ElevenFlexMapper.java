package com.hx.nine.eleven.mybatisflex;

import com.mybatisflex.core.mybatis.Mappers;

/**
 * 封装对数据库的简化操作
 * @auth wml
 * @date 2024/9/25
 */
public class ElevenFlexMapper {

	public <T> int insert(T entity) {
		ElevenFlexBaseMapper mapper = Mappers.ofMapperClass(ElevenFlexBaseMapper.class);
		return mapper.insert(entity, false);
	}

	public <T> int insertSelective(T entity) {
		ElevenFlexBaseMapper mapper = Mappers.ofMapperClass(ElevenFlexBaseMapper.class);
		return mapper.insert(entity, true);
	}

	/**
	 * 插入带有主键的实体类，不忽略 null 值，如果主键没有赋值则会抛出
	 * IllegalArgumentException("Entity Primary Key value must not be null.")
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insertWithPk(T entity) {
		ElevenFlexBaseMapper mapper = Mappers.ofMapperClass(ElevenFlexBaseMapper.class);
		return mapper.insertWithPk(entity, false);
	}

	/**
	 * 插入带有主键的实体类，忽略 null 值，如果主键没有赋值则会抛出
	 * IllegalArgumentException("Entity Primary Key value must not be null.")
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public <T> int insertSelectiveWithPk(T entity) {
		ElevenFlexBaseMapper mapper = Mappers.ofMapperClass(ElevenFlexBaseMapper.class);
		return mapper.insertWithPk(entity, true);
	}

}
