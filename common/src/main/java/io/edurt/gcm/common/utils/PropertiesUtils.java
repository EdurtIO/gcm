package io.edurt.gcm.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    private PropertiesUtils()
    {}

    /**
     * load properties file
     *
     * @param resourcesPaths file path
     * @return properties stream
     */
    public static Properties loadProperties(String... resourcesPaths)
    {
        Properties props = new Properties();
        for (String location : resourcesPaths) {
            LOGGER.debug("load properties file from {}", location);
            File propertiesFile = new File(location);
            try (InputStream inputStream = new FileInputStream(propertiesFile)) {
                props.load(inputStream);
            }
            catch (IOException ex) {
                LOGGER.error("unable to load properties file path:" + location + ": " + ex.getMessage());
            }
        }
        return props;
    }

    /**
     * read int type data
     *
     * @param properties read configuration file information for
     * @param key key to read data
     * @param defaultValue default value
     * @return if the data is read, the default value will be returned
     */
    public static Integer getIntValue(Properties properties, String key, Integer defaultValue)
    {
        return Integer.valueOf(getStringValue(properties, key, String.valueOf(defaultValue)));
    }

    public static String getStringValue(Properties properties, String key, String defaultValue)
    {
        if (ObjectUtils.isEmpty(properties)) {
            LOGGER.debug("the configuration file passed is empty and the default value will be used");
            return defaultValue;
        }
        if (!properties.containsKey(key)) {
            LOGGER.debug("the configuration passed does not contain the primary key <{}>, which needs to be resolved. The default value will be used", key);
            return defaultValue;
        }
        return String.valueOf(properties.getOrDefault(key, defaultValue));
    }

    public static Boolean getBoolValue(Properties properties, String key, Boolean defaultValue)
    {
        return Boolean.valueOf(getStringValue(properties, key, String.valueOf(defaultValue)));
    }
}
