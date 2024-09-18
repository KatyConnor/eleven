package com.hx.domain.framework.function;

import com.hx.domain.framework.factory.DefaultDomainFactory;

/**
 * 领域 domain 执行
 * @author wml
 * @Date 2019-08-27
 */
@FunctionalInterface
public interface DomainExecutor<T> {

    /**
     *
     * @param response
     * @param factory
     * @return
     */
    T execute(T response,DefaultDomainFactory factory);
}
