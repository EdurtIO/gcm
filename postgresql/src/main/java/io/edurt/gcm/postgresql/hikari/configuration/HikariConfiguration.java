package io.edurt.gcm.postgresql.hikari.configuration;

public class HikariConfiguration
{
    public static final String DRIVER_CLASS_NAME = "jdbc.postgresql.driverClassName";
    public static final String URL = "jdbc.postgresql.url";
    public static final String USERNAME = "jdbc.postgresql.username";
    public static final String PASSWORD = "jdbc.postgresql.password";
    public static final String MINIMUM_IDLE = "jdbc.postgresql.minimumIdle";
    public static final String MAXIMUM_POOL_SIZE = "jdbc.postgresql.maximumPoolSize";
    public static final String CONNECTION_TEST_QUERY = "jdbc.postgresql.connectionTestQuery";
    public static final String CACHE_PREPSTMTS = "jdbc.postgresql.cachePrepStmts";
    public static final String PREP_STMT_CACHESIZE = "jdbc.postgresql.prepStmtCacheSize";
    public static final String PREP_STMT_CACHESQLLIMIT = "jdbc.postgresql.prepStmtCacheSqlLimit";
    public static final String USE_SERVER_PREPSTMTS = "jdbc.postgresql.useServerPrepStmts";
    public static final String SCAN_MAPPER_PACKAGE = "jdbc.postgresql.scan.mapper.package";

    private HikariConfiguration()
    {}
}
