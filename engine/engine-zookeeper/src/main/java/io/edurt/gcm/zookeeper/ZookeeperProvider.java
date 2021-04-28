package io.edurt.gcm.zookeeper;

import com.google.inject.Provider;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.zookeeper.client.ZookeeperClient;
import io.edurt.gcm.zookeeper.configuration.ZookeeperConfiguration;
import io.edurt.gcm.zookeeper.configuration.ZookeeperConfigurationDefault;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ZookeeperProvider
        implements Provider<ZookeeperClient>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperProvider.class);

    private Properties configuration;

    public ZookeeperProvider(Properties configuration)
    {
        this.configuration = configuration;
    }

    private CuratorFramework getZookeeperClient()
    {
        LOGGER.info("Building zookeeper connection client");
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(
                PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.CONNECTION_TIMEOUT,
                        ZookeeperConfigurationDefault.CONNECTION_TIMEOUT),
                PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.RETRY,
                        ZookeeperConfigurationDefault.RETRY));
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(PropertiesUtils.getStringValue(configuration,
                        ZookeeperConfiguration.SERVICE,
                        ZookeeperConfigurationDefault.SERVICE))
                .sessionTimeoutMs(PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.SESSION_TIMEOUT,
                        ZookeeperConfigurationDefault.SESSION_TIMEOUT))
                .connectionTimeoutMs(PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.CONNECTION_TIMEOUT,
                        ZookeeperConfigurationDefault.CONNECTION_TIMEOUT))
                .retryPolicy(retryPolicy)
                .namespace(PropertiesUtils.getStringValue(configuration,
                        ZookeeperConfiguration.NAMESPACE,
                        ZookeeperConfigurationDefault.NAMESPACE))
                .build();
        return client;
    }

    @Override
    public ZookeeperClient get()
    {
        return new ZookeeperClient(getZookeeperClient());
    }
}
