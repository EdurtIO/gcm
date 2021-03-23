package io.edurt.gcm.derby.configuration;

public class DerbyConfiguration
{
    public static final String DRIVER_CLASS_NAME = "jdbc.derby.driverClassName";
    public static final String URL = "jdbc.derby.url";
    public static final String USERNAME = "jdbc.derby.username";
    public static final String PASSWORD = "jdbc.derby.password";
    public static final String MINIMUM_IDLE = "jdbc.derby.minimumIdle";
    public static final String MAXIMUM_POOL_SIZE = "jdbc.derby.maximumPoolSize";
    public static final String CONNECTION_TEST_QUERY = "jdbc.derby.connectionTestQuery";
    public static final String CACHE_PREPSTMTS = "jdbc.derby.cachePrepStmts";
    public static final String PREP_STMT_CACHESIZE = "jdbc.derby.prepStmtCacheSize";
    public static final String PREP_STMT_CACHESQLLIMIT = "jdbc.derby.prepStmtCacheSqlLimit";
    public static final String USE_SERVER_PREPSTMTS = "jdbc.derby.useServerPrepStmts";
    public static final String SCAN_MAPPER_PACKAGE = "jdbc.derby.scan.mapper.package";

    private DerbyConfiguration()
    {}
}
