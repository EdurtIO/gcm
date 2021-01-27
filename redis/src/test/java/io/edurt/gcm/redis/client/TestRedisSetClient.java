package io.edurt.gcm.redis.client;

import io.edurt.gcm.redis.RedisModule;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.runner.JunitRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(value = JunitRunner.class)
@JunitModuleLoader(value = {RedisModule.class})
public class TestRedisSetClient
{
    @Inject
    private RedisSetClient setClient;
    private String key = "SetClient";

    @Test
    public void set()
    {
        this.setClient.set(key, "Set", "Client", "Set", "Guice");
        Assert.assertTrue(this.setClient.getAll(key).size() == 3);
    }

    @Test
    public void deleteValue()
    {
        this.setClient.deleteValue(key, "Set");
        Assert.assertTrue(this.setClient.getAll(key).size() == 2);
    }

    @Test
    public void getAll()
    {
        Assert.assertTrue(this.setClient.getAll(key).size() == 3);
    }
}