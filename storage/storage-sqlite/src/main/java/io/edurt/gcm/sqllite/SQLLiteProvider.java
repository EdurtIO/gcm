package io.edurt.gcm.sqllite;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.sqllite.configuration.SQLLiteConfiguration;
import io.edurt.gcm.sqllite.configuration.SQLLiteConfigurationDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.util.Properties;

public class SQLLiteProvider
        implements Provider<DataSource>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLLiteProvider.class);

    private final HikariDataSource dataSource = new HikariDataSource();

    public SQLLiteProvider(Properties configuration)
    {
        LOGGER.info("generate configuration");
        dataSource.setDriverClassName(PropertiesUtils.getStringValue(configuration,
                SQLLiteConfiguration.DRIVER_CLASS_NAME,
                SQLLiteConfigurationDefault.DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(PropertiesUtils.getStringValue(configuration,
                SQLLiteConfiguration.URL,
                SQLLiteConfigurationDefault.URL));
        dataSource.setUsername(PropertiesUtils.getStringValue(configuration,
                SQLLiteConfiguration.USERNAME,
                SQLLiteConfigurationDefault.USERNAME));
        dataSource.setPassword(PropertiesUtils.getStringValue(configuration,
                SQLLiteConfiguration.PASSWORD,
                SQLLiteConfigurationDefault.PASSWORD));
        dataSource.setMinimumIdle(PropertiesUtils.getIntValue(configuration,
                SQLLiteConfiguration.MINIMUM_IDLE,
                SQLLiteConfigurationDefault.MINIMUM_IDLE));
        dataSource.setMaximumPoolSize(PropertiesUtils.getIntValue(configuration,
                SQLLiteConfiguration.MAXIMUM_POOL_SIZE,
                SQLLiteConfigurationDefault.MAXIMUM_POOL_SIZE));
        dataSource.setConnectionTestQuery(PropertiesUtils.getStringValue(configuration,
                SQLLiteConfiguration.CONNECTION_TEST_QUERY,
                SQLLiteConfigurationDefault.CONNECTION_TEST_QUERY));
        dataSource.addDataSourceProperty("cachePrepStmts", PropertiesUtils.getBoolValue(configuration,
                SQLLiteConfiguration.CACHE_PREPSTMTS,
                SQLLiteConfigurationDefault.CACHE_PREPSTMTS));
        dataSource.addDataSourceProperty("prepStmtCacheSize", PropertiesUtils.getIntValue(configuration,
                SQLLiteConfiguration.PREP_STMT_CACHESIZE,
                SQLLiteConfigurationDefault.PREP_STMT_CACHESIZE));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", PropertiesUtils.getIntValue(configuration,
                SQLLiteConfiguration.PREP_STMT_CACHESQLLIMIT,
                SQLLiteConfigurationDefault.PREP_STMT_CACHESQLLIMIT));
        dataSource.addDataSourceProperty("useServerPrepStmts", PropertiesUtils.getBoolValue(configuration,
                SQLLiteConfiguration.USE_SERVER_PREPSTMTS,
                SQLLiteConfigurationDefault.USE_SERVER_PREPSTMTS));
    }

    @Override
    public DataSource get()
    {
        return dataSource;
    }
}
