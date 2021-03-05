package io.edurt.gcm.common.file;

import org.junit.Before;
import org.junit.Test;

public class TestPaths
{
    private String[] paths;

    @Before
    public void setUp()
    {
        paths = new String[] {"conf", "catalog", "gcm.properties"};
    }

    @Test
    public void getProjectHome()
    {
        System.out.println(Paths.getProjectHome());
    }

    @Test
    public void getProjectConfigurationHome()
    {
        System.out.println(Paths.getProjectConfigurationHome(paths));
    }
}