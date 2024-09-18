package com.hx.lang.commons.function;

import java.util.List;

/**
 * @author wangml
 * @Date 2019-09-10
 */
@FunctionalInterface
public interface ConsumerParams<T,P> {

    /**
     *
     * @param t
     * @param ps
     */
    void accept(T t, List<P> ps);
}
