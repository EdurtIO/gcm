package io.edurt.gcm.test;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJunitModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestJunitModule.class);

    @Override
    protected void configure()
    {
        LOGGER.info("start binding junit component related configuration information");
        bind(TestService.class).to(TestServiceImpl.class);
    }
}
