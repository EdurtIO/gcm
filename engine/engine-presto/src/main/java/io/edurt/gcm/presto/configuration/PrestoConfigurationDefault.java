package io.edurt.gcm.presto.configuration;

public class PrestoConfigurationDefault
{
    public static final String URL = "http://localhost:8081";
    public static final String URL_BACKUP = "http://localhost:8082";
    public static final String USERNAME = "default";
    public static final String PASSWORD = "";
    public static final Integer RETRY = 3;
    public static final String CATALOG = "hive";
    public static final String SCHEMA = "default";
    public static final String SOURCE = "";

    private PrestoConfigurationDefault()
    {}
}
