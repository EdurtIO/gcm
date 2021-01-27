package io.edurt.gcm.redis.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.gcm.redis.RedisModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRedisListClient
{
    private RedisListClient listClient;
    private String key = "ListTest";

    @Before
    public void setUp()
    {
        Injector injector = Guice.createInjector(new RedisModule());
        listClient = injector.getInstance(RedisListClient.class);
    }

    @Test
    public void listSet()
    {
        listClient.delete(key);
        this.listClient.listSet(key, "List", "Test", "Guice");
        Assert.assertTrue(true);
    }

    @Test
    public void listGet()
    {
        Assert.assertTrue(this.listClient.listGet(key).size() == 3);
        Assert.assertTrue(this.listClient.listGet(key, 0L, 1L).size() == 2);
        Assert.assertTrue(this.listClient.listGet(key, 0L, null).size() == 3);
    }

    @Test
    public void listDelete()
    {
        Assert.assertTrue(this.listClient.listDelete(key, null, "List") > 0);
        Assert.assertTrue(this.listClient.listGet(key).size() == 2);
    }
}