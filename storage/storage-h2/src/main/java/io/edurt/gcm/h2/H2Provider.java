package io.edurt.gcm.h2;

import com.google.inject.Provider;
import com.zaxxer.hikari.HikariDataSource;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.h2.configuration.H2Configuration;
import io.edurt.gcm.h2.configuration.H2ConfigurationDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.util.Properties;

public class H2Provider
        implements Provider<DataSource>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(H2Provider.class);

    private final HikariDataSource dataSource = new HikariDataSource();

    public H2Provider(Properties configuration)
    {
        LOGGER.info("generate configuration");
        dataSource.setDriverClassName(PropertiesUtils.getStringValue(configuration,
                H2Configuration.DRIVER_CLASS_NAME,
                H2ConfigurationDefault.DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(PropertiesUtils.getStringValue(configuration,
                H2Configuration.URL,
                H2ConfigurationDefault.URL));
        dataSource.setUsername(PropertiesUtils.getStringValue(configuration,
                H2Configuration.USERNAME,
                H2ConfigurationDefault.USERNAME));
        dataSource.setPassword(PropertiesUtils.getStringValue(configuration,
                H2Configuration.PASSWORD,
                H2ConfigurationDefault.PASSWORD));
        dataSource.setMinimumIdle(PropertiesUtils.getIntValue(configuration,
                H2Configuration.MINIMUM_IDLE,
                H2ConfigurationDefault.MINIMUM_IDLE));
        dataSource.setMaximumPoolSize(PropertiesUtils.getIntValue(configuration,
                H2Configuration.MAXIMUM_POOL_SIZE,
                H2ConfigurationDefault.MAXIMUM_POOL_SIZE));
        dataSource.setConnectionTestQuery(PropertiesUtils.getStringValue(configuration,
                H2Configuration.CONNECTION_TEST_QUERY,
                H2ConfigurationDefault.CONNECTION_TEST_QUERY));
        dataSource.addDataSourceProperty("cachePrepStmts", PropertiesUtils.getBoolValue(configuration,
                H2Configuration.CACHE_PREPSTMTS,
                H2ConfigurationDefault.CACHE_PREPSTMTS));
        dataSource.addDataSourceProperty("prepStmtCacheSize", PropertiesUtils.getIntValue(configuration,
                H2Configuration.PREP_STMT_CACHESIZE,
                H2ConfigurationDefault.PREP_STMT_CACHESIZE));
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", PropertiesUtils.getIntValue(configuration,
                H2Configuration.PREP_STMT_CACHESQLLIMIT,
                H2ConfigurationDefault.PREP_STMT_CACHESQLLIMIT));
        dataSource.addDataSourceProperty("useServerPrepStmts", PropertiesUtils.getBoolValue(configuration,
                H2Configuration.USE_SERVER_PREPSTMTS,
                H2ConfigurationDefault.USE_SERVER_PREPSTMTS));
    }

    @Override
    public DataSource get()
    {
        return dataSource;
    }
}
