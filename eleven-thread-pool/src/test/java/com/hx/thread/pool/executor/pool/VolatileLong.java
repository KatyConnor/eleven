package com.hx.thread.pool.executor.pool;

import sun.misc.Contended;

/**
 * @Author mingliang
 * @Date 2018-07-18 9:38
 */
@Contended
public class VolatileLong {
    public volatile long value = 0L;
}
