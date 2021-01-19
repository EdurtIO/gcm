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
package io.edurt.gcm.clickhouse;

import com.google.inject.name.Names;
import io.edurt.gcm.clickhouse.configuration.ClickHouseConfiguration;
import io.edurt.gcm.clickhouse.configuration.ClickHouseConfigurationDefault;
import io.edurt.gcm.common.utils.PropertiesUtils;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class ClickHouseModule
        extends MyBatisModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickHouseModule.class);

    private final String configuration;

    public ClickHouseModule(String configuration)
    {
        this.configuration = configuration;
    }

    public ClickHouseModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "clickhouse.properties");
    }

    @Override
    protected void initialize()
    {
        LOGGER.info("binding clickhouse by hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding clickhouse by hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
        bindDataSourceProvider(new ClickHouseProvider(configuration));
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClasses(PropertiesUtils.getStringValue(configuration,
                ClickHouseConfiguration.SCAN_MAPPER_PACKAGE,
                ClickHouseConfigurationDefault.SCAN_MAPPER_PACKAGE));
    }
}
