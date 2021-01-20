package io.edurt.gcm.postgresql.hikari;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.postgresql.hikari.configuration.HikariConfiguration;
import io.edurt.gcm.postgresql.hikari.configuration.HikariConfigurationDefault;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class HikariDataSourceModule
        extends MyBatisModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractModule.class);

    private final String configuration;

    public HikariDataSourceModule(String configuration)
    {
        this.configuration = configuration;
    }

    public HikariDataSourceModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "postgresql.properties");
    }

    @Override
    protected void initialize()
    {
        LOGGER.info("binding postgresql by hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        install(JdbcHelper.PostgreSQL);
        bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
        bindDataSourceProvider(new HikariDataSourceProvider(configuration));
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClasses(PropertiesUtils.getStringValue(configuration,
                HikariConfiguration.SCAN_MAPPER_PACKAGE,
                HikariConfigurationDefault.SCAN_MAPPER_PACKAGE));
    }
}
