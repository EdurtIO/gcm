package io.edurt.gcm.hive;

import com.google.inject.PrivateModule;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.hive.annotation.HiveSource;
import io.edurt.gcm.hive.configuration.HiveConfiguration;
import io.edurt.gcm.hive.configuration.HiveConfigurationDefault;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.Set;

public class HiveMultipleModule
        extends PrivateModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HiveMultipleModule.class);

    private final String configuration;

    public HiveMultipleModule(String configuration)
    {
        this.configuration = configuration;
    }

    public HiveMultipleModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "postgresql.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.info("binding hikari datasource configuration information is started.");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("binding hikari datasource configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        String scanPackage = PropertiesUtils.getStringValue(configuration,
                HiveConfiguration.SCAN_MAPPER_PACKAGE,
                HiveConfigurationDefault.SCAN_MAPPER_PACKAGE);
        install(new MyBatisModule()
        {
            @Override
            protected void initialize()
            {
                install(JdbcHelper.MySQL);
                bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("Prod");
                bindDataSourceProvider(new HiveProvider(configuration));
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                ResolverUtil<Object> util = new ResolverUtil<>();
                Set<Class<? extends Object>> mappers = util.findImplementations(Object.class, scanPackage).getClasses();
                for (Class<? extends Object> clazz : mappers) {
                    HiveSource annotation = clazz.getAnnotation(HiveSource.class);
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
