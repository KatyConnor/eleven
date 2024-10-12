package com.hx.nine.eleven.web.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

/**
 * @auth wml
 * @date 2024/10/8
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(65536));
//		pipeline.addLast(new HttpPostRequestHandler());

		// 将ByteBuf转换成字符串
		ch.pipeline().addLast(new HttpServerCodec());
		// 添加 HTTP 消息聚合器，将字符串转换成FullHttpRequest
		ch.pipeline().addLast(new HttpObjectAggregator(65536));
		// 添加 HTTP 100-continue 处理器
		ch.pipeline().addLast(new HttpServerExpectContinueHandler());
		// 添加自定义的 HTTP 请求处理器
		ch.pipeline().addLast(new HttpRequestHandler());
	}
}
