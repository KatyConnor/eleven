package com.hx.nine.eleven.web.socket.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auth wml
 * @date 2024/9/30
 * @Desc 定义一个WebSocket客户端处理器类，继承SimpleChannelInboundHandler类并处理Object类型的消息
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

	/**
	 * 声明WebSocketClientHandshaker对象，用于处理WebSocket握手
	 */
	private final WebSocketClientHandshaker handshaker;

	/**
	 * 声明一个ChannelPromise对象，用于在握手完成时通知其他组件
	 */
	private ChannelPromise handshakeFuture;

	/**
	 * 构造函数，初始化handshaker
	 *
	 * @param handshaker
	 */
	public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
		this.handshaker = handshaker;
	}

	/**
	 * 返回 handshakeFuture 对象
	 *
	 * @return
	 */
	public ChannelFuture handshakeFuture() {
		return handshakeFuture;
	}


	/**
	 * 当处理器被添加到ChannelPipeline中时调用
	 *
	 * @param ctx
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		// 创建一个新的ChannelPromise对象
		handshakeFuture = ctx.newPromise();
	}


	/**
	 * 当 Channel 变为活动状态时调用
	 *
	 * @param ctx
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("Channel active, initiating handshake.");
		// 发起WebSocket握手
		handshaker.handshake(ctx.channel());
	}

	/**
	 * 当Channel变为非活动状态时调用
	 *
	 * @param ctx
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.info("Channel inactive.");
	}

	/**
	 * 读取Channel中的数据
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 获取当前的Channel
		Channel ch = ctx.channel();

		// 如果握手尚未完成
		if (!handshaker.isHandshakeComplete()) {
			handleHandshake(ch, (FullHttpResponse) msg);
			return;
		}

		// 如果收到的是FullHttpResponse，抛出异常
		if (msg instanceof FullHttpResponse) {
			handleUnexpectedHttpResponse((FullHttpResponse) msg);
			return;
		}

		// 处理WebSocket帧
		handleWebSocketFrame(ch, (WebSocketFrame) msg);
	}


	/**
	 * 处理握手
	 *
	 * @param ch
	 * @param response
	 */
	private void handleHandshake(Channel ch, FullHttpResponse response) {
		try {
			handshaker.finishHandshake(ch, response);
			logger.info("WebSocket Handshake complete!");
			handshakeFuture.setSuccess();
		} catch (WebSocketHandshakeException e) {
			logger.error("WebSocket handshake failed!", e);
			handshakeFuture.setFailure(e);
		}
	}

	/**
	 * 处理意外的HTTP响应
	 *
	 * @param response
	 */
	private void handleUnexpectedHttpResponse(FullHttpResponse response) {
		throw new IllegalStateException("Unexpected FullHttpResponse (status=" + response.status() +
				", content=" + response.content().toString(CharsetUtil.UTF_8) + ")");
	}

	/**
	 * 处理WebSocket帧
	 *
	 * @param ch
	 * @param frame
	 */
	private void handleWebSocketFrame(Channel ch, WebSocketFrame frame) {
		if (frame instanceof TextWebSocketFrame) {
			logger.info("收到消息: " + ((TextWebSocketFrame) frame).text());
		} else if (frame instanceof PongWebSocketFrame) {
			logger.info("收到 PongWebSocketFrame");
		} else if (frame instanceof CloseWebSocketFrame) {
			logger.info("收到 CloseWebSocketFrame");
			ch.close();
		} else {
			logger.warn("收到不支持的 WebSocketFrame: " + frame.getClass().getName());
		}
	}


	/**
	 * 当处理过程中出现异常时调用
	 *
	 * @param ctx
	 * @param cause
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 记录异常信息
		logger.error("异常 : ", cause);
		// 如果握手未完成，设置handshakeFuture为失败状态
		if (!handshakeFuture.isDone()) {
			handshakeFuture.setFailure(cause);
		}
		// 关闭Channel
		ctx.close();
	}
}
