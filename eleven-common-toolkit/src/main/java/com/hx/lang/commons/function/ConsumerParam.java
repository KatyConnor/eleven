package com.hx.lang.commons.function;

/**
 * @author wangml
 * @Date 2019-09-10
 */
@FunctionalInterface
public interface ConsumerParam<T,P> {

    /**
     *
     * @param t
     * @param p
     */
    void accept(T t,P p);
}
