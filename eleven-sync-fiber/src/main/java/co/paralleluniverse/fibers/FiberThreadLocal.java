package co.paralleluniverse.fibers;

import com.hx.nine.eleven.sync.fiber.Assert;

public class FiberThreadLocal<T> extends TrueThreadLocal<T> {

	private final String name;

	public FiberThreadLocal(String name) {
		Assert.hasText(name, "Name must not be empty");
		this.name = name;
	}

	@Override
	public void remove() {
		final Thread thread = Thread.currentThread();
		final Fiber fiber = Fiber.currentFiber();
		if (fiber != null)
			fiber.restoreThreadLocals(thread);
		try {
			 super.remove();
		} finally {
			if (fiber != null)
				fiber.installFiberLocals(thread);
		}
	}
}
