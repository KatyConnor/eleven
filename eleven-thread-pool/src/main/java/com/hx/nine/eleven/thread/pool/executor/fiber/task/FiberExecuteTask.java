package com.hx.nine.eleven.thread.pool.executor.fiber.task;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.SuspendableCallable;

public abstract class FiberExecuteTask<T, R> {//implements SuspendableCallable<R> {
	/**
	 * 使用的线程池组名称
	 */
	private String threadGroupName;

	private T entity;

	public FiberExecuteTask(T entity) {
		if (null != entity) {
			this.entity = entity;
		}
	}

	@Suspendable
	public abstract R run() throws SuspendExecution, InterruptedException;

	public T getEntity() {
		return entity;
	}

	public <E extends FiberExecuteTask<T, R>> E setT(T entity) {
		this.entity = entity;
		return (E) this;
	}

	public String getThreadGroupName() {
		return threadGroupName;
	}

	public <E extends FiberExecuteTask<T, R>> E setThreadGroupName(String threadGroupName) {
		this.threadGroupName = threadGroupName;
		return (E) this;
	}
}
