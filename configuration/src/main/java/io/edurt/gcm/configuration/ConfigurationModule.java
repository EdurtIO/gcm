package io.edurt.gcm.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConfigurationModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationModule.class);

    // properties stream
    private Properties bootstrapConfiguration;

    public ConfigurationModule(Properties bootstrapConfiguration)
    {
        this.bootstrapConfiguration = bootstrapConfiguration;
    }

    public ConfigurationModule(String configurationFilePath)
    {
        LOGGER.info("Load the local configuration file with the path address of {}", configurationFilePath);
        if (ObjectUtils.isNotEmpty(configurationFilePath)) {
            this.bootstrapConfiguration = PropertiesUtils.loadProperties(configurationFilePath);
        }
    }

    @Override
    protected void configure()
    {
        LOGGER.info("start binding configuration file related configuration information");
        this.bootstrapConfiguration
                .stringPropertyNames()
                .forEach(v -> LOGGER.info("the primary key of the binding configuration information is {}, and the value is {}", v, bootstrapConfiguration.get(v)));
        Names.bindProperties(binder(), bootstrapConfiguration);
        LOGGER.info("end of binding configuration file related configuration information, with a total of {} configuration information bound", bootstrapConfiguration.stringPropertyNames().size());
    }
}
