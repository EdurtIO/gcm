package io.edurt.gcm.mysql.hikari;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.mysql.hikari.configuration.HikariConfiguration;
import io.edurt.gcm.mysql.hikari.configuration.HikariConfigurationDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.util.Properties;

public class MySQLHikariProvider
        implements Provider<DataSource>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLHikariProvider.class);

    private final HikariDataSource dataSource = new HikariDataSource();

    public MySQLHikariProvider(Properties configuration)
    {
        LOGGER.info("generate configuration");
        dataSource.setDriverClassName(PropertiesUtils.getStringValue(configuration,
                HikariConfiguration.DRIVER_CLASS_NAME,
                HikariConfigurationDefault.DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(PropertiesUtils.getStringValue(configuration,
                HikariConfiguration.URL,
                HikariConfigurationDefault.URL));
        dataSource.setUsername(PropertiesUtils.getStringValue(configuration,
                HikariConfiguration.USERNAME,
                HikariConfigurationDefault.USERNAME));
        dataSource.setPassword(PropertiesUtils.getStringValue(configuration,
                HikariConfiguration.PASSWORD,
                HikariConfigurationDefault.PASSWORD));
        dataSource.setMinimumIdle(PropertiesUtils.getIntValue(configuration,
                HikariConfiguration.MINIMUM_IDLE,
                HikariConfigurationDefault.MINIMUM_IDLE));
        dataSource.setMaximumPoolSize(PropertiesUtils.getIntValue(configuration,
                HikariConfiguration.MAXIMUM_POOL_SIZE,
                HikariConfigurationDefault.MAXIMUM_POOL_SIZE));
        dataSource.setConnectionTestQuery(PropertiesUtils.getStringValue(configuration,
                HikariConfiguration.CONNECTION_TEST_QUERY,
                HikariConfigurationDefault.CONNECTION_TEST_QUERY));
        dataSource.addDataSourceProperty("cachePrepStmts", PropertiesUtils.getBoolValue(configuration,
                HikariConfiguration.CACHE_PREPSTMTS,
                HikariConfigurationDefault.CACHE_PREPSTMTS));
        dataSource.addDataSourceProperty("prepStmtCacheSize", PropertiesUtils.getIntValue(configuration,
                HikariConfiguration.PREP_STMT_CACHESIZE,
                HikariConfigurationDefault.PREP_STMT_CACHESIZE));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", PropertiesUtils.getIntValue(configuration,
                HikariConfiguration.PREP_STMT_CACHESQLLIMIT,
                HikariConfigurationDefault.PREP_STMT_CACHESQLLIMIT));
        dataSource.addDataSourceProperty("useServerPrepStmts", PropertiesUtils.getBoolValue(configuration,
                HikariConfiguration.USE_SERVER_PREPSTMTS,
                HikariConfigurationDefault.USE_SERVER_PREPSTMTS));
    }

    @Override
    public DataSource get()
    {
        return dataSource;
    }
}
