package com.hx.nine.eleven.mybatisflex;

import com.hx.nine.eleven.mybatisflex.mapper.ElevenFlexBaseMapper;
import com.mybatisflex.core.field.FieldQuery;
import com.mybatisflex.core.field.FieldQueryBuilder;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.CPI;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.ElevenRelationManager;
import com.mybatisflex.core.relation.RelationManager;
import com.mybatisflex.core.util.ArrayUtil;
import com.mybatisflex.core.util.CollectionUtil;
import com.mybatisflex.core.util.MapperUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @auth wml
 * @date 2024/9/25
 */
public class ElevenFlexMapperUtil {

	@SafeVarargs
	public static <T, R> Page<R> doPaginate(ElevenFlexBaseMapper mapper, Page<R> page, QueryWrapper queryWrapper, Class<R> asType, boolean withRelations, Consumer<FieldQueryBuilder<R>>... consumers) {
		Long limitRows = CPI.getLimitRows(queryWrapper);
		Long limitOffset = CPI.getLimitOffset(queryWrapper);

		Page var13;
		try {
			if (page.getTotalRow() < 0L) {
				QueryWrapper countQueryWrapper;
				if (page.needOptimizeCountQuery()) {
					countQueryWrapper = MapperUtil.optimizeCountQueryWrapper(queryWrapper);
				} else {
					countQueryWrapper = MapperUtil.rawCountQueryWrapper(queryWrapper);
				}

				CPI.setLimitRows(countQueryWrapper, (Long)null);
				CPI.setLimitOffset(countQueryWrapper, (Long)null);
				page.setTotalRow(mapper.selectCountByQuery(countQueryWrapper));
			}

			if (page.hasRecords()) {
				queryWrapper.limit(page.offset(), page.getPageSize());
				List records;
				if (asType != null) {
					records = mapper.selectListByQueryAs(queryWrapper, asType);
				} else {
					records = mapper.selectListByQuery(queryWrapper);
				}

				if (withRelations) {
					queryRelations(mapper, records);
				}

				queryFields(mapper, records, consumers);
				page.setRecords(records);
				Page var9 = page;
				return var9;
			}

			if (withRelations) {
				RelationManager.clearConfigIfNecessary();
			}
			var13 = page;
		} finally {
			CPI.setLimitRows(queryWrapper, limitRows);
			CPI.setLimitOffset(queryWrapper, limitOffset);
		}
		return var13;
	}

	public static <E> List<E> queryRelations(ElevenFlexBaseMapper mapper, List<E> entities) {
		ElevenRelationManager.queryRelations(mapper, entities);
		return entities;
	}

	public static <E> E queryRelations(ElevenFlexBaseMapper mapper, E entity) {
		if (entity != null) {
			queryRelations(mapper, Collections.singletonList(entity));
		} else {
			RelationManager.clearConfigIfNecessary();
		}

		return entity;
	}

	public static <R> void queryFields(ElevenFlexBaseMapper mapper, List<R> list, Consumer<FieldQueryBuilder<R>>[] consumers) {
		if (!CollectionUtil.isEmpty(list) && !ArrayUtil.isEmpty(consumers) && consumers[0] != null) {
			Map<String, FieldQuery> fieldQueryMap = new HashMap();
			Consumer[] var4 = consumers;
			int var5 = consumers.length;

			for(int var6 = 0; var6 < var5; ++var6) {
				Consumer<FieldQueryBuilder<R>> consumer = var4[var6];
				FieldQueryBuilder<R> fieldQueryBuilder = new FieldQueryBuilder();
				consumer.accept(fieldQueryBuilder);
				FieldQuery fieldQuery = fieldQueryBuilder.build();
				String className = fieldQuery.getEntityClass().getName();
				String fieldName = fieldQuery.getFieldName();
				String mapKey = className + '#' + fieldName;
				fieldQueryMap.put(mapKey, fieldQuery);
			}

			EntityFieldManager.queryFields(mapper, list, fieldQueryMap);
		}
	}
}
