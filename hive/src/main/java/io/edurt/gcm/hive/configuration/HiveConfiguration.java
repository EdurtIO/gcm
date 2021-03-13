package io.edurt.gcm.hive.configuration;

public class HiveConfiguration
{
    public static final String DRIVER_CLASS_NAME = "jdbc.hive.driverClassName";
    public static final String URL = "jdbc.hive.url";
    public static final String USERNAME = "jdbc.hive.username";
    public static final String PASSWORD = "jdbc.hive.password";
    public static final String MINIMUM_IDLE = "jdbc.hive.minimumIdle";
    public static final String MAXIMUM_POOL_SIZE = "jdbc.hive.maximumPoolSize";
    public static final String CONNECTION_TEST_QUERY = "jdbc.hive.connectionTestQuery";
    public static final String CACHE_PREPSTMTS = "jdbc.hive.cachePrepStmts";
    public static final String PREP_STMT_CACHESIZE = "jdbc.hive.prepStmtCacheSize";
    public static final String PREP_STMT_CACHESQLLIMIT = "jdbc.hive.prepStmtCacheSqlLimit";
    public static final String USE_SERVER_PREPSTMTS = "jdbc.hive.useServerPrepStmts";
    public static final String SCAN_MAPPER_PACKAGE = "jdbc.hive.scan.mapper.package";

    private HiveConfiguration()
    {}
}
