package com.hx.vertx.file.monitor.task;

import co.paralleluniverse.strands.SuspendableCallable;

public abstract class FiberCallExecuteTask<T> implements SuspendableCallable<T> {

  private String taskId;

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }
}
