package io.edurt.gcm.netty.dispatcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.netty.annotation.Controller;
import io.edurt.gcm.netty.annotation.ResponseBody;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.configuration.NettyConfiguration;
import io.edurt.gcm.netty.configuration.NettyConfigurationDefault;
import io.edurt.gcm.netty.exception.NettyException;
import io.edurt.gcm.netty.filter.SessionFilter;
import io.edurt.gcm.netty.handler.HttpCharsetContentHandler;
import io.edurt.gcm.netty.router.Router;
import io.edurt.gcm.netty.router.Routers;
import io.edurt.gcm.netty.type.Charseter;
import io.edurt.gcm.netty.type.ContentType;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

@Singleton
public class RequestDispatcher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestDispatcher.class);
    private static final Gson GSON = new GsonBuilder().create();
    private static Properties configuration;

    @Inject
    private Injector injector;

    @Inject
    private SessionFilter sessionFilter;

    @Inject
    private Configuration freemarkerConfiguration;

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
        HttpCharsetContentHandler httpCharsetContentHandler = injector.getInstance(HttpCharsetContentHandler.class);
        URI uri = URI.create(httpRequest.uri());
        String requestUrl = uri.getPath();
        Router router = Routers.getRouter(requestUrl);
        LOGGER.info("Obtain and analyze the client request information from {}", requestUrl);
        if (ObjectUtils.isEmpty(router)) {
            httpResponse.setStatus(HttpResponseStatus.NOT_FOUND);
            LOGGER.error("The requested path <{}> was not found or not supported it!", requestUrl);
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
        Class<?> clazz = Class.forName(ctrlClass);
        Object ctrlObject = injector.getInstance(clazz);
        LOGGER.debug("Current execute controller {}", ctrlObject);
        ParameterDispatcher parameterDispatcher = injector.getInstance(ParameterDispatcher.class);
        ConcurrentHashMap<String, ArrayList> classAndParam = parameterDispatcher.getRequestObjectAndParam(ctrlObject, httpRequest, httpResponse, methodName, router);
        ArrayList<Object> classList = classAndParam.get(ParameterDispatcher.CLASS);
        Class[] classes = classList.toArray(new Class[classList.size()]);
        // When accessing a view, it supports view parameter parsing
        Object[] objects = classAndParam.get(ParameterDispatcher.PARAM).toArray();
        Method method = ctrlObject.getClass().getMethod(methodName, classes);
        String content = null;
        // Fix the problem of using @RestController annotation to return data results
        if (method.isAnnotationPresent(ResponseBody.class) || clazz.isAnnotationPresent(RestController.class)) {
            try {
                content = GSON.toJson(method.invoke(ctrlObject, classAndParam.get(ParameterDispatcher.PARAM).toArray()));
                httpResponse.setStatus(HttpResponseStatus.OK);
            }
            catch (InvocationTargetException ex) {
                if (ex.getCause().getClass() == NettyException.class) {
                    NettyException exception = (NettyException) ex.getCause();
                    content = GSON.toJson(exception);
                }
                else {
                    ex.printStackTrace();
                    content = GSON.toJson(new NettyException(500, ex.getCause().getMessage()));
                }
                httpResponse.setStatus(HttpResponseStatus.BAD_GATEWAY);
            }
            httpResponse.headers().set(CONTENT_TYPE, httpCharsetContentHandler.getContentAndCharset(Charseter.UTF8, ContentType.APPLICATION_JSON));
        }
        else if (clazz.isAnnotationPresent(Controller.class)) {
            String viewName = String.valueOf(method.invoke(ctrlObject, objects));
            freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), getTemplatePath());
            Template template = freemarkerConfiguration.getTemplate(viewName + PropertiesUtils.getStringValue(configuration,
                    NettyConfiguration.VIEW_TEMPLATE_SUFFIX,
                    NettyConfigurationDefault.VIEW_TEMPLATE_SUFFIX));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // In view, we only need to extract the first one
            template.process(objects[0], new OutputStreamWriter(outputStream));
            content = outputStream.toString(CharsetUtil.UTF_8.name());
            httpResponse.headers().set(CONTENT_TYPE, httpCharsetContentHandler.getContentAndCharset(Charseter.UTF8, ContentType.TEXT_HTML));
        }
        else {
            // TODO: We don't do any processing here for the time being, and we will support it later
            LOGGER.warn("We don't do any processing here for the time being, and we will support it later");
        }
        httpResponse.content().writeBytes(Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
    }

    private String getTemplatePath()
    {
        String templatePath = PropertiesUtils.getStringValue(configuration,
                NettyConfiguration.VIEW_TEMPLATE_PATH,
                NettyConfigurationDefault.VIEW_TEMPLATE_PATH);
        // Handling templates in jar packages
        if (templatePath.toLowerCase().startsWith("classpath")) {
            return templatePath.split(":")[1];
        }
        return templatePath;
    }
}
