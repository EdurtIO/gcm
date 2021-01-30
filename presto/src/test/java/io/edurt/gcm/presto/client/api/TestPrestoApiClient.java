package io.edurt.gcm.presto.client.api;

import io.edurt.gcm.presto.PrestoApiModule;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.runner.JunitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(JunitRunner.class)
@JunitModuleLoader(value = {PrestoApiModule.class})
public class TestPrestoApiClient
{
    private String query;

    @Inject
    private PrestoApiClient apiClient;

    @Before
    public void setUp()
    {
        query = "SELECT * FROM system.runtime.nodes LIMIT 100";
    }

    @Test
    public void execute()
    {
        System.out.println(this.apiClient.execute(query));;
    }
}