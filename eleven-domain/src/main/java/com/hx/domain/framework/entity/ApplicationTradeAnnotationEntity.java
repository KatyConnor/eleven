package com.hx.domain.framework.entity;

import com.hx.lang.commons.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationTradeAnnotationEntity {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationTradeAnnotationEntity.class);

    private String tradeCode;

    private Map<String, List<AnnotationEntity>> annotaionMap;

    public ApplicationTradeAnnotationEntity() {
        this.annotaionMap = new HashMap<>();
    }

    public static ApplicationTradeAnnotationEntity build() {
        return new ApplicationTradeAnnotationEntity();
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public ApplicationTradeAnnotationEntity setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
        return this;
    }

    public Map<String, List<AnnotationEntity>> getAnnotaionMap() {
        return annotaionMap;
    }

    public ApplicationTradeAnnotationEntity setAnnotaionMap(Map<String, List<AnnotationEntity>> annotaionMap) {
        this.annotaionMap = annotaionMap;
        return this;
    }

    /**
     * 如果已经存将会被替换
     * @param annotationEntity
     * @return
     */
    public ApplicationTradeAnnotationEntity putAnnotationEntity(AnnotationEntity annotationEntity) {
        if (this.annotaionMap.containsKey(annotationEntity.getAnnotationName())) {
            List<AnnotationEntity> annotationEntityList = this.annotaionMap.get(annotationEntity.getAnnotationName());
            AnnotationEntity anentity = annotationEntityList.stream().filter(a-> a.getSubTradeCode().equals(annotationEntity.getSubTradeCode())).findFirst().orElse(null);
            if (anentity != null){
                annotationEntityList.remove(anentity);
                LOGGER.warn("交易配置存在重复，[{}],[{}]",anentity,annotationEntity);
            }
            annotationEntityList.add(annotationEntity);
            return this;
        }

        List<AnnotationEntity> annotationEntityList = new ArrayList<>();
        annotationEntityList.add(annotationEntity);
        return this;
    }

    public Class<?> getAnnotationClass(String annotationName,String tradeCode, String subTradeCode){
        if (this.tradeCode.equals(tradeCode)){
            List<AnnotationEntity> annotationEntityList = this.annotaionMap.get(annotationName);
            AnnotationEntity anEntity = annotationEntityList.stream().filter(a -> a.getSubTradeCode().equals(subTradeCode)).findFirst().orElse(null);
            if (anEntity != null){
                return anEntity.getTargetClass();
            }
        }
        return null;
    }


    protected class AnnotationEntity {

        public AnnotationEntity() {
        }

        public AnnotationEntity(String annotationName, String tradeCode, String subTradeCode, Class targetClass) {
            this.annotationName = annotationName;
            this.tradeCode = tradeCode;
            this.subTradeCode = subTradeCode;
            this.targetClass = targetClass;
        }

        private String annotationName;

        private String tradeCode;

        private String subTradeCode;

        private Class targetClass;

        public String getAnnotationName() {
            return annotationName;
        }

        public void setAnnotationName(String annotationName) {
            this.annotationName = annotationName;
        }

        public String getTradeCode() {
            return tradeCode;
        }

        public void setTradeCode(String tradeCode) {
            this.tradeCode = tradeCode;
        }

        public String getSubTradeCode() {
            return subTradeCode;
        }

        public void setSubTradeCode(String subTradeCode) {
            this.subTradeCode = subTradeCode;
        }

        public Class getTargetClass() {
            return targetClass;
        }

        public void setTargetClass(Class targetClass) {
            this.targetClass = targetClass;
        }

        @Override
        public boolean equals(Object obj) {
            if (AnnotationEntity.class.isInstance(obj)){
                AnnotationEntity anObj = (AnnotationEntity) obj;
                return this.subTradeCode.equals(anObj.getSubTradeCode()) &&
                        this.tradeCode.equals(anObj.getTradeCode()) &&
                        this.annotationName.equals(anObj.getAnnotationName()) &&
                        this.targetClass.equals(anObj.getTargetClass())?true:false;
            }
            return false;
        }

        @Override
        public String toString() {
            return ObjectUtils.toString(this);
        }
    }
}
