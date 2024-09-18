package com.hx.vertx.file.monitor.thread.pool;

import com.hx.vertx.file.monitor.task.FiberExecuteTask;

import java.util.concurrent.Callable;

public class ThreadCallable<T extends FiberExecuteTask> implements Callable {

  private T task;

    public  ThreadCallable(T task){
        this.task = task;
    }

    @Override
    public Object call() throws Exception {
//        event.doing();
//        return event.runCall();
      return null;
    }
}
