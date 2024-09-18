package com.hx.domain.framework.obj.domain;

import com.hx.domain.framework.entity.PageEntity;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.jooq.jdbc.tx.JooqTransactionManager;

/**
 * 领域对象 DOMAIN Object
 */
public class Domain {

    /**
     * 领域唯一编号
     */
    private String gid;
    /**
     * 分页查询时，存储
     */
    private PageEntity pageDoEntity;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public PageEntity getPageDoEntity() {
        return pageDoEntity;
    }

    public void setPageDoEntity(PageEntity pageDoEntity) {
        this.pageDoEntity = pageDoEntity;
    }

    /**
     * 手动提交当前事务
     * 当执行store操作后，如果需要手动提交事务，可以调用此方法，然后再重新开启一个事务，此处只是调用begin方法执行开启标记
     * 当正式打开数据库连接时才开启事务通道
     */
    public void commitTransaction(){
        JooqTransactionManager jooqTransactionManager = VertxApplicationContextAware.getBean(JooqTransactionManager.class);
        jooqTransactionManager.commit();
        jooqTransactionManager.begin();
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
