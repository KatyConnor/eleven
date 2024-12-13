//package hx.nine.eleven.http.client.thread;
//
//import org.apache.http.conn.HttpClientConnectionManager;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * <p>
// *     使用setStaleConnectionCheckEnabled方法来逐出已被关闭的链接不被推荐。
// *     更好的方式是手动启用一个线程，
// *     定时运行closeExpiredConnections 和closeIdleConnections方法
// *
// *     1. 创建定时任务关闭PoolingHttpClientConnectionManager的异常连接，释放连接和连接相关的资源
// *     2. 关闭重试操作
// * </p>
// * @Author mingliang
// * @Date 2020-06-06
// */
//public class IdleConnectionMonitorThread extends Thread {
//
//    private final HttpClientConnectionManager connMgr;
//    private volatile boolean shutdown;
//    private long maxIdleTimeMs;
//
//    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
//        super();
//        this.connMgr = connMgr;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!shutdown) {
//                synchronized (this) {
//                    sleep(5000);
//                    // Close expired connections  清理过期连接
//                    connMgr.closeExpiredConnections();
//                    // Optionally, close connections
//                    // that have been idle longer than 30 sec  如果指定了最大空闲时间，则清理空闲连接
//                    if (maxIdleTimeMs > 0){
//                        connMgr.closeIdleConnections(maxIdleTimeMs, TimeUnit.SECONDS);
//                    }
//                }
//            }
//        } catch (InterruptedException ex) {
//            // terminate
//        }
//    }
//
//    public void shutdown() {
//        shutdown = true;
//        synchronized (this) {
//            notifyAll();
//        }
//    }
//}
