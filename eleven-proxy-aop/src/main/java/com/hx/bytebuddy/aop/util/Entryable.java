package com.hx.bytebuddy.aop.util;

/**
 * 定义<code>Entry</code>，一般代表键值对
 * @author wml
 * @date 2023-04-06
 */
public interface Entryable<K, V> {

    /**
     * return the key of entry
     *
     * @return key
     */
    K getKey();

    /**
     * return the value of entry
     *
     * @return value
     */
    V getValue();

}
