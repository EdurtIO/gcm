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
package io.edurt.gcm.netty.router;

import io.edurt.gcm.common.jdk.Classs;
import io.edurt.gcm.common.jdk.ObjectBuilder;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.StringUtils;
import io.edurt.gcm.netty.annotation.Controller;
import io.edurt.gcm.netty.annotation.RequestMapping;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.type.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class RouterScan
{
    public static final String URL_PREFIX = "/";
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterScan.class);

    private RouterScan()
    {}

    public static void scanRouters(String scanPackage)
    {
        LOGGER.info("Scan router from {}", scanPackage);
        Set<Class<?>> classes = Classs.scanClassInPackage(scanPackage);
        if (ObjectUtils.isEmpty(classes) || classes.size() < 1) {
            LOGGER.warn("Scan router is empty from {}", scanPackage);
        }
        else {
            classes.forEach(clazz -> {
                if (clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Controller.class)) {
                    String[] parentUrls = null;
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        parentUrls = clazz.getAnnotation(RequestMapping.class).value();
                    }
                    // Filtering only includes the methods for setting the scan package, and the methods included in the base class are ignored
                    List<Method> methods = Arrays.stream(clazz.getMethods())
                            .filter(method -> method.toGenericString().contains(scanPackage))
                            .collect(Collectors.toList());
                    String[] finalParentUrls = parentUrls;
                    methods.forEach(method -> {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                            // The class annotated with @RestController must not contain the actual access address. An exception is thrown.
                            if (ObjectUtils.isEmpty(mapping.value()) || mapping.value().length < 1) {
                                throw new RuntimeException(format("The class <%s> that identifies @RestController must contain the actual access address.",
                                        clazz.getCanonicalName()));
                            }
                            // The class annotated with @RestController must not contain the actual access method. An exception is thrown.
                            if (ObjectUtils.isEmpty(mapping.method()) || mapping.method().length < 1) {
                                throw new RuntimeException(format("The class <%s> method <%s> that identifies @RestController must contain the actual access method.",
                                        clazz.getCanonicalName(), method.getName()));
                            }
                            // TODO: Add to the global routing controller, and then modify the route loading method
                            Arrays.stream(mapping.value())
                                    .forEach(value -> Arrays.stream(mapping.method())
                                            .forEach(requestMethod -> Arrays.stream(mapping.value())
                                                    .forEach(url -> addRouter(finalParentUrls, method, clazz, requestMethod, url)))
                                    );
                        }
                        else {
                            RouterMapping.getMappingScan(clazz, method);
//                            LOGGER.debug("This class <{}> method <{}> is not a valid API interface class method and will be ignored.", clazz, method.getName());
                        }
                    });
                }
                else {
                    LOGGER.debug("This class <{}> is not a valid API interface class and will be ignored.", clazz);
                }
            });
        }
    }

    public static String getUrl(String parentUrl, String url)
    {
        if (StringUtils.isEmpty(parentUrl)) {
            if (!url.startsWith(URL_PREFIX)) {
                url = URL_PREFIX + url;
            }
        }
        else {
            if (!parentUrl.startsWith(URL_PREFIX)) {
                url = String.join(URL_PREFIX, URL_PREFIX + parentUrl, url);
            }
            else {
                url = String.join(URL_PREFIX, parentUrl, url);
            }
        }
        return url;
    }

    public static void addRouter(String[] parentUrls, Method method, Class clazz, RequestMethod requestMethod, String url)
    {
        if (ObjectUtils.isNotEmpty(parentUrls) && parentUrls.length > 0) {
            Arrays.asList(parentUrls).forEach(parentUrl -> {
                Router router = ObjectBuilder.of(Router::new)
                        .with(Router::setMethod, method)
                        .with(Router::setClazz, clazz)
                        .with(Router::setRequestMethod, requestMethod)
                        .with(Router::setUrl, getUrl(parentUrl, url))
                        .build();
                Routers.setRouter(router);
            });
        }
        else {
            Router router = ObjectBuilder.of(Router::new)
                    .with(Router::setMethod, method)
                    .with(Router::setClazz, clazz)
                    .with(Router::setRequestMethod, requestMethod)
                    .with(Router::setUrl, getUrl(null, url))
                    .build();
            Routers.setRouter(router);
        }
    }
}
