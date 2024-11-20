package hx.nine.eleven.thread.pool.executor.pool;

import hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;

import java.util.concurrent.Callable;

public class ThreadCallable implements Callable {

    private ThreadPoolEvent event;

    public ThreadCallable(ThreadPoolEvent event){
        this.event = event;
    }

    @Override
    public Object call() throws Exception {
        event.doing();
        return event.executeCallEvent();
    }
}
