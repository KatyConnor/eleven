package com.hx.nine.eleven.web;

import com.hx.nine.eleven.web.http.HttpRequestHandler;
import com.hx.nine.eleven.web.socket.server.WebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Http 服务启动器
 *
 * @auth wml
 * @date 2024/9/30
 * @Desc 定义了一个 Http 服务器，使用 Netty 框架的 ServerBootstrap 类来启动。
 */
public class HttpServer {

	/**
	 * 服务器端口号
	 */
	private final int port;

	/**
	 * 在构造函数中初始化端口号
	 *
	 * @param port
	 */
	public HttpServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		// // 创建两个 NioEventLoopGroup 实例，分别用于处理服务器的 Accept 操作和 I/O 操作
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 创建 ServerBootstrap 实例
			ServerBootstrap bootstrap = new ServerBootstrap();
			// 分配事件循环组
			bootstrap.group(bossGroup, workerGroup)
					// 设置服务器通道的类型
					.channel(NioServerSocketChannel.class)
					// 设置线程队列得到连接个数
					.option(ChannelOption.SO_BACKLOG, 128)
					/**
					 * SO_RCVBUF Socket参数，TCP数据接收缓冲区大小。
					 * TCP_NODELAY TCP参数，立即发送数据，默认值为Ture。
					 * SO_KEEPALIVE Socket参数，连接保活，默认值为False。启用该功能时，TCP会主动探测空闲连接的有效性。
					 * SO_BACKLOG Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值，Windows为200，其他为128。
					 *
					 *
					 */
					//设置保持活动连接状态
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					// 创建并设置自定义的 ChannelInitializer
					.childHandler(new ChannelInitializer<SocketChannel>() {
						/**
						 * 初始化新创建的子通道
						 *
						 * 注册事件 fireChannelRegistered。
						 * 连接建立事件 fireChannelActive。
						 * 读事件和读完成事件 fireChannelRead、fireChannelReadComplete。
						 * 异常通知事件 fireExceptionCaught。
						 * 用户自定义事件 fireUserEventTriggered。
						 * Channel 可写状态变化事件 fireChannelWritabilityChanged。
						 * 连接关闭事件 fireChannelInactive。
						 * ChannelOutboundHandler处理器常用的事件有：
						 *
						 * 端口绑定 bind。
						 * 连接服务端 connect。
						 * 写事件 write。
						 * 刷新时间 flush。
						 * 读事件 read。
						 * 主动断开连接 disconnect。
						 * 关闭 channel 事件 close。
						 *
						 *
						 * @param ch
						 */
						@Override
						protected void initChannel(SocketChannel ch) {
							// 添加 HTTP 服务器编解码器
							ch.pipeline().addLast(new HttpServerCodec());
							// 添加 HTTP 消息聚合器
							ch.pipeline().addLast(new HttpObjectAggregator(65536));
							// 添加 HTTP 100-continue 处理器
							ch.pipeline().addLast(new HttpServerExpectContinueHandler());
							// 添加自定义的 HTTP 请求处理器
							ch.pipeline().addLast(new HttpRequestHandler());
						}
					});
			// 绑定服务器到指定端口并启动服务器
//			Channel ch = bootstrap.bind(port).sync().channel();
			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			channelFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()){
						System.out.println("连接请求处理成功");
					}
				}
			});
			System.out.println("Server started at http://127.0.0.1:" + port + '/');
			// 等待直到服务器 socket 关闭
//			ch.closeFuture().sync();
			//对关闭通道进行监听
			channelFuture.channel().closeFuture().sync();
		} finally {
			// 优雅地关闭事件循环组
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		new HttpServer(port).start();
	}
}
