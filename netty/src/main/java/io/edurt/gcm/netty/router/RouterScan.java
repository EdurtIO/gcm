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
import io.edurt.gcm.netty.annotation.RequestMapping;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.dispatcher.DispatchRules;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class RouterScan
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterScan.class);

    private RouterScan()
    {}

    public static Map<String, Router> getRouters(String scanPackage)
    {
        LOGGER.info("Scan router from {}", scanPackage);
        Set<Class<?>> classes = Classs.scanClassInPackage(scanPackage);
        Map<String, Router> routers = new ConcurrentHashMap<>();
        if (ObjectUtils.isEmpty(classes) || classes.size() < 1) {
            LOGGER.warn("Scan router is empty from {}", scanPackage);
        }
        else {
            classes.forEach(clazz -> {
                if (clazz.isAnnotationPresent(RestController.class)) {
                    Arrays.stream(clazz.getMethods()).forEach(method -> {
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
                            Arrays.stream(mapping.value()).forEach(value -> {
                                Arrays.stream(mapping.method()).forEach(requestMethod ->
                                        DispatchRules.ROUES.put(format("%s %s", requestMethod, value), format("%s.%s", clazz.getSimpleName(), method.getName()))
                                );
                            });
                        }
                        else {
                            LOGGER.debug("This class <{}> method <{}> is not a valid API interface class method and will be ignored.", clazz, method.getName());
                        }
                    });
                }
                else {
                    LOGGER.debug("This class <{}> is not a valid API interface class and will be ignored.", clazz);
                }
            });
        }
        return routers;
    }
}
