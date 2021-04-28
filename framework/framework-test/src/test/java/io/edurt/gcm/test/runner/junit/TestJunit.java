package io.edurt.gcm.test.runner.junit;

import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.runner.JunitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(JunitRunner.class)
@JunitModuleLoader(value = {TestJunitModule.class})
public class TestJunit
{
    @Inject
    private TestService testService;
    private String message;

    @Before
    public void before()
    {
        message = "Hello Junit Component";
    }

    @Test
    public void println()
    {
        this.testService.println(message);
    }
}
