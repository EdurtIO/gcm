/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.edurt.gcm.netty.handler;

import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.common.utils.StringUtils;
import io.edurt.gcm.netty.configuration.NettyConfiguration;
import io.edurt.gcm.netty.configuration.NettyConfigurationDefault;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class HttpRequestStaticHandler
        extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private final Properties configuration;

    public HttpRequestStaticHandler(Properties configuration)
    {
        this.configuration = configuration;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest)
            throws Exception
    {
        String staticResource = PropertiesUtils.getStringValue(this.configuration,
                NettyConfiguration.VIEW_TEMPLATE_STATIC,
                NettyConfigurationDefault.VIEW_TEMPLATE_STATIC);
        String uri = httpRequest.uri();
        try (InputStream inputStream = getClass().getResourceAsStream(staticResource + uri)) {
            if (ObjectUtils.isEmpty(inputStream)) {
                ctx.fireChannelRead(httpRequest.retain());
                return;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            ByteBuf byteBuf = Unpooled.wrappedBuffer(byteArrayOutputStream.toByteArray()).retain();
            HttpResponseStatus status = new HttpResponseStatus(200, "Ok");
            HttpVersion version = new HttpVersion(HttpVersion.HTTP_1_1.text(), false);
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(version, status, byteBuf);
            HttpHeaders headers = httpResponse.headers();
            headers.set(HttpHeaderNames.CONTENT_TYPE, contentType(uri.substring(uri.lastIndexOf(".") + 1)));
            headers.set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(byteBuf.readableBytes()));
            ctx.write(httpResponse);
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListener(ChannelFutureListener.CLOSE);
        }
    }

    private String contentType(String fileExt)
    {
        String mime = MIME.get(fileExt);
        return StringUtils.isNotEmpty(mime) ? mime : "text/plain";
    }

    private static final HashMap<String, String> MIME = new HashMap<String, String>(256);

    static {
        MIME.put("css", "text/css");
        MIME.put("js", "application/x-javascript");
    }
}
