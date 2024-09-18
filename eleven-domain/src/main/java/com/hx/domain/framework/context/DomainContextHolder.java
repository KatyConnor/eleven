package com.hx.domain.framework.context;

import co.paralleluniverse.fibers.FiberThreadLocal;
import com.hx.vertx.boot.core.NamedThreadLocal;
/**
 * 基于ThreadLocal实现domainConText上下文存储
 * @author wml
 * @date 2022-12-03
 */
public final class DomainContextHolder {

    /**
     * 线程内部传输上下文对象，线程之间不共享，线程安全
     */
    private static final ThreadLocal<DomainContext> DOMAIN_CONTEXT = new NamedThreadLocal<DomainContext>("domain-context") {
        @Override
        protected DomainContext initialValue() {
            return new DomainContext();
        }
    };

    /**
     * 协程内部传输上下文对象，协程之间不共享，协程安全
     */
    private static final FiberThreadLocal<DomainContext> FIBER_DOMAIN_CONTEXT = new FiberThreadLocal<DomainContext>("domain-context") {
        @Override
        protected DomainContext initialValue() {
            return new DomainContext();
        }
    };

    /**
     * 获取当前线程 DomainContext 上下文对象
     * @return
     */
    public static DomainContext getDomainContext(){
        return DOMAIN_CONTEXT.get();
    }

    /**
     * 当前线程添加 DomainContext 上下文对象
     * @param domainContext
     */
    public static void setDomainContext(DomainContext domainContext){
        DOMAIN_CONTEXT.set(domainContext);
    }

    /**
     * 当前线程删除DomainContext上下文
     */
    public static void removeDomainContext(){
        DOMAIN_CONTEXT.remove();
    }

    /**
     * 获取当前协程 DomainContext 上下文对象
     * @return
     */
    public static DomainContext getFiberDomainContext(){
        return FIBER_DOMAIN_CONTEXT.get();
    }

    /**
     * 当前协程添加 DomainContext 上下文对象
     * @param domainContext
     */
    public static void setFiberDomainContext(DomainContext domainContext){
        FIBER_DOMAIN_CONTEXT.set(domainContext);
    }

    /**
     * 当前协程删除DomainContext上下文
     */
    public static void removeFiberDomainContext(){
        FIBER_DOMAIN_CONTEXT.remove();
    }
}
