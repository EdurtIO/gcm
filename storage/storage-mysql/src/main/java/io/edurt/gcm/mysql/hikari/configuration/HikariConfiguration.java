package io.edurt.gcm.mysql.hikari.configuration;

public class HikariConfiguration
{
    public static final String DRIVER_CLASS_NAME = "jdbc.mysql.driverClassName";
    public static final String URL = "jdbc.mysql.url";
    public static final String USERNAME = "jdbc.mysql.username";
    public static final String PASSWORD = "jdbc.mysql.password";
    public static final String MINIMUM_IDLE = "jdbc.mysql.minimumIdle";
    public static final String MAXIMUM_POOL_SIZE = "jdbc.mysql.maximumPoolSize";
    public static final String CONNECTION_TEST_QUERY = "jdbc.mysql.connectionTestQuery";
    public static final String CACHE_PREPSTMTS = "jdbc.mysql.cachePrepStmts";
    public static final String PREP_STMT_CACHESIZE = "jdbc.mysql.prepStmtCacheSize";
    public static final String PREP_STMT_CACHESQLLIMIT = "jdbc.mysql.prepStmtCacheSqlLimit";
    public static final String USE_SERVER_PREPSTMTS = "jdbc.mysql.useServerPrepStmts";
    public static final String SCAN_MAPPER_PACKAGE = "jdbc.mysql.scan.mapper.package";

    private HikariConfiguration()
    {}
}
