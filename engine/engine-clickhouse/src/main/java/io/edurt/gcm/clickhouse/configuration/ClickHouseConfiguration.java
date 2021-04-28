package io.edurt.gcm.clickhouse.configuration;

public class ClickHouseConfiguration
{
    public static final String DRIVER_CLASS_NAME = "jdbc.clickhouse.driverClassName";
    public static final String URL = "jdbc.clickhouse.url";
    public static final String USERNAME = "jdbc.clickhouse.username";
    public static final String PASSWORD = "jdbc.clickhouse.password";
    public static final String MINIMUM_IDLE = "jdbc.clickhouse.minimumIdle";
    public static final String MAXIMUM_POOL_SIZE = "jdbc.clickhouse.maximumPoolSize";
    public static final String CONNECTION_TEST_QUERY = "jdbc.clickhouse.connectionTestQuery";
    public static final String CACHE_PREPSTMTS = "jdbc.clickhouse.cachePrepStmts";
    public static final String PREP_STMT_CACHESIZE = "jdbc.clickhouse.prepStmtCacheSize";
    public static final String PREP_STMT_CACHESQLLIMIT = "jdbc.clickhouse.prepStmtCacheSqlLimit";
    public static final String USE_SERVER_PREPSTMTS = "jdbc.clickhouse.useServerPrepStmts";
    public static final String SCAN_MAPPER_PACKAGE = "jdbc.clickhouse.scan.mapper.package";

    private ClickHouseConfiguration()
    {}
}
