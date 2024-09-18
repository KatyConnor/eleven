package com.hx.domain.framework.function;

import com.hx.domain.framework.entity.DataMapperPOEntity;
import com.hx.domain.framework.factory.DataMapperFactory;
import com.hx.domain.framework.obj.domain.Domain;
import com.hx.domain.framework.obj.po.BasePO;

/**
 * @Description 通过领域对象 domain 对数据库进行操作
 * @author wangml
 * @Date 2019-10-16
 */
@FunctionalInterface
public interface DataMapperFactroryExecutor<P extends BasePO,D extends Domain,F extends DataMapperFactory> {

    DataMapperPOEntity<P> execute(D doman, F factory);
}
