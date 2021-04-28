package io.edurt.gcm.redis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.gcm.redis.client.RedisListClient;
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
        RedisListClient client = injector.getInstance(RedisListClient.class);
        assertNotNull(client);
    }
} 
