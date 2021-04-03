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

import io.edurt.gcm.common.utils.ObjectUtils;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GcmHttpNetworkInterceptor
        implements Interceptor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GcmHttpNetworkInterceptor.class);

    public GcmHttpNetworkInterceptor()
    {}

    @Override
    public Response intercept(Interceptor.Chain chain)
    {
        Response response = null;
        String responseBody;
        try {
            Request request = chain.request();
            response = chain.proceed(request);
            responseBody = response.body().string();
            MediaType mediaType = response.body().contentType();
            response = response.newBuilder().body(ResponseBody.create(mediaType, responseBody)).build();
        }
        catch (Exception e) {
            LOGGER.error("Connected error, details {}", e);
        }
        return response;
    }

    private String getRequestBody(Request request)
    {
        if (ObjectUtils.isEmpty(request)) {
            return null;
        }
        RequestBody requestBody = request.body();
        if (ObjectUtils.isEmpty(requestBody)) {
            return null;
        }
        try {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = StandardCharsets.UTF_8;
            return buffer.readString(charset);
        }
        catch (IOException e) {
            LOGGER.error("Get request body error, details {}", e);
            return null;
        }
    }
}
