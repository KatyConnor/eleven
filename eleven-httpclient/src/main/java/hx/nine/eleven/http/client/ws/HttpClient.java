package hx.nine.eleven.http.client.ws;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author wml
 * 2020-06-08
 */
public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private String httpContentEncoding;
    private volatile EventLoopGroup group;
    private volatile Bootstrap b;
    private String host;
    private int port;
    private int connectTimeoutMillis;
    private int readTimeoutMillis;
    private boolean isBootstrapSetOnce = true;
    private volatile boolean isBootstrapSeted = false;

    public HttpClient() {
    }

    public String getHttpContentEncoding() {
        return this.httpContentEncoding;
    }

    public void setHttpContentEncoding(String httpContentEncoding) {
        this.httpContentEncoding = httpContentEncoding;
    }

    public void initBootstrap() {
        this.group = new NioEventLoopGroup();
        this.b = new Bootstrap();
        this.b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connectTimeoutMillis);
        this.b.group(this.group);
        this.b.channel(NioSocketChannel.class);
        this.b.handler(new HttpClientIntializer(this.readTimeoutMillis));
        this.isBootstrapSeted = true;
    }

    public boolean isBootstrapSetOnce() {
        return this.isBootstrapSetOnce;
    }

    public void setBootstrapSetOnce(boolean isBootstrapSetOnce) {
        this.isBootstrapSetOnce = isBootstrapSetOnce;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public String sendAndRecive(URI uri, byte[] httpContentBytes) throws InterruptedException, URISyntaxException, UnsupportedEncodingException {
        if (this.isBootstrapSetOnce) {
            if (!this.isBootstrapSeted) {
                this.initBootstrap();
            }
        } else {
            this.initBootstrap();
        }

        ChannelFuture f = this.b.connect(this.host, this.port).sync();
        Channel ch = f.sync().channel();
        ChannelHandler chhandler = ch.pipeline().last();
        HttpClientHandler handler = (HttpClientHandler)chhandler;
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(), Unpooled.wrappedBuffer(httpContentBytes));
        request.headers().set(HttpHeaderNames.HOST, this.host);
        request.headers().set(HttpHeaderNames.CONTENT_ENCODING, this.httpContentEncoding);
        request.headers().set("Content-Type", "text/xml;charset=" + this.httpContentEncoding);
        request.headers().set("Content-Length", request.content().readableBytes());
        request.headers().set("Connection", HttpHeaderValues.KEEP_ALIVE);
        byte[] contentArrays = request.content().array();
        String contentHexMD5 = DigestUtils.md5Hex(contentArrays).toUpperCase();
        request.headers().set("Content-MD5", contentHexMD5);
        Charset charset = null;
        if (this.httpContentEncoding != null) {
            try {
                charset = Charset.forName(this.httpContentEncoding);
            } catch (Exception var13) {
                logger.warn("httpContentEncoding:[{}] can not get!", this.httpContentEncoding, var13);
            }
        }

        if (charset == null) {
            charset = Charset.defaultCharset();
            logger.warn("defaultCharset [{}]", charset.name());
        }

        logger.info("httpclient send{}:" + request + "\n" + request.content().toString(charset), charset);
        f.channel().writeAndFlush(request);
        f.channel().closeFuture().sync();
        byte[] respBytes = handler.getRespBytes();
        String respString = new String(respBytes, charset);
        logger.info("httpclient recv:" + respString + "\n");
        return respString;
    }
}
