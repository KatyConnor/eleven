package com.hx.domain.framework.function;

import com.hx.domain.framework.obj.bo.BaseOrderBO;
import com.hx.domain.framework.obj.domain.Domain;

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
