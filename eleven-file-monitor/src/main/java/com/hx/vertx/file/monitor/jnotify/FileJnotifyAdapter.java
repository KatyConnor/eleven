package com.hx.vertx.file.monitor.jnotify;

import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.queue.FileMonitorQueue;
import net.contentobjects.jnotify.JNotifyAdapter;

/**
 * 文件目录变化监听
 */
public class FileJnotifyAdapter extends JNotifyAdapter{

    @Override
    public void fileCreated(int wd, String rootPath, String name) {
        FileMonitorQueue.build().add(rootPath + name);
        HXLogger.build(this).info("文件被创建, 创建位置为：{} " , rootPath + name);
    }

    @Override
    public void fileDeleted(int wd, String rootPath, String name) {
        FileMonitorQueue.build().add(rootPath + name);
        HXLogger.build(this).info("文件被删除, 被删除的文件名为: {}" , rootPath + name);
    }

    @Override
    public void fileModified(int wd, String rootPath, String name) {
        FileMonitorQueue.build().add(rootPath + name);
        HXLogger.build(this).info("文件内容被修改, 文件名为: {}" , rootPath + name);
    }

    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        FileMonitorQueue.build().add(rootPath + newName);
        HXLogger.build(this).info("文件名称被修改, 文件名为：{}" , rootPath + newName);
    }

}
