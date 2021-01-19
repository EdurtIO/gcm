package io.edurt.gcm.clickhouse.configuration;

public class ClickHouseConfigurationDefault
{
    public static final String DRIVER_CLASS_NAME = "ru.yandex.clickhouse.ClickHouseDriver";
    public static final String URL = "jdbc:clickhouse://localhost:8123/default";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";
    public static final int MINIMUM_IDLE = 10;
    public static final int MAXIMUM_POOL_SIZE = 100;
    public static final String CONNECTION_TEST_QUERY = "select 1";
    public static final boolean CACHE_PREPSTMTS = true;
    public static final int PREP_STMT_CACHESIZE = 300;
    public static final int PREP_STMT_CACHESQLLIMIT = 2048;
    public static final boolean USE_SERVER_PREPSTMTS = true;
    public static final String SCAN_MAPPER_PACKAGE = "io.edurt.gcm.clickhouse.mapper";

    private ClickHouseConfigurationDefault()
    {}
}
