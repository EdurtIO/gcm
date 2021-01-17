package io.edurt.gcm.zookeeper;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.zookeeper.client.ZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class ZookeeperModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperModule.class);

    private final String configuration;

    public ZookeeperModule(String configuration)
    {
        this.configuration = configuration;
    }

    public ZookeeperModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "zookeeper.properties");
    }

    @Override
    protected void configure()
    {
        LOGGER.debug("Binding zookeeper component information");
        LOGGER.info("load configuration from local file {}", this.configuration);
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        LOGGER.info("Binding zookeeper configuration information is completed, with a total of {} configurations", configuration.stringPropertyNames().size());
        ZookeeperProvider zookeeperProvider = new ZookeeperProvider(configuration);
        bind(ZookeeperClient.class).toProvider(zookeeperProvider).in(Scopes.SINGLETON);
    }
}
