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
package io.edurt.gcm.netty.dispatcher;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class DispatcherParameter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherParameter.class);

    public Map<String, String> getRequestParam(Object clazz, HttpRequest request)
    {
        return getParamFromRequest(clazz, request);
    }

    /**
     * Extract client request parameters
     * Extract the parameters carried by the request connection, for example:
     * <code>/api/v1/home?name=12</code>
     * <p>When not been extracted to the parameter, the parameter passed in the body is extracted</p>
     * <p>When the parameters are extracted, continue to extract the parameters passed in the body and merge them</p>
     *
     * @param clazz The client requests to map the controller instance of the back end
     * @param request client request
     * @return Client request parameter list
     */
    private Map<String, String> getParamFromRequest(Object clazz, HttpRequest request)
    {
        if (ObjectUtils.isEmpty(request)) {
            LOGGER.error("Unrecognized error request");
            throw new RuntimeException("Unrecognized error request");
        }
        Map<String, String> params = new ConcurrentHashMap<>();
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        if (ObjectUtils.isNotEmpty(queryDecoder)) {
            queryDecoder.parameters().entrySet().forEach(entry -> params.put(entry.getKey(), entry.getValue().get(0)));
        }
        else {
            LOGGER.warn("Unable to get instance of request parameter through client");
        }
        HttpPostRequestDecoder bodyDecoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        if (ObjectUtils.isNotEmpty(bodyDecoder)) {
            bodyDecoder.getBodyHttpDatas().forEach(interfaceHttpData -> {
                if (interfaceHttpData.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) interfaceHttpData;
                    params.put(attribute.getName(), attribute.getValue());
                }
            });
        }
        else {
            LOGGER.warn("Unable to get instance of request body parameter through client");
        }
        return params;
    }
}
