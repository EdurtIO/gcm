package io.edurt.gcm.netty.dispatcher;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.netty.annotation.RequestBody;
import io.edurt.gcm.netty.annotation.RequestParam;
import io.edurt.gcm.netty.annotation.ResponseBody;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.configuration.NettyConfiguration;
import io.edurt.gcm.netty.configuration.NettyConfigurationDefault;
import io.edurt.gcm.netty.exception.NettyException;
import io.edurt.gcm.netty.filter.SessionFilter;
import io.edurt.gcm.netty.router.Router;
import io.edurt.gcm.netty.router.Routers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

@Singleton
public class DispatcherRequest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherRequest.class);
    private static Properties configuration;

    @Inject
    private Injector injector;

    @Inject
    private SessionFilter sessionFilter;

    public static final void builderConfiguration(Properties properties)
    {
        configuration = properties;
    }

    public void processor(ChannelHandlerContext ctx, FullHttpRequest httpRequest, FullHttpResponse httpResponse)
    {
        LOGGER.info("Add session filter");
        sessionFilter.doFilter(ctx, httpRequest, httpResponse);
    }

    public void triggerAction(FullHttpRequest httpRequest, FullHttpResponse httpResponse)
            throws Exception
    {
        URI uri = URI.create(httpRequest.uri());
        String requestUrl = uri.getPath();
        Router router = Routers.getRouter(requestUrl);
        LOGGER.info("Obtain and analyze the client request information from {}", requestUrl);
        if (ObjectUtils.isEmpty(router)) {
            httpResponse.setStatus(HttpResponseStatus.NOT_FOUND);
            LOGGER.error("The requested path <{}> was not found", requestUrl);
            return;
        }
        String methodName = router.getMethod().getName();
        String controller = PropertiesUtils.getStringValue(configuration,
                NettyConfiguration.CONTROLLER_PACKAGE,
                NettyConfigurationDefault.CONTROLLER_PACKAGE);
        String ctrlClass = router.getClazz().getName();
        try {
            Class.forName(ctrlClass);
        }
        catch (ClassNotFoundException e) {
            LOGGER.error("Unable to instantiate controller information. Please check whether the package name is correct and the system specified path is {}",
                    controller);
            httpResponse.setStatus(HttpResponseStatus.NOT_FOUND);
            return;
        }
        LOGGER.debug("Parsing method parameters, used to inject the corresponding entity!");
        ArrayList<Class> classList = new ArrayList<>();
        ArrayList<Object> objectList = new ArrayList<>();
        Class<?> clazz = Class.forName(ctrlClass);
        Object ctrlObject = injector.getInstance(clazz);
        LOGGER.debug("Current execute controller {}", ctrlObject);
        Map<String, String> requestParams = this.getRequestParams(httpRequest);
        Method[] methods = ctrlObject.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                for (Parameter parameter : method.getParameters()) {
                    Class parameterClass = Class.forName(parameter.getType().getTypeName());
                    if (parameterClass == FullHttpRequest.class) {
                        LOGGER.debug("Processing data information carried by FullHttpRequest");
                        objectList.add(httpRequest);
                        classList.add(parameterClass);
                    }
                    else if (parameterClass == FullHttpResponse.class) {
                        LOGGER.debug("Processing data information carried by FullHttpResponse");
                        objectList.add(httpResponse);
                        classList.add(parameterClass);
                    }
                    else if (parameter.getAnnotation(RequestParam.class) != null) {
                        LOGGER.debug("Processing data information carried by RequestParam");
                        String requestParamKey = parameter.getAnnotation(RequestParam.class).value();
                        String requestParamVal = requestParams.get(requestParamKey);
                        if (parameterClass == Long.class) {
                            objectList.add(Long.valueOf(requestParamVal));
                            classList.add(Long.class);
                        }
                        else if (parameterClass == Integer.class) {
                            objectList.add(Integer.valueOf(requestParamVal));
                            classList.add(Integer.class);
                        }
                        else {
                            objectList.add(String.valueOf(requestParamVal));
                            classList.add(String.class);
                        }
                    }
                    else if (parameter.getAnnotation(RequestBody.class) != null) {
                        LOGGER.debug("Processing data information carried by RequestBody");
                        ByteBuf bf = httpRequest.content();
                        byte[] byteArray = new byte[bf.capacity()];
                        bf.readBytes(byteArray);
                        // The original data type should be used here, otherwise class conversion error will occur. The following is an error example:
                        // Caused by: java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be xxxx
                        objectList.add((new Gson()).fromJson(new String(byteArray), parameter.getParameterizedType()));
                        classList.add(parameterClass);
                    }
                    else {
                        LOGGER.debug("Ignore it if it is not resolved");
                        objectList.add(null);
                        classList.add(Object.class);
                    }
                }
                break;
            }
        }
        Class[] classes = classList.toArray(new Class[classList.size()]);
        Object[] objects = objectList.toArray();
        Method method = ctrlObject.getClass().getMethod(methodName, classes);
        String content = null;
        // Fix the problem of using @RestController annotation to return data results
        if (method.isAnnotationPresent(ResponseBody.class) || clazz.isAnnotationPresent(RestController.class)) {
            Gson gson = new Gson();
            try {
                content = gson.toJson(method.invoke(ctrlObject, objects));
                httpResponse.setStatus(HttpResponseStatus.OK);
            }
            catch (InvocationTargetException ex) {
                if (ex.getCause().getClass() == NettyException.class) {
                    NettyException exception = (NettyException) ex.getCause();
                    content = gson.toJson(exception);
                }
                else {
                    ex.printStackTrace();
                    content = gson.toJson(new NettyException(500, ex.getCause().getMessage()));
                }
                httpResponse.setStatus(HttpResponseStatus.BAD_GATEWAY);
            }
            httpResponse.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        }
        else {
            // TODO: We don't do any processing here for the time being, and we will support it later
            LOGGER.warn("We don't do any processing here for the time being, and we will support it later");
        }
        httpResponse.content().writeBytes(Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
    }

    /**
     * Processing get post requests
     *
     * @param request HttpRequest
     * @return HttpRequest Params
     */
    private Map<String, String> getRequestParams(HttpRequest request)
    {
        LOGGER.debug("Processing get post requests, current method {}", request.method());
        Map<String, String> requestParams = new HashMap<>();
        if (request.method() == HttpMethod.GET || request.method() == HttpMethod.POST) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> params = decoder.parameters();
            for (Map.Entry<String, List<String>> next : params.entrySet()) {
                requestParams.put(next.getKey(), next.getValue().get(0));
            }
        }
        if (request.method() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : postData) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        return requestParams;
    }
}
