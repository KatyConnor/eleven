package com.mybatisflex.core.relation;

import com.hx.nine.eleven.mybatisflex.ElevenFlexBaseMapper;
import com.mybatisflex.core.datasource.DataSourceKey;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.util.ClassUtil;
import com.mybatisflex.core.util.CollectionUtil;
import com.mybatisflex.core.util.StringUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @auth wml
 * @date 2024/9/26
 */
public class ElevenRelationManager {

	public static <E> void queryRelations(ElevenFlexBaseMapper mapper, List<E> entities) {
		try {
			doQueryRelations(mapper, entities, 0, RelationManager.getMaxDepth(),
					RelationManager.getIgnoreRelations(), RelationManager.getQueryRelations());
		} finally {
			com.mybatisflex.core.relation.RelationManager.clearConfigIfNecessary();
		}
	}

	static <E> void doQueryRelations(ElevenFlexBaseMapper mapper, List<E> entities, int currentDepth, int maxDepth, Set<String> ignoreRelations, Set<String> queryRelations) {
		if (!CollectionUtil.isEmpty(entities)) {
			if (currentDepth < maxDepth) {
				Class<E> entityClass = (Class<E>) ClassUtil.getUsefulClass(entities.get(0).getClass());
				List<AbstractRelation> relations = RelationManager.getRelations(entityClass);
				if (!relations.isEmpty()) {
					String currentDsKey = DataSourceKey.get();

					try {
						relations.forEach((relation) -> {
							if (ignoreRelations == null || !ignoreRelations.contains(relation.getSimpleName()) && !ignoreRelations.contains(relation.getName())) {
								if (queryRelations == null || queryRelations.isEmpty() || queryRelations.contains(relation.getSimpleName()) || queryRelations.contains(relation.getName())) {
									String configDsKey = relation.getDataSource();
									if (StringUtil.isBlank(configDsKey) && currentDsKey != null) {
										configDsKey = currentDsKey;
									}

									try {
										if (StringUtil.isNotBlank(configDsKey)) {
											DataSourceKey.use(configDsKey);
										}

										List<Row> mappingRows = null;
										Object targetValues;
										if (relation.isRelationByMiddleTable()) {
											Set selfFieldValues = relation.getSelfFieldValues(entities);
											if (selfFieldValues.isEmpty()) {
												return;
											}

											QueryWrapper queryWrapper = QueryWrapper.create().select().from(new String[]{relation.getJoinTable()});
											if (selfFieldValues.size() > 1) {
												queryWrapper.where(QueryMethods.column(relation.getJoinSelfColumn(), new Object[0]).in(selfFieldValues));
											} else {
												queryWrapper.where(QueryMethods.column(relation.getJoinSelfColumn(), new Object[0]).eq(selfFieldValues.iterator().next()));
											}

											mappingRows = mapper.selectRowsByQuery(queryWrapper);
											if (CollectionUtil.isEmpty(mappingRows)) {
												return;
											}

											targetValues = new HashSet();
											Iterator var13 = mappingRows.iterator();

											while(var13.hasNext()) {
												Row mappingData = (Row)var13.next();
												Object targetValue = mappingData.getIgnoreCase(relation.getJoinTargetColumn());
												if (targetValue != null) {
													((Set)targetValues).add(targetValue);
												}
											}
										} else {
											targetValues = relation.getSelfFieldValues(entities);
										}

										if (!CollectionUtil.isEmpty((Collection)targetValues)) {
											QueryWrapper queryWrapperx = relation.buildQueryWrapper((Set)targetValues);
											List<?> targetObjectList = mapper.selectListByQueryAs(queryWrapperx, relation.isOnlyQueryValueField() ? relation.getTargetEntityClass() : relation.getMappingType());
											if (CollectionUtil.isNotEmpty(targetObjectList)) {
												doQueryRelations(mapper, targetObjectList, currentDepth + 1, maxDepth, ignoreRelations, queryRelations);
												relation.join(entities, targetObjectList, mappingRows);
											}

											return;
										}
									} finally {
										if (StringUtil.isNotBlank(configDsKey)) {
											DataSourceKey.clear();
										}

									}

								}
							}
						});
					} finally {
						if (currentDsKey != null) {
							DataSourceKey.use(currentDsKey);
						}

					}

				}
			}
		}
	}
}
