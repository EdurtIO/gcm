package io.edurt.gcm.sqllite.configuration;

public class SQLLiteConfiguration
{
    public static final String DRIVER_CLASS_NAME = "jdbc.sqllite.driverClassName";
    public static final String URL = "jdbc.sqllite.url";
    public static final String USERNAME = "jdbc.sqllite.username";
    public static final String PASSWORD = "jdbc.sqllite.password";
    public static final String MINIMUM_IDLE = "jdbc.sqllite.minimumIdle";
    public static final String MAXIMUM_POOL_SIZE = "jdbc.sqllite.maximumPoolSize";
    public static final String CONNECTION_TEST_QUERY = "jdbc.sqllite.connectionTestQuery";
    public static final String CACHE_PREPSTMTS = "jdbc.sqllite.cachePrepStmts";
    public static final String PREP_STMT_CACHESIZE = "jdbc.sqllite.prepStmtCacheSize";
    public static final String PREP_STMT_CACHESQLLIMIT = "jdbc.sqllite.prepStmtCacheSqlLimit";
    public static final String USE_SERVER_PREPSTMTS = "jdbc.sqllite.useServerPrepStmts";
    public static final String SCAN_MAPPER_PACKAGE = "jdbc.sqllite.scan.mapper.package";

    private SQLLiteConfiguration()
    {}
}
