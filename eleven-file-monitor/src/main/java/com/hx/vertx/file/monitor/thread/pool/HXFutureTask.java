package com.hx.vertx.file.monitor.thread.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class HXFutureTask<V> extends FutureTask<V> {


    public HXFutureTask(Callable<V> callable) {
        super(callable);
    }

    public HXFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    @Override
    public void run() {
        super.run();
    }

  /**
   * 获取协程处理结果
   * @return
   */
  public Object getCall(){
      return null;
    }
}
