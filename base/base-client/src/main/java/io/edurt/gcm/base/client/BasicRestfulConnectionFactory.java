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
package io.edurt.gcm.base.client;

import io.edurt.gcm.common.utils.ObjectUtils;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BasicRestfulConnectionFactory
        implements RestfulConnectionFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicRestfulConnectionFactory.class);
    private String connectionUrl;
    private OkHttpClient okHttpClient;
    private final BaseRestfulConfig config;

    private BasicRestfulConnectionFactory(BaseRestfulConfig config)
    {
        this.config = config;
    }

    public static BasicRestfulConnectionFactory builder(BaseRestfulConfig config)
    {
        return new BasicRestfulConnectionFactory(config);
    }

    @Override
    public OkHttpClient openConnection(BaseRestfulConfig config)
    {
        okHttpClient = new OkHttpClient.Builder()
                .callTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
        connectionUrl = String.format("%s://%s:%s/%s", config.getProtocol(), config.getHost(), config.getPort(), config.getPath());
        return okHttpClient;
    }

    @Override
    public Response postExecute(Map<String, Object> params)
    {
        return null;
    }

    @Override
    public Response getExecute(Map<String, Object> params)
            throws IOException
    {
        openConnection(this.config);
        LOGGER.debug("Execute get method on {}", connectionUrl);
        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder httpUrl = HttpUrl.parse(connectionUrl).newBuilder();
        if (ObjectUtils.isNotEmpty(params)) {
            params.entrySet().forEach(entry -> httpUrl.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue())));
        }
        requestBuilder.url(httpUrl.build());
        Request request = requestBuilder.build();
        return okHttpClient.newCall(request).execute();
    }

    @Override
    public Response postExecute(String body)
            throws IOException
    {
        openConnection(this.config);
        Request.Builder builder = new Request.Builder();
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), body);
        Request request = builder
                .url(connectionUrl)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request).execute();
    }
}
