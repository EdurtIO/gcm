package io.edurt.gcm.test.runner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.edurt.gcm.test.annotation.JunitModuleLoader;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JunitRunner
        extends BlockJUnit4ClassRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JunitRunner.class);

    protected final Injector injector;

    public JunitRunner(Class<?> klass)
            throws InitializationError
    {
        super(klass);
        Class<?>[] classes = getModulesFor(klass);
        injector = createInjectorFor(classes);
    }

    @Override
    public Object createTest()
            throws Exception
    {
        Object obj = super.createTest();
        injector.injectMembers(obj);
        return obj;
    }

    private Injector createInjectorFor(Class<?>[] classes)
            throws InitializationError
    {
        Module[] modules = new Module[classes.length];
        for (int i = 0; i < classes.length; i++) {
            LOGGER.debug("Load class {} from local", modules[i]);
            try {
                modules[i] = (Module) (classes[i]).newInstance();
            }
            catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Load class error {}", e);
                throw new InitializationError(e);
            }
        }
        return Guice.createInjector(modules);
    }

    private Class<?>[] getModulesFor(Class<?> klass)
    {
        JunitModuleLoader annotation = klass.getAnnotation(JunitModuleLoader.class);
        if (ObjectUtils.isEmpty(annotation)) {
            return new Class[0];
        }
        return annotation.value();
    }
}
