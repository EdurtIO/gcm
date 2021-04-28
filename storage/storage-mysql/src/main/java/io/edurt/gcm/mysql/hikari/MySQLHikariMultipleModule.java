package io.edurt.gcm.mysql.hikari;

import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.mysql.hikari.configuration.HikariConfiguration;
import io.edurt.gcm.mysql.hikari.configuration.HikariConfigurationDefault;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class MySQLHikariMultipleModule
        extends PrivateModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLHikariMultipleModule.class);

    private final String configuration;

    public MySQLHikariMultipleModule(String configuration)
    {
        this.configuration = configuration;
    }

    public MySQLHikariMultipleModule()
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
                bindDataSourceProvider(new MySQLHikariProvider(configuration));
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                addMapperClasses(scanPackage);
            }
        });
        // Use the tool class of mybatis to obtain all classes under Dao package path and expose them to Guice global environment
        new ResolverUtil<>()
                .find(new ResolverUtil.IsA(Object.class), scanPackage)
                .getClasses()
                .forEach(MySQLHikariMultipleModule.this::expose);
    }
}
