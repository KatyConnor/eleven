package com.hx.vertx.file.monitor.task;

import co.paralleluniverse.fibers.Fiber;
import com.hx.vertx.file.monitor.queue.FiberWorkTaskQueue;
import com.hx.vertx.file.monitor.log.HXLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件拆分任务实现类
 * @author wml
 * @date 2023-03-06
 */
public class FileSplitFiberTask<T extends FileChannel> extends FiberExecuteTask {

    /**
     * 文件大小
     */
    private long splitSize;
    /**
     * 当前文件数
     */
    private long splitIndex;
    /**
     * 源文件内容读取起始位
     */
    private long startPoint;

    /**
     * 源文件读取通道
     */
    private T inputChannel;

    /**
     * 文件存储目录
     */
    private String currentDir;

    @Override
    public void run() {
        File currentDirFile = new File(this.currentDir);
        if (!currentDirFile.exists()) {
            currentDirFile.mkdirs();
        }
        FileOutputStream fos = null;
        try{
            String splitFileName = currentDir +"tmp"+ splitIndex;
            File splitFile = new File(splitFileName);
            fos = new FileOutputStream(splitFile);
            inputChannel.transferTo(this.startPoint, this.splitSize, fos.getChannel());
            fos.flush();
        }catch (Exception e){
                HXLogger.build(this).error("拆分文件失败,当前文件ID={},文件名称=tmp{},Exception:{}",e,this.splitIndex,splitIndex);
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                    HXLogger.build(this).info("当前运行线程名称:" + Thread.currentThread().getName() + "---当前运行协程名称:" + Fiber.currentFiber().getName());
                }
            } catch (IOException e) {
                HXLogger.build(this).error("关闭文件流失败,{}",e);
            }
        }
    }

    public long getSplitSize() {
        return splitSize;
    }

    public FileSplitFiberTask setSplitSize(long splitSize) {
        this.splitSize = splitSize;
        return this;
    }

    public long getSplitIndex() {
        return splitIndex;
    }

    public FileSplitFiberTask setSplitIndex(long splitIndex) {
        this.splitIndex = splitIndex;
        return this;
    }

    public long getStartPoint() {
        return startPoint;
    }

    public FileSplitFiberTask setStartPoint(long startPoint) {
        this.startPoint = startPoint;
        return this;
    }

    public T getInputChannel() {
        return inputChannel;
    }

    public FileSplitFiberTask setInputChannel(T inputChannel) {
        this.inputChannel = inputChannel;
        return this;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public FileSplitFiberTask setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
        return this;
    }
}
