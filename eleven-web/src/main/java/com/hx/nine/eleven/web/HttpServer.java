package com.hx.nine.eleven.web;

import com.hx.nine.eleven.web.http.HttpRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

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
					// 创建并设置自定义的 ChannelInitializer
					.childHandler(new ChannelInitializer<SocketChannel>() {
						/**
						 * 初始化新创建的子通道
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
			Channel ch = bootstrap.bind(port).sync().channel();
			System.out.println("Server started at http://127.0.0.1:" + port + '/');
			// 等待直到服务器 socket 关闭
			ch.closeFuture().sync();
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
