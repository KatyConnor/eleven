package com.hx.nine.eleven.web.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * 基于Netty快速实现WebSocket客户端，不手动处理握手
 *
 * @auth wml
 * @date 2024/9/30
 */
public class SimpleWebSocketClient {

	// 日志记录器
	static Logger logger = LoggerFactory.getLogger(SimpleWebSocketClient.class);

	/**
	 * 倒计时锁，用于同步处理
	 */
	final CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
		// 创建客户端实例并执行测试
		SimpleWebSocketClient client = new SimpleWebSocketClient();
		client.test();
	}

	public void test() throws Exception {
		// 获取连接通道
		Channel dest = dest();
		// 等待 CountDownLatch 完成
		latch.await();
		// 发送 WebSocket 消息
		dest.writeAndFlush(new TextWebSocketFrame("CountDownLatch完成后发送的消息"));
	}

	public Channel dest() throws Exception {
		// 目标 WebSocket URL
		URI uri = new URI("wss://localhost:8088/message_transfer/websocket/topic");

		// 创建事件循环组
		EventLoopGroup group = new NioEventLoopGroup();
		// 创建引导程序
		Bootstrap boot = new Bootstrap();
		// 定期发送心跳包，以确保连接的有效性。如果在一定时间内没有收到响应，连接将被认为已断开。
		boot.option(ChannelOption.SO_KEEPALIVE, true)
				// 启用TCP的TCP_NODELAY选项，关闭了Nagle算法。
				// Nagle算法通过减少发送小数据包的数量来提高网络效率，
				// 但会增加一些延迟。启用 TCP_NODELAY 可以减少延迟，对需要快速响应的应用有帮助。
				.option(ChannelOption.TCP_NODELAY, true)
				// 设置用于处理事件的EventLoopGroup。group是一个EventLoopGroup实例，
				// 负责管理通道的I/O操作和事件通知。
				.group(group)
				// 设置了一个日志处理器 (LoggingHandler)，用于记录所有的事件和操作。日志级别为INFO，
				// 因此会记录一般的信息、警告和错误。
				.handler(new LoggingHandler(LogLevel.INFO))
				// 指定了通道类型为NioSocketChannel。这意味着使用NIO（非阻塞 I/O）选择器机制来处理 I/O 操作。
				// NioSocketChannel是 Netty 提供的一个具体实现类，适用于客户端通道。
				.channel(NioSocketChannel.class)
				// 这个方法设置了通道的初始化逻辑。ChannelInitializer是一个特殊的处理器，
				// 用于在通道注册到 EventLoop 时配置新的通道。
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						// 获取管道
						ChannelPipeline pipeline = sc.pipeline();
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
							pipeline.addLast(sslCtx.newHandler(sc.alloc(), uri.getHost(), uri.getPort()));
						}
						// 添加HTTP客户端编解码器
						pipeline.addLast(new HttpClientCodec());
						// 添加分块写处理器
						pipeline.addLast(new ChunkedWriteHandler());
						// 添加HTTP对象聚合器，聚合消息内容最大为64KB
						pipeline.addLast(new HttpObjectAggregator(64 * 1024));
						// 添加WebSocket客户端协议处理器
						pipeline.addLast(new WebSocketClientProtocolHandler(WebSocketClientHandshakerFactory
								.newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders())));
						// 添加自定义的处理器
						pipeline.addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>() {

							/**
							 * 收到消息时被调用
							 * @param ctx
							 * @param msg
							 * @throws Exception
							 */
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
									throws Exception {
								// 打印收到的消息
								logger.info("收到消息 : {}", msg.text());
							}

							/**
							 * 触发用户事件
							 * @param ctx
							 * @param evt
							 * @throws Exception
							 */
							@Override
							public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
								// 如果握手完成事件触发
								if (WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE
										.equals(evt)) {
									logger.info("握手完成: {}", ctx.channel().id().toString());
									// 释放 CountDownLatch
									latch.countDown();
									// 发送消息
									send(ctx.channel(), "握手成功");
								}
								super.userEventTriggered(ctx, evt);
							}
						});
					}
				});

		// 连接到服务器并同步
		ChannelFuture cf = boot.connect(uri.getHost(), getPort(uri)).sync();

		// 返回连接的通道
		return cf.channel();
	}


	/**
	 * 发送 WebSocket 消息
	 *
	 * @param channel Netty 通道对象
	 */
	public static void send(Channel channel, String message) {
		if (channel == null) {
			logger.error("通道为 null，无法发送消息: {}", message);
			return;
		}

		if (!channel.isActive()) {
			logger.error("通道未处于活跃状态，无法发送消息: {}", message);
			return;
		}

		// 创建一个包含待发送消息的 TextWebSocketFrame 实例
		TextWebSocketFrame frame = new TextWebSocketFrame(message);

		// 向通道中写入并发送该帧（消息），并添加一个监听器以处理发送结果
		channel.writeAndFlush(frame).addListener((ChannelFutureListener) channelFuture -> {
			// 检查发送操作是否成功
			if (channelFuture.isSuccess()) {
				// 如果发送成功，记录日志
				logger.info("消息发送成功: {}", message);
			} else {
				// 如果发送失败，获取失败的原因
				Throwable cause = channelFuture.cause();
				// 记录发送失败的日志，并打印失败原因的堆栈跟踪信息
				logger.error("消息发送失败: {}", message, cause);
				cause.printStackTrace();
				// 关闭当前通道以避免进一步的错误
				closeChannel(channelFuture.channel());
			}
		});
	}

	/**
	 * 关闭指定的 Netty 通道
	 *
	 * @param channel Netty 通道对象
	 */
	private static void closeChannel(Channel channel) {
		if (channel != null && channel.isOpen()) {
			channel.close().addListener((ChannelFutureListener) closeFuture -> {
				if (closeFuture.isSuccess()) {
					logger.info("通道关闭成功: {}", channel.id());
				} else {
					logger.error("通道关闭失败: {}", channel.id(), closeFuture.cause());
					closeFuture.cause().printStackTrace();
				}
			});
		}
	}


	/**
	 * 获取端口号，如果未指定则根据协议设置默认端口
	 *
	 * @param uri
	 * @return
	 */
	private static int getPort(URI uri) {
		int port = uri.getPort();
		if (port == -1) {
			return "wss".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
		}
		return port;
	}
}
