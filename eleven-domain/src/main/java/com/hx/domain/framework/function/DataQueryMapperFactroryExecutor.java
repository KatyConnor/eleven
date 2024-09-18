package com.hx.domain.framework.function;

import com.hx.domain.framework.entity.DataMapperPOEntity;
import com.hx.domain.framework.factory.DataMapperFactory;
import com.hx.domain.framework.obj.po.BasePO;

/**
 * @Description 数据库查询操作
 * @author wangml
 * @Date 2019-11-21
 */
@FunctionalInterface
public interface DataQueryMapperFactroryExecutor<P extends BasePO,F extends DataMapperFactory> {

    /**
     * 通过领域对象对数据进行插叙操作
     * @param factory  查询逻辑实现 factory
     * @return
     */
    DataMapperPOEntity<P> execute(F factory);
}
