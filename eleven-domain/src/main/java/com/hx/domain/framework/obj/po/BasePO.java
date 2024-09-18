package com.hx.domain.framework.obj.po;

import com.hx.lang.commons.utils.ObjectUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description 数据库映射类 Project Object
 * @Author wml
 * @Date 2019-02-15
 */
public class BasePO<E> implements Serializable {
    // 公共信息部分
//    @TableId
    private E id;
    private String createTime;
    private String updateTime;
    private Long version;
    private Boolean effective;

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getEffective() {
        return effective;
    }

    public void setEffective(Boolean effective) {
        this.effective = effective;
    }

    public void update(){
        this.updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.version += 1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public  boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if (obj == null){
            return false;
        }

        if (getClass() != obj.getClass()){
            return false;
        }

        if (this.id == null && ((BasePO)obj).id != null) {
            return false;
        } else if (this.id != null && !this.id.equals(((BasePO)obj).id)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
