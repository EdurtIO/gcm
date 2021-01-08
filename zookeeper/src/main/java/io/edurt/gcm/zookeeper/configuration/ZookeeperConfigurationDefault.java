package io.edurt.gcm.zookeeper.configuration;

public class ZookeeperConfigurationDefault
{
    public static final String SERVICE = "localhost:2181";
    public static final Integer SESSION_TIMEOUT = 5000;
    public static final Integer CONNECTION_TIMEOUT = 5000;
    public static final Integer RETRY = 3;
    public static final String NAMESPACE = "default";

    private ZookeeperConfigurationDefault() {}
}
