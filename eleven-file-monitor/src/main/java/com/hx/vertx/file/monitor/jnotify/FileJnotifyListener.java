package com.hx.vertx.file.monitor.jnotify;

import com.hx.vertx.file.monitor.queue.FileMonitorQueue;
import com.hx.vertx.file.monitor.log.HXLogger;
import net.contentobjects.jnotify.JNotifyListener;

public class FileJnotifyListener implements JNotifyListener {
    @Override
    public void fileCreated(int wd, String rootPath, String name) {
        FileMonitorQueue.build().add(rootPath);
        HXLogger.build(this).info("文件被创建, 创建位置为：{} " , rootPath + name);
    }

    @Override
    public void fileDeleted(int wd, String rootPath, String name) {
        FileMonitorQueue.build().add(rootPath);
        HXLogger.build(this).info("文件被删除, 被删除的文件名为: {}" , rootPath + name);
    }

    @Override
    public void fileModified(int wd, String rootPath, String name) {
        // 当文件写完，提示目录发生变化时，向队列中添加
        FileMonitorQueue.build().add(rootPath);
        HXLogger.build(this).info("文件内容被修改, 文件名为: {}" , rootPath + name);
    }

    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        FileMonitorQueue.build().add(rootPath);
        HXLogger.build(this).info("文件名称被修改, 文件名为：{}" , rootPath + newName);
    }
}
