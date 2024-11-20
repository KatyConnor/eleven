package hx.nine.eleven.domain.obj.ddo;

import hx.nine.eleven.commons.utils.ObjectUtils;

import java.util.Date;

/**
 *  领域值对象,用于接收domain内部发起的异步线程结果集，或者领域对象子领域属性值接收
 */
public class DomainDO<E,D>{

    private E id;
    private D createTime;
    private D updateTime;
    private Long version;
    private Boolean effective;

    public E getId() {
        return id;
    }

    public void setId(E id) {
        this.id = id;
    }

    public D getCreateTime() {
        return createTime;
    }

    public void setCreateTime(D createTime) {
        this.createTime = createTime;
    }

    public D getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(D updateTime) {
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

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
