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
package io.edurt.gcm.sqllite;

import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.sqllite.annotation.SQLLiteSource;
import io.edurt.gcm.sqllite.configuration.SQLLiteConfiguration;
import io.edurt.gcm.sqllite.configuration.SQLLiteConfigurationDefault;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.Set;

public class SQLLiteMultipleModule
        extends PrivateModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLLiteMultipleModule.class);

    private final String configuration;

    public SQLLiteMultipleModule(String configuration)
    {
        this.configuration = configuration;
    }

    public SQLLiteMultipleModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "sqllite.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("binding sqllite by hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding sqllite by hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        String scanPackage = PropertiesUtils.getStringValue(configuration,
                SQLLiteConfiguration.SCAN_MAPPER_PACKAGE,
                SQLLiteConfigurationDefault.SCAN_MAPPER_PACKAGE);
        install(new MyBatisModule()
        {
            @Override
            protected void initialize()
            {
                bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
                bindDataSourceProvider(new SQLLiteProvider(configuration));
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                ResolverUtil<Object> util = new ResolverUtil<>();
                Set<Class<? extends Object>> mappers = util.findImplementations(Object.class, scanPackage).getClasses();
                for (Class<? extends Object> clazz : mappers) {
                    SQLLiteSource annotation = clazz.getAnnotation(SQLLiteSource.class);
                    if (ObjectUtils.isNotEmpty(annotation)) {
                        addMapperClass(clazz);
                        expose(clazz);
                    }
                }
                addSimpleAliases(scanPackage);
            }
        });
    }
}
