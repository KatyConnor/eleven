package com.hx.nine.eleven.web.socket.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * WebSocket 处理器
 *
 * @auth wml
 * @date 2024/9/30
 * @Desc 用于处理 WebSocket 服务器端的特定逻辑。
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	/**
	 * 用于存储特定 URI 到 Channel 集合的映射, 每个 URI 对应一个集合
	 */
	private static final Map<String, ChannelGroup> channelsByUri = new ConcurrentHashMap<>();

	/**
	 * 用于存储每个 WebSocket 连接的 URI
	 */
	private static final AttributeKey<String> URI_KEY = AttributeKey.valueOf("uri");

	/**
	 * 用于执行 WebSocket 握手操作的握手器
	 */
	private WebSocketServerHandshaker handshaker;

	/**
	 * 当 ChannelHandler 被添加到 ChannelPipeline 中时，此方法被调用。
	 *
	 * @param ctx ChannelHandler 和 Channel 之间的上下文关系
	 * @throws Exception
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		// 可以添加一些别的处理
	}


	/**
	 * 当 ChannelHandler 从 ChannelPipeline 中移除时，此方法被调用。
	 *
	 * @param ctx ChannelHandler 和 Channel 之间的上下文关系
	 * @throws Exception
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// 从属性中获取 URI
		String uri = ctx.channel().attr(URI_KEY).get();
		if (uri != null) {
			ChannelGroup channels = channelsByUri.get(uri);
			if (channels != null) {
				// 从 ChannelGroup 中移除当前 Channel
				channels.remove(ctx.channel());
				// 如果该 URI 的 ChannelGroup 为空，则清理资源
				if (channels.isEmpty()) {
					channelsByUri.remove(uri);
				}
			}
		}
	}

	/**
	 * 读取数据时调用，根据消息类型分发到不同的处理方法。
	 *
	 * @param ctx ChannelHandler 和 Channel 之间的上下文关系
	 * @param msg 接收到的消息
	 * @throws Exception
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			// WebSocket 客户端与服务器在第一次连接时，首先会发送一个 HTTP 请求。
			// 这是 WebSocket 协议的一部分，称为“WebSocket 握手”（WebSocket handshake）。
			// WebSocket 握手从客户端发送一个带有特定头的 HTTP GET 请求开始。
			// 这些头部信息告诉服务器客户端希望升级到 WebSocket 协议。
			// 服务器收到请求后，会检查请求头中的信息，确定是否支持 WebSocket 协议。
			// 如果支持，服务器会发送一个 HTTP 101 切换协议（Switching Protocols）的响应，
			// 表示握手成功，并同意升级协议。
			// 握手成功后，客户端和服务器之间的通信协议从 HTTP 升级为 WebSocket。这时，连接变成全双工的 WebSocket 连接，
			// 可以双向发送和接收消息。
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			// 处理 WebSocket 帧
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	/**
	 * 处理 HTTP 请求的方法。WebSocket 客户端与服务器在第一次连接时，首先会发送一个 HTTP 请求。
	 *
	 * @param ctx ChannelHandler 和 Channel 之间的上下文关系
	 * @param req http请求
	 * @throws Exception
	 */
	private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) throws Exception {

		// 检查请求是否成功解码，以及请求是否为 WebSocket 握手请求
		if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get(HttpHeaderNames.UPGRADE)))) {
			// 如果请求不是 WebSocket 握手请求，发送 400 Bad Request 错误响应
			sendHttpResponse(ctx, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}
		// 获取请求的 URI
		String uri = req.uri();
		// 如果 URI 为 null，则抛出异常
		if (null == uri) {
			throw new Exception("URI 不能为 null");
		}

		// 使用 QueryStringDecoder 对请求的 URI 进行解码
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
		// 创建 WebSocket 握手处理器工厂
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				queryStringDecoder.uri(),  // WebSocket 端点的 URI
				"", // 子协议为空
				true); // 允许 WebSocket 扩展

		// 使用工厂尝试创建 WebSocketServerHandshaker 实例
		handshaker = wsFactory.newHandshaker(req);

		// 如果握手处理器为 null，表示不支持的 WebSocket 版本
		if (handshaker == null) {
			// 发送不支持的版本响应
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			// 执行握手
			handshaker.handshake(ctx.channel(), req);
			// 设置 Channel 的属性，存储 WebSocket 请求的 URI
			Attribute<String> attr = ctx.channel().attr(URI_KEY);
			attr.set(queryStringDecoder.uri());

			// 确保每个 URI 都关联一个 ChannelGroup，用于后续消息广播
			ChannelGroup existingGroup = channelsByUri.get(uri);
			if (existingGroup == null) {
				// 如果还没有为这个 URI 创建 ChannelGroup，则创建一个新的并加入到映射中
				existingGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
				channelsByUri.put(uri, existingGroup);
			}
			// 将当前 Channel 添加到对应的 ChannelGroup
			existingGroup.add(ctx.channel());
		}
	}

	/**
	 * 处理 WebSocket 帧的方法。
	 *
	 * @param ctx   ChannelHandler 和 Channel 之间的上下文关系
	 * @param frame WebSocketFrame WebSocket 数据帧
	 */
	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 检查 WebSocket 帧类型，并进行相应处理
		if (frame instanceof CloseWebSocketFrame) {
			// 如果是关闭帧，关闭连接
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		} else if (frame instanceof PingWebSocketFrame) {
			// 如果是 Ping 帧，发送 Pong 帧作为响应
			ctx.write(new PongWebSocketFrame(frame.content().retain()));
			return;
		} else if (frame instanceof TextWebSocketFrame) {
			// 如果是文本帧，提取文本内容
			TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
			String request = textFrame.text();
			// 获取存储在 Channel 属性中的 URI
			Attribute<String> attr = ctx.channel().attr(URI_KEY);
			String uri = attr.get();
			System.out.println(request);
			// 广播消息到所有相同 URI 的 Channel
			if (uri != null) {
				broadcast(uri, request, ctx);
			}
		}
	}

	/**
	 * 广播消息到所有相同 URI 的 Channel。
	 *
	 * @param uri     用于定位 ChannelGroup 的 URI
	 * @param message 要广播的消息
	 * @param ctx     ChannelHandler 和 Channel 之间的上下文关系
	 */
	private void broadcast(String uri, String message, ChannelHandlerContext ctx) {
		// 根据 URI 获取对应的 ChannelGroup
		ChannelGroup channels = channelsByUri.get(uri);
		if (channels != null) {
			// 遍历 ChannelGroup 中的所有 Channel
			for (Channel ch : channels) {
				// // 如果 Channel 不是当前发送消息的 Channel，则向该 Channel 发送消息
				if (ch != ctx.channel()) {
					ch.writeAndFlush(new TextWebSocketFrame(message));
				}
			}
		}
	}

	/**
	 * 发送 HTTP 响应的方法。
	 *
	 * @param ctx ChannelHandler 和 Channel 之间的上下文关系
	 * @param res
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, DefaultFullHttpResponse res) {
		// 如果响应的状态码不是 200
		if (res.status().code() != 200) {
			// 创建一个字节缓冲区，包含状态码的描述信息
			ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
			// 将字节缓冲区的内容写入响应的 content
			res.content().writeBytes(buf);
			// 释放字节缓冲区
			buf.release();
			// 设置响应内容的长度
			HttpUtil.setContentLength(res, res.content().readableBytes());
		}

		// 创建一个 ChannelFutureListener 用于处理发送响应后的操作
		ChannelFutureListener futureListener = future -> {
			// 如果操作未成功完成
			if (!future.isSuccess()) {
				// 打印异常信息
				future.cause().printStackTrace();
				// 关闭 Channel
				future.channel().close();
			}
		};
		// 将响应写入并刷新 Channel 的 pipeline，然后添加之前创建的监听器
		ctx.channel().writeAndFlush(res).addListener(futureListener);
	}

	/**
	 * 捕获异常的方法，关闭发生异常的 Channel。
	 *
	 * @param ctx   ChannelHandler 和 Channel 之间的上下文关系
	 * @param cause
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 打印异常信息
		cause.printStackTrace();
		// 关闭 Channel
		ctx.close();
	}
}
