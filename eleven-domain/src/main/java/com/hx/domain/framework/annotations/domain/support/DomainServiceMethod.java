package com.hx.domain.framework.annotations.domain.support;

import java.lang.annotation.*;

/**
 * 领域服务通用支撑,作用于服务层方法路由分配
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DomainServiceMethod {

    /**
     * 交易码
     * @return
     */
    String subTradeCode() default "";

    /**
     * 执行方法
     * @return
     */
    String method() default "";
}
