package com.hx.nine.eleven.web.http;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 处理 HTTP 请求
 *
 * @auth wml
 * @date 2024/9/30
 * @Desc HttpRequestHandler 类继承自 Netty 的 SimpleChannelInboundHandler，
 * 专门用于处理 FullHttpRequest 类型的入站数据。
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	/**
	 * 定义一个路由 Map，它将 HTTP 请求的路径映射到一个处理函数，
	 * 这个处理函数是一个接受 FullHttpRequest 和 ChannelHandlerContext 并返回 FullHttpResponse 的 BiFunction。
	 */
	private static final Map<String, BiFunction<FullHttpRequest, ChannelHandlerContext, FullHttpResponse>> routes = new HashMap<>();

	// 初始化路由 Map，将特定的路径 "/get" 和 "/post" 与对应的处理函数关联起来。
	static {
		// 将 "/get" 路径映射到 handleGetRequest 方法
		routes.put("/get", HttpRequestHandler::handleGetRequest);
		// 将 "/post" 路径映射到 handlePostRequest 方法
		routes.put("/post", HttpRequestHandler::handlePostRequest);
		// 可以添加更多的路径和处理函数
	}

	/**
	 * channelRead0 方法是 SimpleChannelInboundHandler 的抽象方法实现，
	 * 处理入站的 FullHttpRequest 对象
	 *
	 * @param ctx
	 * @param req
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
		// 检查请求是否期望发送 100 Continue 响应，这通常用于 Expect: 100-continue 请求头。
		if (HttpUtil.is100ContinueExpected(req)) {
			// 如果需要，发送 100 Continue 响应
			send100Continue(ctx);
		}

		// 检查请求是否需要保持连接活跃（即 Connection 头部不是 close）。
		boolean keepAlive = HttpUtil.isKeepAlive(req);
		// 获取请求的 URI。
		String uri = req.uri();
		// 从路由 Map 中获取与当前请求 URI 对应的处理函数。
		BiFunction<FullHttpRequest, ChannelHandlerContext, FullHttpResponse> handler = routes.get(uri);

		FullHttpResponse response;
		// 如果找到处理函数，则应用该函数生成响应。
		if (handler != null) {
			response = handler.apply(req, ctx);
		} else {
			// 如果没有找到处理函数，生成一个 404 Not Found 响应。
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
			response.content().writeBytes("404 Not Found".getBytes(CharsetUtil.UTF_8));
			// 设置响应头 Content-Type
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
			// 设置响应头 Content-Length。
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		}
		// 如果请求需要保持连接活跃，则设置 Connection 头部为 keep-alive。
		if (keepAlive) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}

		// 写入并刷新响应，即发送数据到客户端。如果请求不是 keep-alive，则添加 CLOSE 监听器来关闭连接
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * 发送 100 Continue 响应
	 *
	 * @param ctx
	 */
	private void send100Continue(ChannelHandlerContext ctx) {
		// 写入并刷新 100 Continue 响应
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(response);
	}

	/**
	 * 处理 GET 请求
	 *
	 * @param req
	 * @param ctx
	 * @return
	 */
	private static FullHttpResponse handleGetRequest(FullHttpRequest req, ChannelHandlerContext ctx) {
		// 定义 GET 请求的响应内容
		String content = "这是一个 Get 请求";
		// 创建一个 HTTP 1.1 的 FullHttpResponse 对象，并设置状态码为 OK
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		// 将响应内容写入响应体
		response.content().writeBytes(content.getBytes(CharsetUtil.UTF_8));
		// 设置响应头 Content-Type
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		// 设置响应头 Content-Length
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		return response;
	}

	/**
	 * 处理 POST 请求
	 *
	 * @param req
	 * @param ctx
	 * @return
	 */
	private static FullHttpResponse handlePostRequest(FullHttpRequest req, ChannelHandlerContext ctx) {
		// 从请求体中读取 POST 请求的内容
		String content = req.content().toString(CharsetUtil.UTF_8);
		// 定义 POST 请求的响应内容
		String responseContent = "这是一个 post 请求: " + content;
		// 创建一个 HTTP 1.1 的 FullHttpResponse 对象，并设置状态码为 OK
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		// 将响应内容写入响应体
		response.content().writeBytes(responseContent.getBytes(CharsetUtil.UTF_8));
		// 设置响应头 Content-Type
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		// 设置响应头 Content-Length
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		return response;
	}
}
