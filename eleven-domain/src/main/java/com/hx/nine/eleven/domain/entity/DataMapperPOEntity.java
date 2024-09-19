package com.hx.nine.eleven.domain.entity;

import com.hx.nine.eleven.domain.obj.po.BasePO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 数据库增删改查时，数据库实体映射，字段映射集合
 * @author wangml
 * @Date 2019-10-15
 */
public class DataMapperPOEntity<P extends BasePO> {

    /**
     * 多表对象集合，每一个元素对应一张
     */
    private Map<String,P> poMap;
    /**
     * 多表对象集合，可以存储表实体集合
     */
    private Map<String,List<P>> poMapList;
    /**
     * 其他类型字段
     */
    private Map<String,Object> property;
    /**
     * 分页数据
     */
    private PageEntity pageDoEntity;

    private boolean successful;

    /**
     * 初始化一个DataMapperEntity对象
     * @return
     */
    public static DataMapperPOEntity build(){
        return new DataMapperPOEntity();
    }

    public Map<String, P> getPoMap() {
        return poMap;
    }

    public DataMapperPOEntity setPoMap(Map<String, P> poMap) {
        this.poMap = poMap;
        return this;
    }

    public Map<String, List<P>> getPoMapList() {
        return poMapList;
    }

    public DataMapperPOEntity setPoMapList(Map<String, List<P>> poMapList) {
        this.poMapList = poMapList;
        return this;
    }

    /**
     * 添加所有property数据集合
     * @param property   被添加的数据集合
     * @return 返回DataMapperEntity本身
     */
    public DataMapperPOEntity setProperty(Map<String, Object> property) {
        if (this.property == null){
            this.property = new HashMap<>();
        }
        this.property.putAll(property);
        return this;
    }

    /**
     * 单个属性添加，field：属性字段，value：属性值
     * @param field  属性字段
     * @param value  属性值
     * @return  返回DataMapperEntity本身
     */
    public DataMapperPOEntity setProperty(String field, Object value) {
        if (this.property == null){
            this.property = new HashMap<>();
        }
        this.property.put(field,value);
        return this;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public DataMapperPOEntity setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public Map<String, Object> getProperty() {
        return property;
    }

    /**
     * 分页实体数据
     * @return
     */
    public PageEntity getPageDoEntity() {
        return pageDoEntity;
    }

    /**
     * 分页实体数据
     * @return
     */
    public DataMapperPOEntity setPageDoEntity(PageEntity pageDoEntity) {
        this.pageDoEntity = pageDoEntity;
        return this;
    }
}
