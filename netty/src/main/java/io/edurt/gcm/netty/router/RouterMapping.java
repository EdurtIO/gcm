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

import io.edurt.gcm.common.jdk.ObjectBuilder;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.netty.annotation.DeleteMapping;
import io.edurt.gcm.netty.annotation.GetMapping;
import io.edurt.gcm.netty.annotation.PostMapping;
import io.edurt.gcm.netty.annotation.PutMapping;
import io.edurt.gcm.netty.type.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

public class RouterMapping
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterMapping.class);

    private RouterMapping()
    {}

    public static void getMappingScan(Class clazz, Method method)
    {
        String[] mappingValues = new String[0];
        RequestMethod requestMethod = null;
        if (method.isAnnotationPresent(GetMapping.class)) {
            mappingValues = method.getAnnotation(GetMapping.class).value();
            requestMethod = RequestMethod.GET;
        }
        if (method.isAnnotationPresent(PostMapping.class)) {
            mappingValues = method.getAnnotation(PostMapping.class).value();
            requestMethod = RequestMethod.POST;
        }
        if (method.isAnnotationPresent(PutMapping.class)) {
            mappingValues = method.getAnnotation(PutMapping.class).value();
            requestMethod = RequestMethod.PUT;
        }
        if (method.isAnnotationPresent(DeleteMapping.class)) {
            mappingValues = method.getAnnotation(DeleteMapping.class).value();
            requestMethod = RequestMethod.DELETE;
        }
        LOGGER.debug("Current scan mapping {} method {}", clazz.getSimpleName(), method.getName());
        if (ObjectUtils.isEmpty(mappingValues) || mappingValues.length < 1) {
            LOGGER.debug("The <{}> method in the current class <{}> does not specify any access method or path, skipping this scan",
                    clazz.getCanonicalName(),
                    method.getName());
            return;
        }
        RequestMethod finalRequestMethod = requestMethod;
        Arrays.stream(mappingValues)
                .map(url -> RouterScan.getUrl(url))
                .forEach(value -> {
                    Router router = ObjectBuilder.of(Router::new)
                            .with(Router::setMethods, new HashSet<RequestMethod>()
                            {{
                                add(finalRequestMethod);
                            }})
                            .with(Router::setMethod, method)
                            .with(Router::setClazz, clazz)
                            .with(Router::setUrls, new HashSet<String>()
                            {{
                                add(value);
                            }})
                            .build();
                    Routers.setRouter(value, router);
                });
    }
}
