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
package io.edurt.gcm.elasticsearch;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.edurt.gcm.common.file.Paths;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.elasticsearch.client.ElasticsearchClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ElasticsearchModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchModule.class);

    private final String configuration;

    public ElasticsearchModule(String configuration)
    {
        this.configuration = configuration;
    }

    public ElasticsearchModule()
    {
        this.configuration = Paths.getProjectConfigurationHome("conf", "catalog", "elasticsearch.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("Binding elasticsearch datasource configuration information is started.");
        LOGGER.info("Load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("Binding elasticsearch datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        ElasticsearchProvider provider = new ElasticsearchProvider(configuration);
        bind(ElasticsearchClient.class).toProvider(provider).in(Scopes.SINGLETON);
    }
}
