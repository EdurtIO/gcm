package io.edurt.gcm.zookeeper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class TestZookeeperModule
{
    private Injector injector;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new ZookeeperModule());
    }

    @Test
    public void test()
    {
        ZookeeperModule module = injector.getInstance(ZookeeperModule.class);
        assertNotNull(module);
    }
}