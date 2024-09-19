package com.hx.nine.eleven.domain.function;

/**
 * @Description 对象实例化
 * @author wangml
 * @Date 2019-08-30
 */
@FunctionalInterface
public interface BeanInstance<T> {

    /**
     *  实例化一个新对象
     * @return
     */
    T create();
}

