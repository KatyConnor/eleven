package com.hx.vertx.file.monitor.file;

import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import com.hx.vertx.file.monitor.queue.FiberTaskQueue;
import com.hx.vertx.file.monitor.queue.FiberWorkTaskQueue;
import com.hx.vertx.file.monitor.task.FileSplitFiberTask;
import com.hx.vertx.file.monitor.thread.ThreadPoolService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 大文件拆分
 * @author wml
 * @date 2023-03-02
 */
public class FileSplitPart {

    public static void main(String[] args) throws InterruptedException {
//        long a = 715;
//        long r = a % 3;
//        System.out.println(r);
//        File file = new File("D:\\qycache\\cqnsh_1201.dmp");
//        long splitSize = 1024*1024*20;
//        long splitNum = file.length() % splitSize;
//        System.out.println("fileLength= "+file.length());
//        System.out.println("splitNum=" + splitNum);
        long startTime = System.currentTimeMillis();
        File file = new File("/data/home/wml/devcode/app/dd.mp4");
        long splitSize = 1024 * 1024 * 20;
        long fileLength = file.length();
        long moldTaking = fileLength % splitSize;
        long splitNum = fileLength / splitSize;
        if (moldTaking > 0) {
            splitNum++;
        }

        FileInputStream fis = null;
        FileChannel inputChannel = null;
        try {
            fis = new FileInputStream(file);
            System.out.println(splitNum);
            inputChannel = nioSpilt(fis, Integer.valueOf(String.valueOf(splitNum)), "/data/home/wml/devcode/app/split/", splitSize, moldTaking);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 等待执行,当任务执行完毕关闭线程
        while (FiberTaskQueue.build().getTaskNum() >=0) {
            if (FiberWorkTaskQueue.build().size() > 0){
                Thread.sleep(1000);
            }
            if (FiberTaskQueue.build().getTaskNum() ==0 && FiberWorkTaskQueue.build().size() == 0){
                break;
            }

        }
        try {
            inputChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis() - startTime) + "/毫秒");
        System.out.println((System.currentTimeMillis() - startTime) / 1000 + "/秒");
    }


    /**
     * file，splitNum:，currentDir：，splitSize：
     *
     * @param fis        需要分片的文件
     * @param splitNum   要分几片
     * @param currentDir 分片后存放的位置
     * @param splitSize  按多大分片
     * @throws Exception
     */
    public static FileChannel nioSpilt(FileInputStream fis, int splitNum, String currentDir, long splitSize, long lastSplitSize) {
        FileChannel inputChannel = fis.getChannel();
        for (int i = 1; i <= splitNum; i++) {
            FileSplitFiberTask task = new FileSplitFiberTask();
            task.setSplitIndex(i).setStartPoint(splitSize * (i -1))
                    .setSplitSize(lastSplitSize > 0 && i == splitNum ? lastSplitSize : splitSize)
                    .setCurrentDir(currentDir)
                    .setInputChannel(inputChannel);
          ThreadPoolService.build().addQueue(task).execute("file-monitor-server-group");
        }
        System.out.println("当前队列剩余待处理数 = "+FiberTaskQueue.build().getTaskNum());
        return inputChannel;
    }

    /**
     * 文件合并
     *
     * @param mergeFile 合并后文件
     * @param fileList  被合并的文件列表
     * @return
     */
    public static boolean merge(File mergeFile, ArrayList<File> fileList) {
        // 合并文件
        try (FileChannel fosChannel = new FileOutputStream(mergeFile).getChannel()) {
            for (File chunkFile : fileList) {
                try (FileChannel fisChannel = new FileInputStream(chunkFile).getChannel()) {
                    fisChannel.transferTo(0, fisChannel.size(), fosChannel);
                } catch (IOException ex) {
                    System.out.println("IOException" + ex);
                }
            }

        } catch (IOException ex) {
            System.out.println("IOException" + ex);
        }
        return true;
    }

    /**
     * 文件合并
     *
     * @param targetFile 要形成的文件名
     * @param folder     要形成的文件夹地址
     * @param filename   文件的名称
     */
    public static void merge(String targetFile, String folder, String filename) {
        try {
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
//                            log.error(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
//            log.error(e.getMessage(), e);
        }
    }

    private static FiberScheduler defaultFiberExecutorScheduler() {
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
