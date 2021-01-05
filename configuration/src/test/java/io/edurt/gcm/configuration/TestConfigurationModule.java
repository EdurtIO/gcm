package io.edurt.gcm.configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * ConfigurationModule Tester.
 *
 * @author qianmoq
 * @version 1.0
 * @since <pre>1月 5, 2021</pre>
 */
public class TestConfigurationModule
{
    private String path;

    @Before
    public void before()
    {
        path = this.getClass().getResource("/file.properties").getPath();
    }

    @After
    public void after()
    {
    }

    /**
     * Method: configure()
     */
    @Test
    public void testConfigure()
    {
        Injector injector = Guice.createInjector(new ConfigurationModule(path));
        Assert.assertTrue(injector.getBindings().toString().contains("Test1"));
    }
} 
