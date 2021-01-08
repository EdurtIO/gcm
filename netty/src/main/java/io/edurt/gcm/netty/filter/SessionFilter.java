package io.edurt.gcm.netty.filter;

import com.google.inject.Inject;
import io.edurt.gcm.netty.dispatcher.RequestDispatcher;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

public class SessionFilter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFilter.class);

    @Inject
    private RequestDispatcher requestProcessor;

    private static void writeErrorResponse(HttpResponseStatus responseStatus, FullHttpResponse httpResponse, String message)
    {
        String json = "{\"status\":" + responseStatus.code() + ", \"message\":\"" + message + "\"}";
        httpResponse.setStatus(responseStatus);
        httpResponse.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        httpResponse.content().writeBytes(Unpooled.copiedBuffer(json, CharsetUtil.UTF_8));
    }

    public void doFilter(ChannelHandlerContext ctx, FullHttpRequest httpRequest, FullHttpResponse httpResponse)
    {
        String uri = httpRequest.uri();
        // TODO: Add support skip uri
        LOGGER.info("doFilter from {}", uri);
        try {
            requestProcessor.triggerAction(httpRequest, httpResponse);
        }
        catch (Exception ex) {
            LOGGER.error("There is an exception in session filtering. The specific information is {}", ex);
            writeErrorResponse(HttpResponseStatus.BAD_GATEWAY, httpResponse, ex.getMessage());
        }
    }
}
