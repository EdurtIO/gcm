package io.edurt.gcm.derby;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.derby.configuration.DerbyConfiguration;
import io.edurt.gcm.derby.configuration.DerbyConfigurationDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.util.Properties;

public class DerbyProvider
        implements Provider<DataSource>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DerbyProvider.class);

    private final HikariDataSource dataSource = new HikariDataSource();

    public DerbyProvider(Properties configuration)
    {
        LOGGER.info("generate configuration");
        dataSource.setDriverClassName(PropertiesUtils.getStringValue(configuration,
                DerbyConfiguration.DRIVER_CLASS_NAME,
                DerbyConfigurationDefault.DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(PropertiesUtils.getStringValue(configuration,
                DerbyConfiguration.URL,
                DerbyConfigurationDefault.URL));
        dataSource.setUsername(PropertiesUtils.getStringValue(configuration,
                DerbyConfiguration.USERNAME,
                DerbyConfigurationDefault.USERNAME));
        dataSource.setPassword(PropertiesUtils.getStringValue(configuration,
                DerbyConfiguration.PASSWORD,
                DerbyConfigurationDefault.PASSWORD));
        dataSource.setMinimumIdle(PropertiesUtils.getIntValue(configuration,
                DerbyConfiguration.MINIMUM_IDLE,
                DerbyConfigurationDefault.MINIMUM_IDLE));
        dataSource.setMaximumPoolSize(PropertiesUtils.getIntValue(configuration,
                DerbyConfiguration.MAXIMUM_POOL_SIZE,
                DerbyConfigurationDefault.MAXIMUM_POOL_SIZE));
        dataSource.setConnectionTestQuery(PropertiesUtils.getStringValue(configuration,
                DerbyConfiguration.CONNECTION_TEST_QUERY,
                DerbyConfigurationDefault.CONNECTION_TEST_QUERY));
        dataSource.addDataSourceProperty("cachePrepStmts", PropertiesUtils.getBoolValue(configuration,
                DerbyConfiguration.CACHE_PREPSTMTS,
                DerbyConfigurationDefault.CACHE_PREPSTMTS));
        dataSource.addDataSourceProperty("prepStmtCacheSize", PropertiesUtils.getIntValue(configuration,
                DerbyConfiguration.PREP_STMT_CACHESIZE,
                DerbyConfigurationDefault.PREP_STMT_CACHESIZE));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", PropertiesUtils.getIntValue(configuration,
                DerbyConfiguration.PREP_STMT_CACHESQLLIMIT,
                DerbyConfigurationDefault.PREP_STMT_CACHESQLLIMIT));
        dataSource.addDataSourceProperty("useServerPrepStmts", PropertiesUtils.getBoolValue(configuration,
                DerbyConfiguration.USE_SERVER_PREPSTMTS,
                DerbyConfigurationDefault.USE_SERVER_PREPSTMTS));
    }

    @Override
    public DataSource get()
    {
        return dataSource;
    }
}
