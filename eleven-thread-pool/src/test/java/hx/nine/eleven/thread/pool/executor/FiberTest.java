package hx.nine.eleven.thread.pool.executor;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.futures.AsyncListenableFuture;
import co.paralleluniverse.strands.SuspendableCallable;
import com.google.common.util.concurrent.SettableFuture;

import java.util.concurrent.ExecutionException;

public class FiberTest {

    public static void main(String[] args) {
        final SettableFuture<String> fut = SettableFuture.create();
        FiberScheduler scheduler = new FiberForkJoinScheduler("test", 4, null, false);
        Fiber<String> fiber = new Fiber<>(scheduler, new SuspendableCallable<String>() {
            @Override
            public String run() throws SuspendExecution, InterruptedException {
                    System.out.println("协程处理完毕----处理结果[线程处理完毕]");
                try {
                    Fiber.sleep(5000000);
//                    Fiber.currentFiber().getName();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SuspendExecution e) {
                    e.printStackTrace();
                }
                fut.set("协程1处理完毕");

                    return "线程处理完毕";

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("线程开始----");
                    Fiber fiber = new Fiber<>(scheduler, ()-> {
                        try {
                            String res = AsyncListenableFuture.get(fut);
                            System.out.println("异步获取结果------"+res);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }).start();
//

//                    String ss = fiber.get();

//                    fut.setException(new RuntimeException("haha!"));
                    System.out.println("线程结束----");
                } catch (Exception e) {
                }
            }
        }).start();

        try {
//            Thread.sleep(5000);
//            String res = AsyncListenableFuture.get(fut);
//            System.out.println("异步获取结果------res:"+res);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
