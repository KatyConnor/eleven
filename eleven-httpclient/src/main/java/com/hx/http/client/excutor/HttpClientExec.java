package com.hx.http.client.excutor;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.execchain.ClientExecChain;

import java.io.IOException;

/**
 * @Author mingliang
 * @Date 2018-08-01 16:29
 */
public class HttpClientExec implements ClientExecChain {

    @Override
    public CloseableHttpResponse execute(HttpRoute httpRoute, HttpRequestWrapper httpRequestWrapper,
                                         HttpClientContext httpClientContext,
                                         HttpExecutionAware httpExecutionAware) throws IOException, HttpException {
        //从连接管理器HttpClientConnectionManager中获取一个连接请求ConnectionRequest
//        final ConnectionRequest connRequest = connManager.requestConnection(httpRequestWrapper, userToken);
//        final HttpClientConnection managedConn;
//        final int timeout = config.getConnectionRequestTimeout();
//        //从连接请求ConnectionRequest中获取一个被管理的连接HttpClientConnection
//        managedConn = connRequest.get(timeout > 0 ? timeout : 0, TimeUnit.MILLISECONDS);
//　　　　 //将连接管理器HttpClientConnectionManager与被管理的连接HttpClientConnection交给一个ConnectionHolder持有
//        final ConnectionHolder connHolder = new ConnectionHolder(this.log, this.connManager, managedConn);
//        try {
//            HttpResponse response;
//            if (!managedConn.isOpen()) {
//　　　　　　　　　　//如果当前被管理的连接不是出于打开状态，需要重新建立连接
//                establishRoute(proxyAuthState, managedConn, route, request, context);
//            }
//　　　　　　　//通过连接HttpClientConnection发送请求
//            response = requestExecutor.execute(request, managedConn, context);
//　　　　　　　//通过连接重用策略判断是否连接可重用
//            if (reuseStrategy.keepAlive(response, context)) {
//                //获得连接有效期
//                final long duration = keepAliveStrategy.getKeepAliveDuration(response, context);
//                //设置连接有效期
//                connHolder.setValidFor(duration, TimeUnit.MILLISECONDS);
//　　　　　　　　　 //将当前连接标记为可重用状态
//                connHolder.markReusable();
//            } else {
//                connHolder.markNonReusable();
//            }
//        }
//        final HttpEntity entity = response.getEntity();
//        if (entity == null || !entity.isStreaming()) {
//            //将当前连接释放到池中，供下次调用
//            connHolder.releaseConnection();
//            return new HttpResponseProxy(response, null);
//        } else {
//            return new HttpResponseProxy(response, connHolder);
//        }
        return null;
    }

}
