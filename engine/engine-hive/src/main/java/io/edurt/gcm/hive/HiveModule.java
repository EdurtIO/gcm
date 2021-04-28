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
package io.edurt.gcm.hive;

import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.hive.configuration.HiveConfiguration;
import io.edurt.gcm.hive.configuration.HiveConfigurationDefault;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class HiveModule
        extends MyBatisModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HiveModule.class);

    private final String configuration;

    public HiveModule(String configuration)
    {
        this.configuration = configuration;
    }

    public HiveModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "hive.properties");
    }

    @Override
    protected void initialize()
    {
        LOGGER.info("binding hive by hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding clickhouse by hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
        bindDataSourceProvider(new HiveProvider(configuration));
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClasses(PropertiesUtils.getStringValue(configuration,
                HiveConfiguration.SCAN_MAPPER_PACKAGE,
                HiveConfigurationDefault.SCAN_MAPPER_PACKAGE));
    }
}
