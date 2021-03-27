package io.edurt.gcm.h2.configuration;

public class H2Configuration
{
    public static final String DRIVER_CLASS_NAME = "jdbc.h2.driverClassName";
    public static final String URL = "jdbc.h2.url";
    public static final String USERNAME = "jdbc.h2.username";
    public static final String PASSWORD = "jdbc.h2.password";
    public static final String MINIMUM_IDLE = "jdbc.h2.minimumIdle";
    public static final String MAXIMUM_POOL_SIZE = "jdbc.h2.maximumPoolSize";
    public static final String CONNECTION_TEST_QUERY = "jdbc.h2.connectionTestQuery";
    public static final String CACHE_PREPSTMTS = "jdbc.h2.cachePrepStmts";
    public static final String PREP_STMT_CACHESIZE = "jdbc.h2.prepStmtCacheSize";
    public static final String PREP_STMT_CACHESQLLIMIT = "jdbc.h2.prepStmtCacheSqlLimit";
    public static final String USE_SERVER_PREPSTMTS = "jdbc.h2.useServerPrepStmts";
    public static final String SCAN_MAPPER_PACKAGE = "jdbc.h2.scan.mapper.package";

    private H2Configuration()
    {}
}
