package io.edurt.gcm.redis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.gcm.redis.client.RedisClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * RedisModule Tester.
 *
 * @author qianmoq
 * @version 1.0
 * @since <pre>1月 6, 2021</pre>
 */
public class TestRedisModule
{
    @Before
    public void before()
    {
    }

    @After
    public void after()
    {
    }

    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(new RedisModule());
        RedisClient client = injector.getInstance(RedisClient.class);
        assertNotNull(client);
    }
} 
