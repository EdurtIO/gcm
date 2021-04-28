package io.edurt.gcm.configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import io.edurt.gcm.configuration.annotation.ConfigurationProperties;
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
@ConfigurationProperties(value = "/etc/test.properties")
public class TestConfigurationModule
{
    private String path;
    private Injector injector;

    @Before
    public void before()
    {
        path = this.getClass().getResource("/file.properties").getPath();
        injector = Guice.createInjector(new ConfigurationModule(path));
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
        Assert.assertTrue(injector.getBindings().toString().contains("Test1"));
    }

    @Test
    public void testAnnotationConfiguration()
    {
        Assert.assertEquals("Test", injector.getInstance(Key.get(String.class, Names.named("annotation"))));
    }
}
