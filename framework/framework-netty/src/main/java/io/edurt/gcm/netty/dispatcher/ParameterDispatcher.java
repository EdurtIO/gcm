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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Singleton;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.netty.annotation.PathVariable;
import io.edurt.gcm.netty.annotation.RequestBody;
import io.edurt.gcm.netty.annotation.RequestParam;
import io.edurt.gcm.netty.exception.NettyException;
import io.edurt.gcm.netty.handler.HttpPathHandler;
import io.edurt.gcm.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.edurt.gcm.netty.router.Router;
import io.edurt.gcm.netty.view.ParamModel;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ParameterDispatcher
{
    public static final String CLASS = "class";
    public static final String PARAM = "param";
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterDispatcher.class);
    private static final Gson GSON = new GsonBuilder().create();

    /**
     * Analyze the client request, obtain the request parameters and the directed execution instance of the underlying service
     *
     * @param clazz Directed execution instance of underlying service
     * @param request Client request
     * @param response Server response
     * @param requestMethodName The client request points to the class method actually executed by the server
     * @return Directed execution instance composition map of request parameters and underlying services
     * @throws ClassNotFoundException Class.forName
     */
    public ConcurrentHashMap<String, ArrayList> getRequestObjectAndParam(Object clazz, FullHttpRequest request, FullHttpResponse response, String requestMethodName, Router router)
            throws NettyException
    {
        Map<String, String> requestParams = getRequestParam(request);
        Method[] methods = clazz.getClass().getMethods();
        ArrayList<Class> classList = new ArrayList<>();
        ArrayList<Object> paramList = new ArrayList<>();
        try {
            for (Method method : methods) {
                if (method.getName().equals(requestMethodName)) {
                    for (Parameter parameter : method.getParameters()) {
                        Class parameterClass = Class.forName(parameter.getType().getTypeName());
                        if (parameterClass == FullHttpRequest.class) {
                            LOGGER.debug("Processing data information carried by FullHttpRequest");
                            paramList.add(request);
                            classList.add(parameterClass);
                        }
                        else if (parameterClass == FullHttpResponse.class) {
                            LOGGER.debug("Processing data information carried by FullHttpResponse");
                            paramList.add(response);
                            classList.add(parameterClass);
                        }
                        else if (parameterClass == ParamModel.class) {
                            paramList.add(new ParamModel());
                            classList.add(parameterClass);
                        }
                        else if (parameter.getAnnotation(RequestParam.class) != null) {
                            LOGGER.debug("Processing data information carried by RequestParam");
                            String requestParamKey = parameter.getAnnotation(RequestParam.class).value();
                            String defaultValue = parameter.getAnnotation(RequestParam.class).defaultValue();
                            String requestParamVal = requestParams.get(requestParamKey);
                            if (parameterClass == Long.class) {
                                paramList.add(Long.valueOf(getParamValue(requestParamVal, defaultValue)));
                                classList.add(Long.class);
                            }
                            else if (parameterClass == Integer.class) {
                                paramList.add(Integer.valueOf(getParamValue(requestParamVal, defaultValue)));
                                classList.add(Integer.class);
                            }
                            else {
                                paramList.add(getParamValue(requestParamVal, defaultValue));
                                classList.add(String.class);
                            }
                        }
                        else if (parameter.getAnnotation(RequestBody.class) != null) {
                            LOGGER.debug("Processing data information carried by RequestBody");
                            ByteBuf bf = request.content();
                            byte[] byteArray = new byte[bf.capacity()];
                            bf.readBytes(byteArray);
                            // The original data type should be used here, otherwise class conversion error will occur. The following is an error example:
                            // Caused by: java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be xxxx
                            paramList.add(GSON.fromJson(new String(byteArray, StandardCharsets.UTF_8), parameter.getParameterizedType()));
                            classList.add(parameterClass);
                        }
                        else if (ObjectUtils.isNotEmpty(parameter.getAnnotation(PathVariable.class))) {
                            Map<String, String> params = HttpPathHandler.getParams(request.uri(), new String[] {router.getUrl()});
                            paramList.add(getParamValue(params.get(parameter.getAnnotation(PathVariable.class).value()),
                                    params.get(parameter.getAnnotation(PathVariable.class).defaultValue())));
                            classList.add(parameterClass);
                        }
                        else {
                            LOGGER.debug("Ignore it if it is not resolved");
                            paramList.add(null);
                            classList.add(Object.class);
                        }
                    }
                    break;
                }
            }
        }
        catch (Exception ex) {
            LOGGER.info("Convert parameter error {}", ex);
            throw new NettyException(500, ex.getMessage());
        }
        return new ConcurrentHashMap<String, ArrayList>()
        {{
            put(CLASS, classList);
            put(PARAM, paramList);
        }};
    }

    /**
     * Extract client request parameters
     * Extract the parameters carried by the request connection, for example:
     * <code>/api/v1/home?name=12</code>
     * <p>When not been extracted to the parameter, the parameter passed in the body is extracted</p>
     * <p>When the parameters are extracted, continue to extract the parameters passed in the body and merge them</p>
     *
     * @param request client request
     * @return Client request parameter list
     */
    private Map<String, String> getRequestParam(FullHttpRequest request)
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

    private String getParamValue(String value, String defaultValue)
    {
        if (value == null) {
            return defaultValue;
        }
        else {
            return value;
        }
    }
}
