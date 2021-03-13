package io.edurt.gcm.hive;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.hive.configuration.HiveConfiguration;
import io.edurt.gcm.hive.configuration.HiveConfigurationDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.util.Properties;

public class HiveProvider
        implements Provider<DataSource>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HiveProvider.class);

    private final HikariDataSource dataSource = new HikariDataSource();

    public HiveProvider(Properties configuration)
    {
        LOGGER.info("generate configuration");
        dataSource.setDriverClassName(PropertiesUtils.getStringValue(configuration,
                HiveConfiguration.DRIVER_CLASS_NAME,
                HiveConfigurationDefault.DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(PropertiesUtils.getStringValue(configuration,
                HiveConfiguration.URL,
                HiveConfigurationDefault.URL));
        dataSource.setUsername(PropertiesUtils.getStringValue(configuration,
                HiveConfiguration.USERNAME,
                HiveConfigurationDefault.USERNAME));
        dataSource.setPassword(PropertiesUtils.getStringValue(configuration,
                HiveConfiguration.PASSWORD,
                HiveConfigurationDefault.PASSWORD));
        dataSource.setMinimumIdle(PropertiesUtils.getIntValue(configuration,
                HiveConfiguration.MINIMUM_IDLE,
                HiveConfigurationDefault.MINIMUM_IDLE));
        dataSource.setMaximumPoolSize(PropertiesUtils.getIntValue(configuration,
                HiveConfiguration.MAXIMUM_POOL_SIZE,
                HiveConfigurationDefault.MAXIMUM_POOL_SIZE));
        dataSource.setConnectionTestQuery(PropertiesUtils.getStringValue(configuration,
                HiveConfiguration.CONNECTION_TEST_QUERY,
                HiveConfigurationDefault.CONNECTION_TEST_QUERY));
        dataSource.addDataSourceProperty("cachePrepStmts", PropertiesUtils.getBoolValue(configuration,
                HiveConfiguration.CACHE_PREPSTMTS,
                HiveConfigurationDefault.CACHE_PREPSTMTS));
        dataSource.addDataSourceProperty("prepStmtCacheSize", PropertiesUtils.getIntValue(configuration,
                HiveConfiguration.PREP_STMT_CACHESIZE,
                HiveConfigurationDefault.PREP_STMT_CACHESIZE));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", PropertiesUtils.getIntValue(configuration,
                HiveConfiguration.PREP_STMT_CACHESQLLIMIT,
                HiveConfigurationDefault.PREP_STMT_CACHESQLLIMIT));
        dataSource.addDataSourceProperty("useServerPrepStmts", PropertiesUtils.getBoolValue(configuration,
                HiveConfiguration.USE_SERVER_PREPSTMTS,
                HiveConfigurationDefault.USE_SERVER_PREPSTMTS));
    }

    @Override
    public DataSource get()
    {
        return dataSource;
    }
}
