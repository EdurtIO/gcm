package io.edurt.gcm.hive;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.gcm.hive.mapper.TestMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestHiveModule
{
    private String path;

    @Before
    public void setUp()
    {
        this.path = this.getClass().getResource("/hive.properties").getPath();
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void test()
    {
        Injector injector = Guice.createInjector(new HiveModule(path));
        TestMapper mapper = injector.getInstance(TestMapper.class);
        mapper.findAll().forEach(System.out::println);
    }
}