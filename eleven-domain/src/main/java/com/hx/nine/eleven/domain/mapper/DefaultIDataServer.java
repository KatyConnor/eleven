package com.hx.nine.eleven.domain.mapper;

//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
//import com.hx.nine.eleven.domain.obj.po.BasePO;
//import com.hx.nine.eleven.domain.service.HXBaseService;
//import com.github.yulichang.base.MPJBaseMapper;
//import com.github.yulichang.interfaces.MPJBaseJoin;
//import java.io.Serializable;
//import java.util.*;
//import java.util.function.Function;


@Deprecated
public abstract class DefaultIDataServer {

//    public static  <M extends HXBaseService> M getMapper(Class<M> mapperClass){
//        return  BeanFactoryLocator.getBean(mapperClass);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean save(P po, Class<M> mapperClass) {
//        return getMapper(mapperClass).save(po);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean saveBatch(Collection<P> entityList, Class<M> mapperClass) {
//        return getMapper(mapperClass).saveBatch(entityList);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean saveBatch(Collection<P> entityList, int batchSize, Class<M> mapperClass){
//        return getMapper(mapperClass).saveBatch(entityList,batchSize);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean saveOrUpdateBatch(Collection<P> entityList, Class<M> mapperClass) {
//        return getMapper(mapperClass).saveOrUpdateBatch(entityList);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean saveOrUpdateBatch(Collection<P> entityList, int batchSize, Class<M> mapperClass){
//        return getMapper(mapperClass).saveOrUpdateBatch(entityList,batchSize);
//    }
//
//    public static <M extends HXBaseService> boolean removeById(Serializable id, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeById(id);
//    }
//
//    public static <M extends HXBaseService> boolean removeById(Serializable id, boolean useFill, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeById(id,useFill);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean removeById(P entity, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeById(entity);
//    }
//
//    public static <M extends HXBaseService> boolean removeByMap(Map<String, Object> columnMap, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeByMap(columnMap);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean remove(Wrapper<P> queryWrapper, Class<M> mapperClass) {
//        return getMapper(mapperClass).remove(queryWrapper);
//    }
//
//    public static <M extends HXBaseService> boolean removeByIds(Collection<?> list, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeByIds(list);
//    }
//
//    public static <M extends HXBaseService> boolean removeByIds(Collection<?> list, boolean useFill, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeByIds(list,useFill);
//    }
//
//    public static <M extends HXBaseService> boolean removeBatchByIds(Collection<?> list, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeBatchByIds(list);
//    }
//
//    public static <M extends HXBaseService> boolean removeBatchByIds(Collection<?> list, boolean useFill, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeBatchByIds(list,useFill);
//    }
//
//    public static <M extends HXBaseService> boolean removeBatchByIds(Collection<?> list, int batchSize, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeBatchByIds(list,batchSize);
//    }
//
//    public static <M extends HXBaseService> boolean removeBatchByIds(Collection<?> list, int batchSize, boolean useFill, Class<M> mapperClass) {
//        return getMapper(mapperClass).removeBatchByIds(list,batchSize,useFill);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean updateById(P entity, Class<M> mapperClass) {
//        return getMapper(mapperClass).updateById(entity);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean update(Wrapper<P> updateWrapper, Class<M> mapperClass) {
//        return update(null,updateWrapper,mapperClass);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean update(P entity, Wrapper<P> updateWrapper, Class<M> mapperClass) {
//        return getMapper(mapperClass).update(entity,updateWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean updateBatchById(Collection<P> entityList, Class<M> mapperClass) {
//        return getMapper(mapperClass).updateBatchById(entityList);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean updateBatchById(Collection<P> entityList, int batchSize, Class<M> mapperClass){
//        return getMapper(mapperClass).updateBatchById(entityList,batchSize);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean saveOrUpdate(P entity, Class<M> mapperClass){
//        return getMapper(mapperClass).saveOrUpdate(entity);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> P getById(Serializable id, Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getById(id);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<P> listByIds(Collection<? extends Serializable> idList, Class<M> mapperClass) {
//        return getMapper(mapperClass).listByIds(idList);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<P> listByMap(Map<String, Object> columnMap, Class<M> mapperClass) {
//        return getMapper(mapperClass).listByMap(columnMap);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> P getOne(Wrapper<P> queryWrapper, Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getOne(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> P getOne(Wrapper<P> queryWrapper, boolean throwEx, Class<M> mapperClass){
//        return (P)getMapper(mapperClass).getOne(queryWrapper,throwEx);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> Map<String, Object> getMap(Wrapper<P> queryWrapper, Class<M> mapperClass){
//        return getMapper(mapperClass).getMap(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,V> V getObj(Wrapper<P> queryWrapper, Function<? super Object, V> mapper, Class<M> mapperClass){
//        return (V)getMapper(mapperClass).getObj(queryWrapper,mapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> long count(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).count(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<P> list(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).list(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,E extends IPage<P>> E page(E page, Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (E) getMapper(mapperClass).page(page,queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,E extends IPage<P>> E page(E page,Class<M> mapperClass) {
//        return (E) getMapper(mapperClass).page(page);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<Map<String, Object>> listMaps(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).listMaps(queryWrapper);
//    }
//
//    public static <M extends HXBaseService,V> List<V> listObjs(Function<? super Object, V> mapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).listObjs(mapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<Object> listObjs(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).listObjs(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,V> List<V> listObjs(Wrapper<P> queryWrapper, Function<? super Object, V> mapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).listObjs(queryWrapper,mapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageMaps(page,queryWrapper);
//    }
//
//    public static <M extends HXBaseService,E extends IPage<Map<String, Object>>> E pageMaps(E page,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageMaps(page);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> boolean saveOrUpdate(P entity, Wrapper<P> updateWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).saveOrUpdate(entity,updateWrapper);
//    }
//
//
//    public static <P extends BasePO,M extends HXBaseService> Integer selectJoinCount(MPJBaseJoin<P> wrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectJoinCount(wrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,DTO> DTO selectJoinOne(Class<DTO> clazz, MPJBaseJoin<P> wrapper,Class<M> mapperClass) {
//        return (DTO)getMapper(mapperClass).selectJoinOne(clazz,wrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,DTO> List<DTO> selectJoinList(Class<DTO> clazz, MPJBaseJoin<P> wrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectJoinList(clazz,wrapper);
//    }
//
//    public static <T extends BasePO,M extends HXBaseService,DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, Class<DTO> clazz, MPJBaseJoin<T> wrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectJoinListPage(page,clazz,wrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> Map<String, Object> selectJoinMap(MPJBaseJoin<P> wrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectJoinMap(wrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<Map<String, Object>> selectJoinMaps(MPJBaseJoin<P> wrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectJoinMaps(wrapper);
//    }
//
//    public static <T extends BasePO,M extends HXBaseService,P extends IPage<Map<String, Object>>> IPage<Map<String, Object>> selectJoinMapsPage(P page, MPJBaseJoin<T> wrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectJoinMapsPage(page,wrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> P getByIdDeep(Serializable id,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getByIdDeep(id);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> P getByIdDeep(Serializable id,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (P)getMapper(mapperClass).getByIdDeep(id,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> P  getByIdDeep(Serializable id, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getByIdDeep(id,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<P> listByIdsDeep(Collection<? extends Serializable> idList,Class<M> mapperClass) {
//        return getMapper(mapperClass).listByIdsDeep(idList);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listByIdsDeep(Collection<? extends Serializable> idList,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).listByIdsDeep(idList,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listByIdsDeep(Collection<? extends Serializable> idList, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).listByIdsDeep(idList,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<P> listByMapDeep(Map<String, Object> columnMap,Class<M> mapperClass) {
//        return getMapper(mapperClass).listByMapDeep(columnMap);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listByMapDeep(Map<String, Object> columnMap,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).listByMapDeep(columnMap,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listByMapDeep(Map<String, Object> columnMap, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).listByMapDeep(columnMap,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> P getOneDeep(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getOneDeep(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> P getOneDeep(Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (P)getMapper(mapperClass).getOneDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> P getOneDeep(Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getOneDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> P getOneDeep(Wrapper<P> queryWrapper, boolean throwEx,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getOneDeep(queryWrapper,throwEx);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> P getOneDeep(Wrapper<P> queryWrapper, boolean throwEx,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (P)getMapper(mapperClass).getOneDeep(queryWrapper,throwEx,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> P getOneDeep(Wrapper<P> queryWrapper, boolean throwEx, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).getOneDeep(queryWrapper,throwEx,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> Map<String, Object> getMapDeep(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).getMapDeep(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> Map<String, Object> getMapDeep(Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).getMapDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> Map<String, Object> getMapDeep(Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).getMapDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<P> listDeep(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).listDeep(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listDeep(Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).listDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listDeep(Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).listDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listDeep(Class<M> mapperClass,SFunction<P, R>... property) {
//        return getMapper(mapperClass).listDeep(property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<P> listDeep(List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).listDeep(property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,E extends IPage<P>> E pageDeep(E page, Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageDeep(page,queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R,E extends IPage<P>> E pageDeep(E page, Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (E)getMapper(mapperClass).pageDeep(page,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R,E extends IPage<P>> E pageDeep(E page, Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageDeep(page,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,E extends IPage<P>> E pageDeep(E page,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageDeep(page);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R,E extends IPage<P>> E pageDeep(E page,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (E)getMapper(mapperClass).pageDeep(page,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R,E extends IPage<P>> E pageDeep(E page, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageDeep(page,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService> List<Map<String, Object>> listMapsDeep(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).listMapsDeep(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<Map<String, Object>> listMapsDeep(Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).listMapsDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<Map<String, Object>> listMapsDeep(Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).listMapsDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<Map<String, Object>> listMapsDeep(Class<M> mapperClass,SFunction<P, R>... property) {
//        return getMapper(mapperClass).listMapsDeep(property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R> List<Map<String, Object>> listMapsDeep(List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).listMapsDeep(property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageMapsDeep(page,queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (E)getMapper(mapperClass).pageMapsDeep(page,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageMapsDeep(page,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,E extends IPage<Map<String, Object>>> E pageMapsDeep(E page,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageMapsDeep(page);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (E)getMapper(mapperClass).pageMapsDeep(page,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseService,R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (E)getMapper(mapperClass).pageMapsDeep(page,property);
//    }

}
