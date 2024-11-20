package hx.nine.eleven.mybatisflex;

import hx.nine.eleven.mybatisflex.mapper.ElevenFlexBaseMapper;
import com.mybatisflex.core.exception.FlexExceptions;
import com.mybatisflex.core.field.FieldQuery;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.table.TableInfoFactory;
import com.mybatisflex.core.util.ClassUtil;
import com.mybatisflex.core.util.CollectionUtil;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @auth wml
 * @date 2024/9/26
 */
public class EntityFieldManager {

	public static void queryFields(ElevenFlexBaseMapper mapper, Collection<?> entities, Map<String, FieldQuery> fieldQueryMap) {
		Iterator var3 = entities.iterator();

		while(var3.hasNext()) {
			Object entity = var3.next();
			if (entity != null) {
				String className = ClassUtil.getUsefulClass(entity.getClass()).getName();
				fieldQueryMap.forEach((key, fieldQuery) -> {
					if (key.startsWith(className + "#")) {
						QueryWrapper queryWrapper = fieldQuery.getQueryBuilder().build(entity);
						if (queryWrapper != null) {
							Class<?> filedType = fieldQuery.getFieldWrapper().getFieldType();
							Object value;
							Class componentType;
							List objects;
							if (Collection.class.isAssignableFrom(filedType)) {
								componentType = fieldQuery.getFieldWrapper().getMappingType();
								objects = mapper.selectListByQueryAs(queryWrapper, componentType);
								value = getCollectionValue(filedType, objects);
								if ((!Number.class.isAssignableFrom(componentType) || !String.class.isAssignableFrom(componentType) || !Map.class.isAssignableFrom(componentType)) && !fieldQuery.isPrevent()) {
									queryFields(mapper, (Collection)value, fieldQueryMap);
								}
							} else if (Map.class.isAssignableFrom(filedType)) {
								List<Row> rows = mapper.selectRowsByQuery(queryWrapper);
								if (rows != null && !rows.isEmpty() && rows.get(0) != null) {
									value = getMapValue(filedType, (Map)rows.get(0));
								} else {
									value = new HashMap();
								}
							} else if (filedType.isArray()) {
								componentType = filedType.getComponentType();
								objects = mapper.selectListByQueryAs(queryWrapper, componentType);
								value = getArrayValue(componentType, objects);
							} else if (TableInfoFactory.defaultSupportColumnTypes.contains(filedType)) {
								value = mapper.selectObjectByQueryAs(queryWrapper, filedType);
							} else {
								value = mapper.selectOneByQueryAs(queryWrapper, filedType);
								if (!fieldQuery.isPrevent()) {
									queryFields(mapper, Collections.singletonList(value), fieldQueryMap);
								}
							}

							if (value != null) {
								fieldQuery.getFieldWrapper().set(value, entity);
							}

						}
					}
				});
			}
		}

	}

	private static Object getCollectionValue(Class<?> fieldType, Collection value) {
		if (value == null) {
			if (fieldType == List.class) {
				return Collections.emptyList();
			} else {
				return fieldType == Set.class ? Collections.emptySet() : ClassUtil.newInstance(fieldType);
			}
		} else if (ClassUtil.canInstance(fieldType.getModifiers())) {
			Collection collection = (Collection)ClassUtil.newInstance(fieldType);
			collection.addAll(value);
			return collection;
		} else if (List.class.isAssignableFrom(fieldType)) {
			return value;
		} else if (Set.class.isAssignableFrom(fieldType)) {
			return new HashSet(value);
		} else {
			throw FlexExceptions.wrap("Unsupported collection type: " + fieldType, new Object[0]);
		}
	}

	private static Object getMapValue(Class<?> fieldType, Map value) {
		if (ClassUtil.canInstance(fieldType.getModifiers())) {
			Map map = (Map)ClassUtil.newInstance(fieldType);
			map.putAll(value);
			return map;
		} else {
			return new HashMap(value);
		}
	}

	private static <T> Object getArrayValue(Class<?> componentType, List<T> list) {
		if (CollectionUtil.isEmpty(list)) {
			return Array.newInstance(componentType, 0);
		} else {
			T[] array = (T[]) Array.newInstance(componentType, list.size());

			for(int rows = 0; rows < list.size(); ++rows) {
				array[rows] = list.get(rows);
			}

			return array;
		}
	}
}
