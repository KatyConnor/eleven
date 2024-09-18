package com.hx.etx.sync.fiber;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * 需要结合线程池使用
 * @author wml
 * @date
 */
public abstract class FiberAsyncAdaptor<T> extends HXAbstractFiberAsyncHandler<T> implements Handler<AsyncResult<T>>{


  /**
   * 处理返回结果
   * @param res
   */
  @Override
  public void handle(AsyncResult<T> res) {
    if (res.succeeded()) {
      asyncCompleted(res.result());
    } else {
      asyncFailed(res.cause());
    }
  }
}
