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

import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import io.edurt.gcm.clickhouse.configuration.ClickHouseConfiguration;
import io.edurt.gcm.clickhouse.configuration.ClickHouseConfigurationDefault;
import io.edurt.gcm.common.utils.PropertiesUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class ClickHouseMultipleModule
        extends PrivateModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickHouseMultipleModule.class);

    private final String configuration;

    public ClickHouseMultipleModule(String configuration)
    {
        this.configuration = configuration;
    }

    public ClickHouseMultipleModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "clickhouse.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("binding clickhouse by hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding clickhouse by hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        String scanPackage = PropertiesUtils.getStringValue(configuration,
                ClickHouseConfiguration.SCAN_MAPPER_PACKAGE,
                ClickHouseConfigurationDefault.SCAN_MAPPER_PACKAGE);
        install(new MyBatisModule()
        {
            @Override
            protected void initialize()
            {
                bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
                bindDataSourceProvider(new ClickHouseProvider(configuration));
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                addMapperClasses(scanPackage);
            }
        });
        // Use the tool class of mybatis to obtain all classes under Dao package path and expose them to Guice global environment
        new ResolverUtil<>()
                .find(new ResolverUtil.IsA(Object.class), scanPackage)
                .getClasses()
                .forEach(ClickHouseMultipleModule.this::expose);
    }
}
