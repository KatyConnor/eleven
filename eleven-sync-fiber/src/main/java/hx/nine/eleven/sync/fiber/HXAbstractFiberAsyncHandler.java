package hx.nine.eleven.sync.fiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberAsync;
import co.paralleluniverse.fibers.FiberScheduler;

public abstract class HXAbstractFiberAsyncHandler<T> extends FiberAsync<T, Throwable>{

	/**
	 * 如果当前线程没有fiber，创建一个fiber
	 */
	public HXAbstractFiberAsyncHandler(){
//		if (Fiber.currentFiber() == null){
//			// 激活fiber
//			FiberScheduler scheduler = FiberThreadPoolScheduler.build().getFiberScheduler();
//			new Fiber<T>(scheduler);
//		}
	}
}
