package com.hx.lang.commons.function;

/**
 * @author wangml
 * @Date 2019-08-30
 */
@FunctionalInterface
public interface IAction<T> {

    /**
     *
     * @return
     */
    T run();
}
