package com.hx.nine.eleven.web.socket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * WebSocket 服务启动器
 * @auth wml
 * @date 2024/9/30
 * @Desc  定义了一个 WebSocket 服务器，使用 Netty 框架的 ServerBootstrap 类来启动。
 */
public class WebSocketServer {

		/**
		 * 定义服务器端口号
		 */
		private final int port;

		/**
		 * 构造函数，初始化端口号
		 * @param port
		 */
		public WebSocketServer(int port) {
			this.port = port;
		}

		/**
		 * 启动服务器方法
		 * @throws InterruptedException
		 */
		public void start() throws InterruptedException {
			// 创建 bossGroup，用于接受新连接
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			// 创建 workerGroup，用于处理已接受的连接,用于处理连接上的I/O操作，含有子线程NioEventGroup个数为CPU核数大小的2倍
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				// 创建 ServerBootstrap 实例
				ServerBootstrap bootstrap = new ServerBootstrap();
				// 设置 bossGroup 和 workerGroup,将boss线程组和worker线程组暂存到ServerBootstrap
				bootstrap.group(bossGroup, workerGroup)
						// 设置服务端服务器通道的Channel实现类为NioServerSocketChannel作为通道实现
						.channel(NioServerSocketChannel.class)
						// 设置用于初始化新创建的子通道的 ChannelInitializer
						.childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							public void initChannel(SocketChannel ch) throws Exception {
								// 添加自定义的 WebSocket 服务器初始化器
								ch.pipeline().addLast(new WebSocketServerInitializer());
							}
						})
						//设置启动参数，初始化服务端接受连接的队列长度
						// 服务端处理客户端连接请求是顺序处理，一个时间内只能处理一个客户端请求
						// 当有多个客户端同时来请求时，未处理的请求先放入队列中
						.option(ChannelOption.SO_BACKLOG, 128)
						// 设置子通道的 TCP keep-alive 属性
						.childOption(ChannelOption.SO_KEEPALIVE, true);

				// 绑定服务器端口并启动服务器，等待直到绑定完成
				// bind方法是异步的，sync方法是等待异步操作执行完成，返回ChannelFuture异步对象
				ChannelFuture future = bootstrap.bind(port).sync();
				System.out.println("WebSocket 服务器启动, 端口号为: " + port);
				// 等待直到服务器 socket 关闭
				future.channel().closeFuture().sync();
			} finally {
				// 优雅关闭 workerGroup 和 bossGroup
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		}

		public static void main(String[] args) throws InterruptedException {
			int port = 8080; // Default port
			new WebSocketServer(port).start();
		}
}
