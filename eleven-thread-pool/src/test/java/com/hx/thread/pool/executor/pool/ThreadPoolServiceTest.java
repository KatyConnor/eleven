package com.hx.thread.pool.executor.pool;

import com.hx.thread.pool.executor.ApplicationContext;

import java.util.concurrent.*;

/**
 * @Author mingliang
 * @Date 2017-12-29 21:00
 */
//@RunWith(SpringJUnit4ClassRunner.class)
public class ThreadPoolServiceTest {


//    @Test
    public void testPool() {
        ThreadPoolService threadPoolService = ThreadPoolService.build();
        ThreadPoolExcutorService service = new ThreadPoolExcutorService();
        service.initThreadPool();
        for (int i = 0; i < 2000; i++) {
            final int j = i;
            new Thread(() -> {
                GenerateHtml generateHtml = null;
                if (j % 2 == 0) {
                    generateHtml = new GenerateHtml("线程池 test2 开始执行事件！" + j);
                    threadPoolService.execute(generateHtml);
                } else {
                    generateHtml = new GenerateHtml("线程池 test 开始执行事件！" + j);
                    FutureTask futureTask = threadPoolService.submit(generateHtml);
                    ApplicationContext.build().addFutureTask(futureTask);
                }
            }).start();
        }
        int next = 0;
        while (true) {
            try {
                FutureTask futureTask = ApplicationContext.build().getIsDoneFutureTask();
                if (futureTask != null){
                    System.out.println(futureTask.get());
                    ApplicationContext.build().removeFutureTask(futureTask);
                    next++;
                }
                if (next == 1000){
                    System.out.println("执行结束，执行数：count="+next);
                    next++;
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
