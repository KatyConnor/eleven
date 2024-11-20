package hx.nine.eleven.sync.fiber;

import co.paralleluniverse.fibers.*;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.channels.Channel;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 协程处理类
 */
public class HXFiberSync {
	private static final String FIBER_SCHEDULER_CONTEXT_KEY = "__vertx-sync.fiberScheduler";
	private static final Logger LOGGER = LoggerFactory.getLogger(HXFiberSync.class);

	/**
	 * PS: 当前线程必须有正在运行或者活动的协程
	 * 调用异步操作并同步获得结果,在结果可用之前，协程fiber将被阻塞。没有内核线程被阻塞。
	 *
	 * @param consumer 异步处理逻辑程序
	 * @param <T>      返回结果类型
	 * @return return 返回异步处理结果
	 */
	@Suspendable
	public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> consumer) {
		AtomicReference<T> res = new AtomicReference<>();
		if (Fiber.currentFiber() == null) {
			FiberScheduler scheduler = FiberThreadPoolScheduler.build().getFiberScheduler();
			new Fiber<T>(scheduler, () -> {
				try {
					res.set(awaitResultConsumer(consumer));
				} catch (Throwable t) {
					throw new VertxException(t);
				}
			}).start();
			return res.get();
		}
		LOGGER.error("This FiberAsync instance has already been used");
		throw new IllegalStateException("This FiberAsync instance has already been used");
	}

	@Suspendable
	public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> consumer, FiberScheduler scheduler) {
		AtomicReference<T> res = new AtomicReference<>();
		if (Fiber.currentFiber() == null) {
			new Fiber<T>(scheduler, () -> {
				try {
					res.set(awaitResultConsumer(consumer));
				} catch (Throwable t) {
					throw new VertxException(t);
				}
			}).start();
			return res.get();
		}
		LOGGER.error("This FiberAsync instance has already been used");
		throw new IllegalStateException("This FiberAsync instance has already been used");
	}

	/**
	 * @param consumer
	 * @param timeout
	 * @param <T>
	 * @return
	 */
	@Suspendable
	public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> consumer, long timeout) {
		AtomicReference<T> res = new AtomicReference<>();
		if (Fiber.currentFiber() == null) {
			FiberScheduler scheduler = FiberThreadPoolScheduler.build().getFiberScheduler();
			new Fiber<T>(scheduler, () -> {
				try {
					res.set(awaitResultConsumer(consumer, timeout));
				} catch (Throwable t) {
					throw new VertxException(t);
				}
			}).start();
			return res.get();
		}
		LOGGER.error("This FiberAsync instance has already been used");
		throw new IllegalStateException("This FiberAsync instance has already been used");
	}

	/**
	 * @param consumer
	 * @param timeout
	 * @param <T>
	 * @return
	 */
	@Suspendable
	public static <T> T awaitResult(Consumer<Handler<AsyncResult<T>>> consumer, FiberScheduler scheduler, long timeout) {
		AtomicReference<T> res = new AtomicReference<>();
		if (Fiber.currentFiber() == null) {
			new Fiber<T>(scheduler, () -> {
				try {
					res.set(awaitResultConsumer(consumer, timeout));
				} catch (Throwable t) {
					throw new VertxException(t);
				}
			}).start();
			return res.get();
		}
		LOGGER.error("This FiberAsync instance has already been used");
		throw new IllegalStateException("This FiberAsync instance has already been used");
	}

	@Suspendable
	public static <T> T awaitEvent(Consumer<Handler<T>> consumer) {
		try {
			return new FiberAsyncHandlerAdaptor<T>() {
				@Override
				@Suspendable
				protected void requestAsync() {
					try {
						consumer.accept(this);
					} catch (Exception e) {
						throw new VertxException(e);
					}
				}
			}.run();
		} catch (Throwable t) {
			throw new VertxException(t);
		}
	}

	@Suspendable
	public static <T> T awaitEvent(Consumer<Handler<T>> consumer, long timeout) {
		try {
			return new FiberAsyncHandlerAdaptor<T>() {
				@Suspendable
				@Override
				protected void requestAsync() {
					try {
						consumer.accept(this);
					} catch (Exception e) {
						throw new VertxException(e);
					}
				}
			}.run(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException to) {
			return null;
		} catch (Throwable t) {
			throw new VertxException(t);
		}
	}

	@Suspendable
	public static <T> Handler<T> fiberHandler(Handler<T> handler) {
		FiberScheduler scheduler = getContextScheduler();
		return p -> new Fiber<Void>(scheduler, () -> handler.handle(p)).start();
	}

	@Suspendable
	public static <T> void fiberExecute(Consumer<T> consumer, T t) {
		FiberScheduler scheduler = getContextScheduler();
		new Fiber<Void>(scheduler, () -> consumer.accept(t)).start();
	}

	@Suspendable
	public static <T> FiberHandlerReceiverAdaptor<T> streamAdaptor() {
		return new FiberHandlerReceiverAdaptorImpl<>(getContextScheduler());
	}

	@Suspendable
	public static <T> FiberHandlerReceiverAdaptor<T> streamAdaptor(Channel<T> channel) {
		return new FiberHandlerReceiverAdaptorImpl<>(getContextScheduler(), channel);
	}

	@Suspendable
	public static FiberScheduler getContextScheduler() {
		Context context = Vertx.currentContext();
		if (context == null || !context.isEventLoopContext()) {
			LOGGER.warn(context == null ? "Not in context" : "Not on event loop");
			return FiberThreadPoolScheduler.build().getFiberScheduler();
		}
		// We maintain one scheduler per context
		FiberScheduler scheduler = context.get(FIBER_SCHEDULER_CONTEXT_KEY);
		if (scheduler == null) {
			Thread eventLoop = Thread.currentThread();
			scheduler = new FiberExecutorScheduler("vertx.contextScheduler", command -> {
				if (Thread.currentThread() != eventLoop) {
					context.runOnContext(v -> command.run());
				} else {
					// Just run directly
					command.run();
				}
			});
			context.put(FIBER_SCHEDULER_CONTEXT_KEY, scheduler);
		}
		return scheduler;
	}

	@Suspendable
	public static void removeContextScheduler() {
		Context context = Vertx.currentContext();
		if (context != null) {
			context.remove(FIBER_SCHEDULER_CONTEXT_KEY);
		}
	}

	@Suspendable
	private static <T> T awaitResultConsumer(Consumer<Handler<AsyncResult<T>>> consumer) {
		try {
			return new FiberAsyncAdaptor<T>() {
				@Override
				@Suspendable
				protected void requestAsync() {
					try {
						consumer.accept(this);
					} catch (Exception e) {
						throw new VertxException(e);
					}
				}
			}.run();
		} catch (Throwable e) {
			throw new VertxException(e);
		}
	}

	private static <T> T awaitResultConsumer(Consumer<Handler<AsyncResult<T>>> consumer, long timeout) {
		try {
			return new FiberAsyncAdaptor<T>() {
				@Override
				@Suspendable
				protected void requestAsync() {
					try {
						consumer.accept(this);
					} catch (Exception e) {
						throw new VertxException(e);
					}
				}
			}.run(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException to) {
			return null;
		} catch (Throwable e) {
			throw new VertxException(e);
		}
	}

	public static <R> R fiberExecuteCall(SuspendableCallable<R> call) throws SuspendExecution, InterruptedException {
		return call.run();
	}
}
