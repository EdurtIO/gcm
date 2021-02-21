package io.edurt.gcm.postgresql.hikari;

import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.postgresql.hikari.annotation.PostgresSource;
import io.edurt.gcm.postgresql.hikari.configuration.HikariConfiguration;
import io.edurt.gcm.postgresql.hikari.configuration.HikariConfigurationDefault;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.Set;

public class HikariPostgresSQLMultipleModule
        extends PrivateModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HikariPostgresSQLMultipleModule.class);

    private final String configuration;

    public HikariPostgresSQLMultipleModule(String configuration)
    {
        this.configuration = configuration;
    }

    public HikariPostgresSQLMultipleModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "mysql.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("binding hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        String scanPackage = PropertiesUtils.getStringValue(configuration,
                HikariConfiguration.SCAN_MAPPER_PACKAGE,
                HikariConfigurationDefault.SCAN_MAPPER_PACKAGE);
        install(new MyBatisModule()
        {
            @Override
            protected void initialize()
            {
                install(JdbcHelper.MySQL);
                bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
                bindDataSourceProvider(new HikariPostgreSQLProvider(configuration));
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                ResolverUtil<Object> util = new ResolverUtil<>();
                Set<Class<? extends Object>> mappers = util.findImplementations(Object.class, scanPackage).getClasses();
                for (Class<? extends Object> clazz : mappers) {
                    PostgresSource annotation = clazz.getAnnotation(PostgresSource.class);
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
