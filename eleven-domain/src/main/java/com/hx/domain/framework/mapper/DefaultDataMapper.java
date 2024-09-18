package com.hx.domain.framework.mapper;

//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
//import com.hx.lang.commons.utils.BeanMapUtil;
//import com.hx.domain.framework.exception.DBExecuteException;
//import com.hx.domain.framework.obj.param.BaseParam;
//import com.hx.domain.framework.obj.po.BasePO;
//import com.hx.domain.framework.service.HXBaseMapper;
//import com.hx.domain.framework.syscode.DomainApplicationSysCode;
//import com.github.yulichang.interfaces.MPJBaseJoin;
//
//import java.io.Serializable;
//import java.util.*;

/**
 *  常用数据库操作
 * @Author wml
 * @Date 2020-06-28
 */
@Deprecated
public abstract class DefaultDataMapper {

//	public static  <M extends HXBaseMapper> M getMapper(Class<M> mapperClass){
//	    return  BeanFactoryLocator.getBean(mapperClass);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> int insert(P po,Class<M> mapperClass) {
//        return getMapper(mapperClass).insert(po);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> int deleteById(Serializable id, Class<M> mapperClass) {
//        return getMapper(mapperClass).deleteById(id);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> int deleteById(P po,Class<M> mapperClass) {
//        return getMapper(mapperClass).deleteById(po);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> int deleteByMap(Map<String, Object> columnMap, Class<M> mapperClass) {
//        return getMapper(mapperClass).deleteByMap(columnMap);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> int delete(Wrapper<P> queryWrapper, Class<M> mapperClass) {
//        return getMapper(mapperClass).delete(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> int deleteBatchIds(Collection<?> idList, Class<M> mapperClass) {
//        return getMapper(mapperClass).deleteBatchIds(idList);
//    }
//
//
//    public static <P extends BasePO,M extends HXBaseMapper> int updateById(P po,Class<M> mapperClass) {
//        UpdateWrapper<P> lqw = new UpdateWrapper<>();
//        setUpdateWrapper(po,lqw,true,mapperClass.getName()+".updateById");
//        return getMapper(mapperClass).update(po,lqw);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> int update(P entity, UpdateWrapper<P> updateWrapper,Class<M> mapperClass) {
//        if (!Optional.ofNullable(entity.getVersion()).isPresent()){
//            // 版本号不能为null
//            throw new DBExecuteException(DomainApplicationSysCode.D0300010002,mapperClass.getName()+".update");
//        }
//        updateWrapper.eq("VERSION",entity.getVersion());
//        entity.update();
//        return getMapper(mapperClass).update(entity,updateWrapper);
//    }
//
//    /**
//     * 需要调用者设置 where version = ?
//     * @param entity
//     * @param updateWrapper
//     * @param mapperClass
//     * @param <P>
//     * @param <M>
//     * @return
//     */
//    public static <P extends BasePO,M extends HXBaseMapper> int update(P entity, Wrapper<P> updateWrapper,Class<M> mapperClass) {
//        if (!Optional.ofNullable(entity.getVersion()).isPresent()){
//            // 版本号不能为null
//            throw new DBExecuteException(DomainApplicationSysCode.D0300010002,mapperClass.getName()+".update");
//        }
//        entity.update();
//        return getMapper(mapperClass).update(entity,updateWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> P selectById(Serializable id,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).selectById(id);
//    }
//
//    public static <P extends BasePO,Q extends BaseParam,M extends HXBaseMapper> List<P> selectBatchIds(Collection<? extends Serializable> idList,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectBatchIds(idList);
//    }
//
//    public static <P extends BasePO,Q extends BaseParam,M extends HXBaseMapper> List<P> selectByMap(Q params,Class<M> mapperClass) {
//        Map<String, Object> columnMap = BeanMapUtil.beanToMap(params);
//        return getMapper(mapperClass).selectByMap(columnMap);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> P selectOne(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).selectOne(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> boolean exists(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).exists(queryWrapper);
//    }
//
//
//    public static <P extends BasePO,M extends HXBaseMapper> Long selectCount(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectCount(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<P> selectList(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectList(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<Map<String, Object>> selectMaps(Wrapper<P> queryWrapper,Class<M> mapperClass){
//        return getMapper(mapperClass).selectMaps(queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<P> selectObjs(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectObjs(queryWrapper);
//    }
//
//    public static <P extends IPage<T>,T extends BasePO,M extends HXBaseMapper> P selectPage(P page, Wrapper<T> queryWrapper, Class<M> mapperClass) {
//        return (P) getMapper(mapperClass).selectPage(page,queryWrapper);
//    }
//
//    public static <P extends IPage<Map<String, Object>>,T extends BasePO,M extends HXBaseMapper> P selectMapsPage(P page,Wrapper<T> queryWrapper,Class<M> mapperClass) {
//        return (P) getMapper(mapperClass).selectMapsPage(page,queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> Integer selectJoinCount(MPJBaseJoin<P> mpjBaseJoin,Class<M> mapperClass){
//        return getMapper(mapperClass).selectJoinCount(mpjBaseJoin);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper,DTO> DTO selectJoinOne(Class<DTO> dtoClass,MPJBaseJoin<P> mpjBaseJoin,Class<M> mapperClass){
//        return (DTO) getMapper(mapperClass).selectJoinOne(dtoClass,mpjBaseJoin);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> Map<String, Object> selectJoinMap(MPJBaseJoin<P> mpjBaseJoin,Class<M> mapperClass){
//        return getMapper(mapperClass).selectJoinMap(mpjBaseJoin);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper,DTO> List<DTO> selectJoinList(Class<DTO> dtoClass,MPJBaseJoin<P> mpjBaseJoin,Class<M> mapperClass){
//        return getMapper(mapperClass).selectJoinList(dtoClass,mpjBaseJoin);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<Map<String, Object>> selectJoinMaps(MPJBaseJoin<P> mpjBaseJoin,Class<M> mapperClass){
//        return getMapper(mapperClass).selectJoinMaps(mpjBaseJoin);
//    }
//
//    public static <T extends BasePO,M extends HXBaseMapper,DTO, P extends IPage<?>> IPage<DTO> selectJoinPage(P page,Class<DTO> dtoClass,MPJBaseJoin<T> mpjBaseJoin,Class<M> mapperClass){
//        return getMapper(mapperClass).selectJoinPage(page,dtoClass,mpjBaseJoin);
//    }
//
//    public static <T extends BasePO,M extends HXBaseMapper,P extends IPage<?>> IPage<Map<String, Object>> selectJoinMapsPage(P page, MPJBaseJoin<T> mpjBaseJoin,Class<M> mapperClass){
//        return getMapper(mapperClass).selectJoinMapsPage(page,mpjBaseJoin);
//    }
//
//
//    public static <P extends BasePO,M extends HXBaseMapper> P selectByIdDeep(Serializable id,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).selectByIdDeep(id);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> P selectByIdDeep(Serializable id,Class<M> mapperClass,SFunction<P, R>... property) {
//        return (P)getMapper(mapperClass).selectByIdDeep(id,property);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> P selectByIdDeep(Serializable id, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).selectByIdDeep(id,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<P> selectBatchIdsDeep(Collection<? extends Serializable> idList,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectBatchIdsDeep(idList);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<P> selectBatchIdsDeep(Collection<? extends Serializable> idList,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).selectBatchIdsDeep(idList,property);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<P> selectBatchIdsDeep(Collection<? extends Serializable> idList,Class<M> mapperClass, List<SFunction<P, R>> property) {
//        return getMapper(mapperClass).selectBatchIdsDeep(idList,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<P> selectByMapDeep(Map<String, Object> columnMap,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectByMapDeep(columnMap);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<P> selectByMapDeep(Map<String, Object> columnMap,Class<M> mapperClass,SFunction<P, R>... property) {
//        return getMapper(mapperClass).selectByMapDeep(columnMap,property);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<P> selectByMapDeep(Map<String, Object> columnMap, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectByMapDeep(columnMap,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> P selectOneDeep(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).selectOneDeep(queryWrapper);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> P selectOneDeep(Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (P)getMapper(mapperClass).selectOneDeep(queryWrapper,property);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> P selectOneDeep(Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (P)getMapper(mapperClass).selectOneDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<P> selectListDeep(Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectListDeep(queryWrapper);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<P> selectListDeep(Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).selectListDeep(queryWrapper,property);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<P> selectListDeep(Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectListDeep(queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper> List<Map<String, Object>> selectMapsDeep(Class<P> clazz, Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectMapsDeep(clazz,queryWrapper);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<Map<String, Object>> selectMapsDeep(Class<P> clazz, Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return getMapper(mapperClass).selectMapsDeep(clazz,queryWrapper,property);
//    }
//
//    public static <R,P extends BasePO,M extends HXBaseMapper> List<Map<String, Object>> selectMapsDeep(Class<P> clazz, Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return getMapper(mapperClass).selectMapsDeep(clazz,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper,E extends IPage<P>> E selectPageDeep(E page, Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (E) getMapper(mapperClass).selectPageDeep(page,queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper,R, E extends IPage<P>> E selectPageDeep(E page, Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (E) getMapper(mapperClass).selectPageDeep(page,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper,R, E extends IPage<P>> E selectPageDeep(E page, Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (E) getMapper(mapperClass).selectPageDeep(page,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper, E extends IPage<Map<String, Object>>> E selectMapsPageDeep(E page, Class<P> clazz, Wrapper<P> queryWrapper,Class<M> mapperClass) {
//        return (E) getMapper(mapperClass).selectMapsPageDeep(page,clazz,queryWrapper);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper,R, E extends IPage<Map<String, Object>>> E selectMapsPageDeep(E page, Class<P> clazz, Wrapper<P> queryWrapper,Class<M> mapperClass, SFunction<P, R>... property) {
//        return (E) getMapper(mapperClass).selectMapsPageDeep(page,clazz,queryWrapper,property);
//    }
//
//    public static <P extends BasePO,M extends HXBaseMapper,R, E extends IPage<Map<String, Object>>> E selectMapsPageDeep(E page, Class<P> clazz, Wrapper<P> queryWrapper, List<SFunction<P, R>> property,Class<M> mapperClass) {
//        return (E) getMapper(mapperClass).selectMapsPageDeep(page,clazz,queryWrapper,property);
//    }
//
//    private static <P extends BasePO> void setUpdateWrapper(P po, UpdateWrapper<P> updateWrapper, boolean id, String sqlId){
//        if (!Optional.ofNullable(po.getId()).isPresent()){
//            // 主键不能为null
//            throw new DBExecuteException(DomainApplicationSysCode.D0300010001,sqlId);
//        }
//
//        if (!Optional.ofNullable(po.getVersion()).isPresent()){
//            // 版本号不能为null
//            throw new DBExecuteException(DomainApplicationSysCode.D0300010002,sqlId);
//        }
//        updateWrapper.eq("VERSION",po.getVersion());
//        po.update();
//        if (id){
//            updateWrapper.eq("ID", po.getId());
//        }
//    }

}
