package io.edurt.gcm.netty;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestNonControllerNettyModule
{
    public static void main(String[] args)
            throws Exception
    {
        Injector injector = Guice.createInjector(new NettyModule());
        final GcmNettyApplication server = injector.getInstance(GcmNettyApplication.class);
        server.start();
    }
}
