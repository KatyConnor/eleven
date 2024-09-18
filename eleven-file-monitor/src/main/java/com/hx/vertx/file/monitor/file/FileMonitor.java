package com.hx.vertx.file.monitor.file;

import com.hx.vertx.file.monitor.log.HXLogger;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

/**
 * 启动文件目录监听
 * @author wml
 * @date 2023-03-08
 */
public class FileMonitor {

    /**
     * 监控目录
     */
    private String monitorDir;
    /**
     * 是否监视子目录，即级联监视，默认是true
     */
    private Boolean watchSubtree = true;
    /**
     * 监听处理器
     */
    private JNotifyListener listener;

    public String getMonitorDir() {
        return monitorDir;
    }

    public FileMonitor setMonitorDir(String monitorDir) {
        this.monitorDir = monitorDir;
        return this;
    }

    public static FileMonitor build() {
        return new FileMonitor();
    }

    public boolean isWatchSubtree() {
        return watchSubtree;
    }

    public FileMonitor setWatchSubtree(boolean watchSubtree) {
        this.watchSubtree = watchSubtree;
        return this;
    }

    public JNotifyListener getListener() {
        return listener;
    }

    public FileMonitor setListener(JNotifyListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 容器启动时启动监视程序
     *
     * @throws JNotifyException
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void startWatch() throws JNotifyException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        if (this.monitorDir == null || this.monitorDir.isEmpty()) {
            HXLogger.build(this).info("监听目录不能为空");
            return;
        }
        //关注目录的事件
        int MASK = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;
        //添加到监视队列中
        try {
            int watchId = JNotify.addWatch(this.monitorDir, MASK, this.watchSubtree, this.listener);
            HXLogger.build(this).info("-----------jnotify监听启动成功-----------, watchId = {}",watchId);
        } catch (JNotifyException e) {
            HXLogger.build(this).error("{}",e);
        }
    }
}
