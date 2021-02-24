package io.edurt.gcm.netty.dispatcher;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.netty.annotation.ResponseBody;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.configuration.NettyConfiguration;
import io.edurt.gcm.netty.configuration.NettyConfigurationDefault;
import io.edurt.gcm.netty.exception.NettyException;
import io.edurt.gcm.netty.filter.SessionFilter;
import io.edurt.gcm.netty.router.Router;
import io.edurt.gcm.netty.router.Routers;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

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
        String content = null;
        URI uri = URI.create(httpRequest.uri());
        String requestUrl = uri.getPath();
        Router router = Routers.getRouter(requestUrl);
        LOGGER.info("Obtain and analyze the client request information from {}", requestUrl);
        if (ObjectUtils.isEmpty(router)) {
            httpResponse.setStatus(HttpResponseStatus.NOT_FOUND);
            LOGGER.error("The requested path <{}> was not found", requestUrl);
            content = "Oops,the requested path was not found.";
        } else {
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
            Class<?> clazz = Class.forName(ctrlClass);
            Object ctrlObject = injector.getInstance(clazz);
            LOGGER.debug("Current execute controller {}", ctrlObject);
            DispatcherParameter dispatcherParameter = injector.getInstance(DispatcherParameter.class);
            ConcurrentHashMap<String, ArrayList> classAndParam = dispatcherParameter.getRequestObjectAndParam(ctrlObject, httpRequest, httpResponse, methodName);
            ArrayList<Object> classList = classAndParam.get(DispatcherParameter.CLASS);
            Class[] classes = classList.toArray(new Class[classList.size()]);
            Method method = ctrlObject.getClass().getMethod(methodName, classes);
            // Fix the problem of using @RestController annotation to return data results
            if (method.isAnnotationPresent(ResponseBody.class) || clazz.isAnnotationPresent(RestController.class)) {
                Gson gson = new Gson();
                try {
                    content = gson.toJson(method.invoke(ctrlObject, classAndParam.get(DispatcherParameter.PARAM).toArray()));
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
        }
        httpResponse.content().writeBytes(Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
    }
}
