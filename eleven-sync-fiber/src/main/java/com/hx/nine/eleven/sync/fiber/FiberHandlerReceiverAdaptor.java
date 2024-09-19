package com.hx.nine.eleven.sync.fiber;

import io.vertx.core.Handler;

/**
 *
 * Represents an object that is both a handler of a particular event and also a receiver of that event.
 * <p>
 * In other words it converts an asynchronous stream of events into a synchronous receiver of events
 *
 */
public interface FiberHandlerReceiverAdaptor<T> extends Handler<T>, FiberReceiver<T> {
}
