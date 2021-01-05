package io.edurt.gcm.common.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * PropertiesUtils Tester.
 *
 * @author qianmoq
 * @version 1.0
 * @since <pre>1月 5, 2021</pre>
 */
public class TestPropertiesUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestPropertiesUtils.class);

    private String path1;
    private String path2;

    @Before
    public void before()
    {
        path1 = this.getClass().getResource("/file-1.properties").getPath();
        path2 = this.getClass().getResource("/file-2.properties").getPath();
    }

    @After
    public void after()
    {
    }

    /**
     * Method: loadProperties(String... resourcesPaths)
     */
    @Test
    public void testLoadProperties()
    {
        LOGGER.info("test load simple properties from path <{}>", path1);
        Properties properties1 = PropertiesUtils.loadProperties(path1);
        Assert.assertNotNull(properties1);
        Assert.assertTrue(properties1.entrySet().size() == 2);
        LOGGER.info("test load multiple properties from path <{}>, <{}>", path1, path2);
        Properties properties2 = PropertiesUtils.loadProperties(path1, path2);
        Assert.assertNotNull(properties2);
        Assert.assertTrue(properties2.entrySet().size() == 4);
    }

    /**
     * Method: getIntValue(Properties properties, String key, Integer defaultValue)
     */
    @Test
    public void testGetIntValue()
    {
        LOGGER.info("test get int value from properties");
        Properties properties = PropertiesUtils.loadProperties(path1);
        Assert.assertTrue(PropertiesUtils.getIntValue(properties, "value1", 1) == 1);
        Assert.assertTrue(PropertiesUtils.getIntValue(properties, "value2", 10) == 10);
    }

    /**
     * Method: getStringValue(Properties properties, String key, String defaultValue)
     */
    @Test
    public void testGetStringValue()
    {
        LOGGER.info("test get string value from properties");
        Properties properties = PropertiesUtils.loadProperties(path1);
        Assert.assertTrue(PropertiesUtils.getStringValue(properties, "name1", "").equals("Test1"));
        Assert.assertTrue(PropertiesUtils.getStringValue(properties, "name2", "12").equals("12"));
    }
} 
