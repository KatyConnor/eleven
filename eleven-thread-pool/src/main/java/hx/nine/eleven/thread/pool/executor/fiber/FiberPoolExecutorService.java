package hx.nine.eleven.thread.pool.executor.fiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.queues.SingleConsumerLinkedObjectQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 协程池处理
 *
 * @author wml
 * @date 2023-03-07
 */
public class FiberPoolExecutorService {
    //默认协程池中默认协程的个数为5
    private final static int DEFAULT_MAX_FIBER_NUM = 50000;
    //默认队列默认任务为50000000
    private final static int TASK_NUM = 5000000;
    //
//    private Fiber[] workFibers;
    // 当前工作的协程数
    private int workFiberSize;
    //等待队列
    private ArrayBlockingQueue<SuspendableRunnable> taskQueue;
    // 工做协程数组队列
    private ArrayBlockingQueue<Fiber> fiberQueue;
    private SingleConsumerLinkedObjectQueue queue;
    //用户在构造这个协程池时，但愿启动的协程数
    private int coreFibers;
    //任务调度线程
    private FiberScheduler scheduler;

    /**
     * 构造方法：建立具备默认协程个数的协程池
     */
    public FiberPoolExecutorService() {
        this(null, DEFAULT_MAX_FIBER_NUM, TASK_NUM);
    }

    /**
     * 建立协程池,coreFibers为协程池中工做协程的个数
     *
     * @param scheduler
     * @param coreFibers
     * @param taskCount
     */
    public FiberPoolExecutorService(FiberScheduler scheduler, int coreFibers, int taskCount){
        // 如果没配置使用默认线程池调度
        this.scheduler = scheduler == null ? defaultFiberExecutorScheduler() : scheduler;
        this.coreFibers = coreFibers > DEFAULT_MAX_FIBER_NUM ? coreFibers : DEFAULT_MAX_FIBER_NUM;
        this.taskQueue = new ArrayBlockingQueue(taskCount > TASK_NUM ? taskCount : TASK_NUM);
        this.fiberQueue = new ArrayBlockingQueue<>(taskCount > TASK_NUM ? taskCount : TASK_NUM);
        for (int i = 0; i < coreFibers; i++) {
            Fiber fiber = new Fiber<>(this.scheduler, () -> {
                SuspendableRunnable runnable = null;
                while (true) {
                    try {
                        //取任务，没有则阻塞。
                        runnable = this.taskQueue.take();
                        System.out.println("当前运行线程名称:"+Thread.currentThread().getName()+"---当前运行协程名称:"+Fiber.currentFiber().getName());
                    } catch (Exception e) {
                        System.out.println("任务获取失败，{}" + e);
                        //HXLogFactory.log("任务获取失败，{}",e);
                    }
                    //存在任务则运行。
                    if (runnable == null) {
                        break;
                    }
                    runnable.run();
                    this.workFiberSize--;
                    runnable = null;
                }
            });
            try {
                this.fiberQueue.put(fiber);
            } catch (InterruptedException e) {
                System.out.println("丢入待执行fiber队列失败，{}" + e);
                //HXLogFactory.log("丢入待执行fiber队列失败，{}",e);
            }
            //new一个工做协程
//            workFibers[i].start(); //启动工做协程
        }
//        Runtime.getRuntime().availableProcessors();
    }

    /**
     * 执行任务，其实就是把任务加入任务队列，何时执行由协程池管理器决定
     *
     * @param task
     */
    public void execute(SuspendableRunnable task) {
        try {
            taskQueue.put(task);
            if ( this.taskQueue.size() > 0 && this.workFiberSize < DEFAULT_MAX_FIBER_NUM){
                this.fiberQueue.take().start();
                this.workFiberSize++;
            }
        } catch (Exception e) {
            System.out.println("任务获取失败，{}" + e);
//            HXLogFactory.log("添加任务失败，{}",e);
        }
    }

    /**
     * 销毁协程池,该方法保证全部任务都完成的状况下才销毁全部协程，不然等待任务完成再销毁
     */
    public void destory() {
        //工做协程中止工做，且置为null
        System.out.println("ready close thread...");
//        HXLogFactory.log("ready close thread...");
//        this.fiberQueue.remove();
        this.fiberQueue.clear();
        this.taskQueue.clear();  //清空等待队列
    }

    public int getTaskNum() {
        return this.taskQueue.size();
    }

    //覆盖toString方法，返回协程信息：工做协程个数和已完成任务个数
    @Override
    public String toString() {
        return "coreFibers number:" + this.coreFibers + " ==分割线== wait task number:" + taskQueue.size();
    }

    private FiberScheduler defaultFiberExecutorScheduler() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                10,
                5, TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        FiberExecutorScheduler scheduler = new FiberExecutorScheduler("FibersExecutorSchedulerPool", executor);
        return scheduler;
    }
}
