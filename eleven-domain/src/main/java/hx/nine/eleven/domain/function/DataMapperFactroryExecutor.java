package hx.nine.eleven.domain.function;

import hx.nine.eleven.domain.entity.DataMapperPOEntity;
import hx.nine.eleven.domain.factory.DataMapperFactory;
import hx.nine.eleven.domain.obj.domain.Domain;
import hx.nine.eleven.domain.obj.po.BasePO;

/**
 * @Description 通过领域对象 domain 对数据库进行操作
 * @author wangml
 * @Date 2019-10-16
 */
@FunctionalInterface
public interface DataMapperFactroryExecutor<P extends BasePO,D extends Domain,F extends DataMapperFactory> {

    DataMapperPOEntity<P> execute(D doman, F factory);
}
