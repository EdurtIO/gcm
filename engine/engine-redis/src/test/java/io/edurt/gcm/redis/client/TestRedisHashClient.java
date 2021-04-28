package io.edurt.gcm.redis.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.gcm.redis.RedisModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestRedisHashClient
{
    private RedisHashClient hashClient;
    private String key = "HashTest";

    @Before
    public void setUp()
    {
        Injector injector = Guice.createInjector(new RedisModule());
        hashClient = injector.getInstance(RedisHashClient.class);
    }

    @Test
    public void hashSet()
    {
        Map<String, String> value = new ConcurrentHashMap<>();
        value.put("hash1", "client1");
        value.put("hash2", "client2");
        value.put("hash3", "client3");
        this.hashClient.hashSet(key, value);
        Assert.assertTrue(this.hashClient.hashGetAll(key).keySet().size() == 3);
    }

    @Test
    public void hashUpdate()
    {
        Map<String, String> value = new ConcurrentHashMap<>();
        value.put("hash1", "client11");
        this.hashClient.hashUpdate(key, value);
        Assert.assertTrue(this.hashClient.hashGetAll(key).keySet().size() == 3);
        Assert.assertEquals(this.hashClient.hashGet(key, "hash1"), "client11");
    }

    @Test
    public void hashGetAll()
    {
        Assert.assertTrue(this.hashClient.hashGetAll(key).keySet().size() == 3);
    }

    @Test
    public void hashDelete()
    {
        this.hashClient.hashDelete(key, "hash1");
        Assert.assertTrue(this.hashClient.hashGetAll(key).keySet().size() == 2);
    }

    @Test
    public void hashGet()
    {
        Assert.assertEquals(this.hashClient.hashGet(key, "hash2"), "client2");
    }
}