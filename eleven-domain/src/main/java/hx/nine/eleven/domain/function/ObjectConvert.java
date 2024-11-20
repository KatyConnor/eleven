package hx.nine.eleven.domain.function;

import hx.nine.eleven.domain.obj.bo.BaseOrderBO;
import hx.nine.eleven.domain.obj.domain.Domain;

/**
 * @author wangml
 * @Date 2019-08-28
 */
@FunctionalInterface
public interface ObjectConvert<D extends Domain,B extends BaseOrderBO> {

    /**
     * 根据 BO 生成 DO对象
     * @param source
     * @param target
     * @return
     */
    D  convert(B source,D target);
}
