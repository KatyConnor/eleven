package com.hx.nine.eleven.web.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * WebSocket客户端
 *
 * @auth wml
 * @date 2024/9/30
 */
public class WebSocketClient {
	// 创建日志记录器
	Logger logger = LoggerFactory.getLogger(this.getClass());

	static final String URL = System.getProperty("url", "ws://localhost:8088/message_transfer/websocket/topic"); // WebSocket URL


	public static void main(String[] args) throws Exception {
		// 解析URL
		URI uri = new URI(URL);
		// 获取端口
		int port = getPort(uri);

		// 创建事件循环组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 创建WebSocket客户端处理器
			WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory
					.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

			// 配置Bootstrap
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).handler(
					new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							// 获取管道
							ChannelPipeline p = ch.pipeline();
							// 如果是wss连接, 则配置 SSL 处理器
							if ("wss".equalsIgnoreCase(uri.getScheme())) {
								// 配置SSL上下文，用于 wss 链接
								final SslContext sslCtx;
								try {
									sslCtx = SslContextBuilder.forClient()
											.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
								} catch (SSLException e) {
									throw new RuntimeException(e);
								}
								// 添加 SSL 处理器
								p.addLast(sslCtx.newHandler(ch.alloc(), uri.getHost(), uri.getPort()));
							}
							// 添加HTTP客户端编解码器和聚合器
							p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), handler);
						}
					});

			// 连接到服务器
			Channel ch = bootstrap.connect(uri.getHost(), port).sync().channel();
			// 等待握手完成
			handler.handshakeFuture().sync();

			// 读取控制台输入并发送WebSocket消息
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				// 读取控制台输入
				String msg = console.readLine();
				if ("再见".equalsIgnoreCase(msg)) {
					// 发送关闭帧
					ch.writeAndFlush(new CloseWebSocketFrame());
					// 等待关闭完成
					ch.closeFuture().sync();
					break;
				} else if ("ping".equalsIgnoreCase(msg)) {
					// 发送Ping帧
					WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
					ch.writeAndFlush(frame);
				} else {
					// 发送文本帧
					WebSocketFrame frame = new TextWebSocketFrame(msg);
					ch.writeAndFlush(frame);
				}
			}
		} finally {
			// 关闭事件循环组
			group.shutdownGracefully();
		}
	}

	/**
	 * 解析 url 的端口
	 *
	 * @param uri
	 * @return
	 */
	private static int getPort(URI uri) {
		int port = uri.getPort();
		if (port == -1) {
			// 返回默认端口，wss是443，ws是80
			return "wss".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
		}
		return port; // 返回URI中的端口
	}
}
