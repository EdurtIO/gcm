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
package io.edurt.gcm.h2;

import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.h2.configuration.H2Configuration;
import io.edurt.gcm.h2.configuration.H2ConfigurationDefault;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class H2Module
        extends MyBatisModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(H2Module.class);

    private final String configuration;

    public H2Module(String configuration)
    {
        this.configuration = configuration;
    }

    public H2Module()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "h2.properties");
    }

    @Override
    protected void initialize()
    {
        LOGGER.info("binding h2 by hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding h2 by hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
        bindDataSourceProvider(new H2Provider(configuration));
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClasses(PropertiesUtils.getStringValue(configuration,
                H2Configuration.SCAN_MAPPER_PACKAGE,
                H2ConfigurationDefault.SCAN_MAPPER_PACKAGE));
    }
}
