package hx.nine.eleven.sync.fiber;

import io.vertx.core.Handler;

public abstract class FiberAsyncHandlerAdaptor<T> extends HXAbstractFiberAsyncHandler<T> implements Handler<T> {

	@Override
	public void handle(T res) {
		asyncCompleted(res);
	}
}
