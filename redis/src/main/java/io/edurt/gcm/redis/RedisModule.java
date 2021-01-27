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
package io.edurt.gcm.redis;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.redis.client.RedisHashClient;
import io.edurt.gcm.redis.client.RedisListClient;
import io.edurt.gcm.redis.provider.RedisHashProvider;
import io.edurt.gcm.redis.provider.RedisListProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class RedisModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisModule.class);

    private final String configuration;

    public RedisModule(String configuration)
    {
        this.configuration = configuration;
    }

    public RedisModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "redis.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("binding redis datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding redis datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        bind(RedisListClient.class).toProvider(new RedisListProvider(configuration)).in(Scopes.SINGLETON);
        bind(RedisHashClient.class).toProvider(new RedisHashProvider(configuration)).in(Scopes.SINGLETON);
    }
}
