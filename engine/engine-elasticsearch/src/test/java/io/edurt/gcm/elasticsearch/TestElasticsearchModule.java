package io.edurt.gcm.elasticsearch;

import io.edurt.gcm.elasticsearch.client.ElasticsearchClient;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.runner.JunitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(JunitRunner.class)
@JunitModuleLoader(value = {ElasticsearchModule.class})
public class TestElasticsearchModule
{
    @Inject
    private ElasticsearchClient client;

    @Before
    public void setUp()
    {
    }

    @Test
    public void test()
    {
        System.out.println(client.getRestClient());
    }
}