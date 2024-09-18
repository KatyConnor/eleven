package com.hx.http.client.ws;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;

/**
 * @author wml
 * 2020-06-08
 */
public class HttpClientIntializer extends ChannelInitializer<SocketChannel>{

    private int readTimeout;

    public HttpClientIntializer(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast("readTimeout", new ReadTimeoutHandler((long)this.readTimeout, TimeUnit.MILLISECONDS));
        p.addLast("encoder", new HttpRequestEncoder());
        p.addLast("decoder", new HttpResponseDecoder());
        p.addLast(new ChannelHandler[]{new HttpObjectAggregator(524288)});
        p.addLast("handler", new HttpClientHandler());
    }
}
