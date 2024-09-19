package com.hx.nine.eleven.commons.entity;


import com.hx.nine.eleven.commons.utils.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 对象关系映射
 * @author wangml
 * @Date 2019-09-11
 */
public class BeanMappingEntity {

    private List<MappingEntity> mappingEntitys;

    private BeanMappingEntity(){
        this.mappingEntitys = new ArrayList<>();
    }

    public static BeanMappingEntity build(){
        return new BeanMappingEntity();
    }

    public BeanMappingEntity mapping(String sourceField, String targetField){
        MappingEntity mappingEntity = Builder.of(MappingEntity::new).with(MappingEntity::setSourceField,sourceField)
                .with(MappingEntity::setTargetField,targetField).build();
        this.mappingEntitys.add(mappingEntity);
        return this;
    }

    public BeanMappingEntity ignoreProperty(String ignoreField){
        MappingEntity mappingEntity = Builder.of(MappingEntity::new).with(MappingEntity::setIgnoreField,ignoreField).build();
        this.mappingEntitys.add(mappingEntity);
        return this;
    }
    public BeanMappingEntity ignoreProperties(String[] ignoreField){
        Arrays.asList(ignoreField).forEach(field->{
            MappingEntity mappingEntity = Builder.of(MappingEntity::new).with(MappingEntity::setIgnoreField,field).build();
            this.mappingEntitys.add(mappingEntity);
        });
        return this;
    }

    public List<MappingEntity> getMappingEntitys() {
        return mappingEntitys;
    }

    public class MappingEntity{

        private String sourceField;
        private String targetField;
        private String ignoreField;

        public String getSourceField() {
            return sourceField;
        }

        public void setSourceField(String sourceField) {
            this.sourceField = sourceField;
        }

        public String getTargetField() {
            return targetField;
        }

        public void setTargetField(String targetField) {
            this.targetField = targetField;
        }

        public String getIgnoreField() {
            return ignoreField;
        }

        public void setIgnoreField(String ignoreField) {
            this.ignoreField = ignoreField;
        }
    }
}
