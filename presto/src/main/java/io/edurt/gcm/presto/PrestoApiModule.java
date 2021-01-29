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
package io.edurt.gcm.presto;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.presto.client.api.PrestoApiClient;
import io.edurt.gcm.presto.configuration.PrestoConfiguration;
import io.edurt.gcm.presto.configuration.PrestoConfigurationDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class PrestoApiModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PrestoApiModule.class);

    private final String configuration;

    public PrestoApiModule(String configuration)
    {
        this.configuration = configuration;
    }

    public PrestoApiModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "presto.properties");
    }

    private void bindConfiguration(Properties configuration)
    {
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.URL)).to(PropertiesUtils.getStringValue(configuration,
                PrestoConfiguration.URL,
                PrestoConfigurationDefault.URL));
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.URL_BACKUP)).to(PropertiesUtils.getStringValue(configuration,
                PrestoConfiguration.URL_BACKUP,
                PrestoConfigurationDefault.URL_BACKUP));
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.USERNAME)).to(PropertiesUtils.getStringValue(configuration,
                PrestoConfiguration.USERNAME,
                PrestoConfigurationDefault.USERNAME));
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.PASSWORD)).to(PropertiesUtils.getStringValue(configuration,
                PrestoConfiguration.PASSWORD,
                PrestoConfigurationDefault.PASSWORD));
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.CATALOG)).to(PropertiesUtils.getStringValue(configuration,
                PrestoConfiguration.CATALOG,
                PrestoConfigurationDefault.CATALOG));
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.SCHEMA)).to(PropertiesUtils.getStringValue(configuration,
                PrestoConfiguration.SCHEMA,
                PrestoConfigurationDefault.SCHEMA));
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.SOURCE)).to(PropertiesUtils.getStringValue(configuration,
                PrestoConfiguration.SOURCE,
                PrestoConfigurationDefault.SOURCE));
        binder().bindConstant().annotatedWith(Names.named(PrestoConfiguration.RETRY)).to(PropertiesUtils.getIntValue(configuration,
                PrestoConfiguration.RETRY,
                PrestoConfigurationDefault.RETRY));
    }

    @Override
    protected void configure()
    {
        LOGGER.info("binding presto datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        bindConfiguration(configuration);
        requestStaticInjection(Preconditions.class);
        LOGGER.info("binding presto datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        bind(PrestoApiClient.class).in(Scopes.SINGLETON);
    }
}
