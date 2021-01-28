package io.edurt.gcm.common.http;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestGcmHttp
{
    private GcmHttp gcmHttp;
    private String url;

    @Before
    public void before()
    {
        this.gcmHttp = new GcmHttp(3, false);
        this.url = "http://www.google.com";
    }

    @Test
    public void get()
    {
        Assert.assertNotNull(this.gcmHttp.get(url));
    }

    @Test
    public void testGet()
    {
        Map<String, String> params = new ConcurrentHashMap<>();
        params.put("q", "hello");
        Assert.assertNotNull(this.gcmHttp.get(url, params));
    }

    @Test
    public void post()
    {
    }

    @Test
    public void testPost()
    {
    }
}