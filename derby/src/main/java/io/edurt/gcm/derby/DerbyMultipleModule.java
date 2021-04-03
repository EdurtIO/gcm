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
package io.edurt.gcm.derby;

import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.derby.annotation.DerbySource;
import io.edurt.gcm.derby.configuration.DerbyConfiguration;
import io.edurt.gcm.derby.configuration.DerbyConfigurationDefault;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.Set;

public class DerbyMultipleModule
        extends PrivateModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DerbyMultipleModule.class);

    private final String configuration;

    public DerbyMultipleModule(String configuration)
    {
        this.configuration = configuration;
    }

    public DerbyMultipleModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "derby.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("binding derby by hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding derby by hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        String scanPackage = PropertiesUtils.getStringValue(configuration,
                DerbyConfiguration.SCAN_MAPPER_PACKAGE,
                DerbyConfigurationDefault.SCAN_MAPPER_PACKAGE);
        install(new MyBatisModule()
        {
            @Override
            protected void initialize()
            {
                bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
                bindDataSourceProvider(new DerbyProvider(configuration));
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                ResolverUtil<Object> util = new ResolverUtil<>();
                Set<Class<? extends Object>> mappers = util.findImplementations(Object.class, scanPackage).getClasses();
                for (Class<? extends Object> clazz : mappers) {
                    DerbySource annotation = clazz.getAnnotation(DerbySource.class);
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
