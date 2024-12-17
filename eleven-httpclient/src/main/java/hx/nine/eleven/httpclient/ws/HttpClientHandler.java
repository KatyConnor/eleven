package hx.nine.eleven.httpclient.ws;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wml
 * 2020-06-08
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHandler.class);
    private Charset charset;
    private byte[] respBytes;

    public HttpClientHandler() {
    }

    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws IOException {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse)msg;
            if (logger.isDebugEnabled()) {
                logger.debug("recv:[\n{}]", response);
            }
        }

        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent)msg;
            ByteBuf buf = content.content();
            this.respBytes = new byte[buf.readableBytes()];
            buf.readBytes(this.respBytes);
        }

        ctx.channel().close();
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.trace("channelActive");
        super.channelActive(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.trace("channelInactive");
        super.channelInactive(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("exceptionCaught", cause);
        ctx.channel().close();
    }

    public byte[] getRespBytes() {
        return this.respBytes;
    }

}
