package io.edurt.gcm.base.client;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestBasicRestfulConnectionFactory
{
    private BaseRestfulConfig config;

    @Before
    public void before()
    {
        config = new BaseRestfulConfig();
        config.setHost("gcm.incubator.edurt.io");
        config.setPort(443);
        config.setProtocol("https");
        config.setAutoConnection(Boolean.FALSE);
        config.setTimeout(20);
    }

    @Test
    public void test_getExecute()
            throws IOException
    {
        BasicRestfulConnectionFactory factory = BasicRestfulConnectionFactory.builder(config);
        System.out.println(factory.getExecute(null).body().string());
    }

    @Test
    public void test_getExecuteWithParams()
            throws IOException
    {
        config.setHost("localhost");
        config.setPort(8123);
        config.setProtocol("http");
        BasicRestfulConnectionFactory factory = BasicRestfulConnectionFactory.builder(config);
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("query", String.format("%s FORMAT TabSeparatedWithNames", "SELECT name, value FROM system.settings"));
        System.out.println(factory.getExecute(params).body().string());
    }
}