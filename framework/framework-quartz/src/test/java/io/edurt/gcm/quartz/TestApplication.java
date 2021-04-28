package io.edurt.gcm.quartz;

import com.google.inject.Guice;

public class TestApplication
{
    private TestApplication()
    {
        Guice.createInjector(new QuartzModule());
    }

    public static void main(String[] args)
    {
        new TestApplication();
    }
}
