package io.edurt.gcm.clickhouse;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import io.edurt.gcm.clickhouse.configuration.ClickHouseConfiguration;
import io.edurt.gcm.clickhouse.configuration.ClickHouseConfigurationDefault;
import io.edurt.gcm.common.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.util.Properties;

public class ClickHouseProvider
        implements Provider<DataSource>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickHouseProvider.class);

    private final HikariDataSource dataSource = new HikariDataSource();

    public ClickHouseProvider(Properties configuration)
    {
        LOGGER.info("generate configuration");
        dataSource.setDriverClassName(PropertiesUtils.getStringValue(configuration,
                ClickHouseConfiguration.DRIVER_CLASS_NAME,
                ClickHouseConfigurationDefault.DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(PropertiesUtils.getStringValue(configuration,
                ClickHouseConfiguration.URL,
                ClickHouseConfigurationDefault.URL));
        dataSource.setUsername(PropertiesUtils.getStringValue(configuration,
                ClickHouseConfiguration.USERNAME,
                ClickHouseConfigurationDefault.USERNAME));
        dataSource.setPassword(PropertiesUtils.getStringValue(configuration,
                ClickHouseConfiguration.PASSWORD,
                ClickHouseConfigurationDefault.PASSWORD));
        dataSource.setMinimumIdle(PropertiesUtils.getIntValue(configuration,
                ClickHouseConfiguration.MINIMUM_IDLE,
                ClickHouseConfigurationDefault.MINIMUM_IDLE));
        dataSource.setMaximumPoolSize(PropertiesUtils.getIntValue(configuration,
                ClickHouseConfiguration.MAXIMUM_POOL_SIZE,
                ClickHouseConfigurationDefault.MAXIMUM_POOL_SIZE));
        dataSource.setConnectionTestQuery(PropertiesUtils.getStringValue(configuration,
                ClickHouseConfiguration.CONNECTION_TEST_QUERY,
                ClickHouseConfigurationDefault.CONNECTION_TEST_QUERY));
        dataSource.addDataSourceProperty("cachePrepStmts", PropertiesUtils.getBoolValue(configuration,
                ClickHouseConfiguration.CACHE_PREPSTMTS,
                ClickHouseConfigurationDefault.CACHE_PREPSTMTS));
        dataSource.addDataSourceProperty("prepStmtCacheSize", PropertiesUtils.getIntValue(configuration,
                ClickHouseConfiguration.PREP_STMT_CACHESIZE,
                ClickHouseConfigurationDefault.PREP_STMT_CACHESIZE));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", PropertiesUtils.getIntValue(configuration,
                ClickHouseConfiguration.PREP_STMT_CACHESQLLIMIT,
                ClickHouseConfigurationDefault.PREP_STMT_CACHESQLLIMIT));
        dataSource.addDataSourceProperty("useServerPrepStmts", PropertiesUtils.getBoolValue(configuration,
                ClickHouseConfiguration.USE_SERVER_PREPSTMTS,
                ClickHouseConfigurationDefault.USE_SERVER_PREPSTMTS));
    }

    @Override
    public DataSource get()
    {
        return dataSource;
    }
}
