package io.edurt.gcm.postgresql.hikari.configuration;

public class HikariConfigurationDefault
{
    public static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    public static final String URL = "jdbc:postgresql://localhost:5432/test?characterEncoding=utf8&amp;useSSL=true&amp;allowMultiQueries=true&amp;zeroDateTimeBehavior=convertToNull";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "123";
    public static final int MINIMUM_IDLE = 10;
    public static final int MAXIMUM_POOL_SIZE = 100;
    public static final String CONNECTION_TEST_QUERY = "select 1";
    public static final boolean CACHE_PREPSTMTS = true;
    public static final int PREP_STMT_CACHESIZE = 300;
    public static final int PREP_STMT_CACHESQLLIMIT = 2048;
    public static final boolean USE_SERVER_PREPSTMTS = true;
    public static final String SCAN_MAPPER_PACKAGE = "io.edurt.gcm.postgresql.hikari.mapper";

    private HikariConfigurationDefault()
    {}
}
