package hx.nine.eleven.thread.pool.executor.pool;


import co.paralleluniverse.fibers.SuspendExecution;
import hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;

/**
 * @Author mingliang
 * @Date 2017-12-29 16:23
 */
public class GenerateHtml extends ThreadPoolEvent<String,String> {

    public GenerateHtml(String s) {
        super(s);
    }

    @Override
    public void executeEvent() {
        System.out.println(Thread.currentThread().getThreadGroup()+"--"+Thread.currentThread().getName()+"--执行内容--："+getEntity());
    }

    @Override
    public String run() throws SuspendExecution, InterruptedException {
        return null;
    }

    @Override
    public String executeCallEvent() {
        return "获取返回值： "+Thread.currentThread().getThreadGroup()+"--"+Thread.currentThread().getName()+"--执行内容--："+getEntity();
    }

}
