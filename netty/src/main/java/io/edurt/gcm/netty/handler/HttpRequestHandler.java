package io.edurt.gcm.netty.handler;

import com.google.inject.Injector;
import io.edurt.gcm.netty.NettyServer;
import io.edurt.gcm.netty.dispatcher.RequestDispatcher;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

public class HttpRequestHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private final Injector injector;
    private final String websocketPath;
    private Properties configuration;

    public HttpRequestHandler(Injector injector, String websocketPath, Properties configuration)
    {
        this.injector = injector;
        this.websocketPath = websocketPath;
        this.configuration = configuration;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest)
    {
        if (httpRequest.uri().equals(websocketPath)) {
            LOGGER.info("Currently, socket service from /{} is not supported", websocketPath);
        }
        else {
            if (HttpUtil.is100ContinueExpected(httpRequest)) {
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(response);
            }
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
            RequestDispatcher.builderConfiguration(configuration);
            injector.getInstance(RequestDispatcher.class).processor(ctx, httpRequest, httpResponse);
            httpResponse.headers().set(CONTENT_LENGTH, httpResponse.content().readableBytes());
            if (HttpUtil.isKeepAlive(httpRequest)) {
                httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.write(httpResponse);
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!HttpUtil.isKeepAlive(httpRequest)) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
