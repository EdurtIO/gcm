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
package io.edurt.gcm.kafka;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.kafka.client.KafkaProduceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class KafkaModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaModule.class);

    private final String configuration;

    public KafkaModule(String configuration)
    {
        this.configuration = configuration;
    }

    public KafkaModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "kafka.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("Binding kafka configuration information is started.");
        LOGGER.info("Load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("Binding kafka configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        KafkaProvider provider = new KafkaProvider(configuration);
        bind(KafkaProduceClient.class).toProvider(provider).in(Scopes.SINGLETON);
    }
}
