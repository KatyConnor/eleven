package com.hx.nine.eleven.web.socket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocket 服务器初始化器
 *
 * @auth wml
 * @date 2024/9/30
 * @Desc 用于初始化新创建的 Channel。
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * initChannel 方法会在新 Channel 创建时被调用
	 *
	 * @param ch
	 * @throws Exception
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 添加 HttpServerCodec 处理器，用于处理 HTTP 请求和响应
		ch.pipeline().addLast(new HttpServerCodec());
		// 添加 HttpObjectAggregator 处理器，聚合 HTTP Fragments 到一个完整的 HTTP 对象
		ch.pipeline().addLast(new HttpObjectAggregator(65536));
		// 添加 ChunkedWriteHandler 处理器，用于处理 chunked 传输编码
		ch.pipeline().addLast(new ChunkedWriteHandler());
		// 添加 WebSocketServerHandler 处理器，用于处理 WebSocket 帧
		ch.pipeline().addLast(new WebSocketServerHandler());
	}
}
