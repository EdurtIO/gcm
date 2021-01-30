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
package io.edurt.gcm.common.http;

import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GcmHttp
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GcmHttp.class);
    private static final int CONNECTION_TIME_OUT = 200000; // Connection timeout
    private static final int SOCKET_TIME_OUT = 20000; // Read write timeout
    private static final int MAX_IDLE_CONNECTIONS = 30; // Number of idle connections
    private static final long KEEP_ALIVE_TIME = 60000L; // Keep connected time
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static volatile GcmHttp httpClient;
    private final OkHttpClient okHttpClient;

    /**
     * Construction of gcmhttp client instance
     *
     * @param retry Number of retries after errors
     * @param autoConnected Automatically reconnects after disconnection
     */
    public GcmHttp(Integer retry, Boolean autoConnected)
    {
        LOGGER.info("Connected to remote retry count {}", retry);
        if (ObjectUtils.isEmpty(retry)) {
            retry = 3;
        }
        if (ObjectUtils.isEmpty(autoConnected)) {
            autoConnected = Boolean.FALSE;
        }
        ConnectionPool connectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS);
        this.okHttpClient = new OkHttpClient()
                .newBuilder()
                .readTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(autoConnected)
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new GcmHttpRetryInterceptor(retry))
                .addNetworkInterceptor(new GcmHttpNetworkInterceptor())
                .build();
    }

    public static GcmHttp getInstance(Integer retry, Boolean autoConnected)
    {
        httpClient = new GcmHttp(retry, autoConnected);
        return httpClient;
    }

    public String get(String url)
    {
        return this.get(url, null);
    }

    public String get(String url, Map<String, String> pathParams)
    {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if (ObjectUtils.isNotEmpty(pathParams)) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(builder.build().toString())
                .build();
        return execute(request);
    }

    public String post(String url, String body)
    {
        return post(url, null, body);
    }

    public String post(String url, Map<String, String> pathParams, String body)
    {
        RequestBody requestBody = RequestBody.create(JSON, body);
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if (ObjectUtils.isNotEmpty(pathParams)) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                builder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()
                .post(requestBody)
                .addHeader("Accept-Encoding", "identity")
                .url(builder.build().toString())
                .build();
        return execute(request);
    }

    private String execute(Request request)
    {
        String responseBody = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            responseBody = response.body().string();
        }
        catch (IOException | NullPointerException e) {
            LOGGER.error("Execute error {}", e);
        }
        return responseBody;
    }
}
