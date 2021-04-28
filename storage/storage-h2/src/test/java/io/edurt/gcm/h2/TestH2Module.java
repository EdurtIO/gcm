package io.edurt.gcm.h2;

import io.edurt.gcm.h2.mapper.TestMapper;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import io.edurt.gcm.test.runner.JunitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(JunitRunner.class)
@JunitModuleLoader(H2Module.class)
public class TestH2Module
{
    @Inject
    private TestMapper mapper;

    @Test
    public void test()
    {
        mapper.runSql("show tables").forEach(System.out::println);
    }
}