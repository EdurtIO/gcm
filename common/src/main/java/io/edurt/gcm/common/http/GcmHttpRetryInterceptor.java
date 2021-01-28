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

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GcmHttpRetryInterceptor
        implements Interceptor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GcmHttpRetryInterceptor.class);
    public int maxRetryCount;
    private int count = 1;

    public GcmHttpRetryInterceptor(int maxRetryCount)
    {
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public Response intercept(Chain chain)
    {
        return retry(chain);
    }

    public Response retry(Chain chain)
    {
        LOGGER.debug("Retry the connection service start time {}", System.currentTimeMillis());
        LOGGER.debug("Try first {} Retry the connection service", this.count);
        Response response = null;
        Request request = chain.request();
        try {
            Thread.sleep(2000);
            response = chain.proceed(request);
            while (!response.isSuccessful() && count < maxRetryCount) {
                count++;
                response = retry(chain);
            }
        }
        catch (Exception e) {
            LOGGER.error("Retry connection error, error details {}", e);
            while (count < maxRetryCount) {
                count++;
                response = retry(chain);
            }
        }
        return response;
    }
}
