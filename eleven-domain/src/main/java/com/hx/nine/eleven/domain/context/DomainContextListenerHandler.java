package com.hx.nine.eleven.domain.context;

import co.paralleluniverse.fibers.Suspendable;

/**
 * 领域驱动对象上线文处理监听事件
 */
public interface DomainContextListenerHandler {


    /**
     * 上下文监听处理器
     */
    @Suspendable
    Object domainContextProcessing(DomainContext context);

}
