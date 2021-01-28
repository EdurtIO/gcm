/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.edurt.gcm.test.runner.mybatis;

import com.google.inject.Inject;
import io.edurt.gcm.test.annotation.mybatis.MapperClasses;
import io.edurt.gcm.test.runner.JunitRunner;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSessionManager;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBatisRunner
        extends JunitRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JunitRunner.class);

    @Inject
    private SqlSessionManager sqlSessionManager;

    public MyBatisRunner(Class<?> klass)
            throws InitializationError
    {
        super(klass);
        this.injector.injectMembers(getAnnotatedMapperClasses(klass));
        this.injector.injectMembers(this);
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object instance)
    {
        return new Statement()
        {
            @Override
            public void evaluate()
                    throws Throwable
            {
                try {
                    LOGGER.info("Current execution instance {}, execution method {}", instance, method);
                    invokeMethodInNewSession(method, instance);
                }
                finally {
                    LOGGER.info("Rollback all database operations.");
                    cleanupSession();
                }
            }
        };
    }

    private void invokeMethodInNewSession(final FrameworkMethod method, final Object instance)
            throws Throwable
    {
        LOGGER.info("Start session ...");
        sqlSessionManager.startManagedSession();
        method.invokeExplosively(instance);
    }

    private void cleanupSession()
    {
        try {
            sqlSessionManager.rollback(true);
        }
        finally {
            LOGGER.info("Close sql session manager.");
            sqlSessionManager.close();
        }
    }

    private Class<?>[] getAnnotatedMapperClasses(final Class<?> clazz)
            throws InitializationError
    {
        MapperClasses annotation = clazz.getAnnotation(MapperClasses.class);
        if (ObjectUtils.isEmpty(annotation)) {
            LOGGER.debug("Class '{}' must have a MapperClasses annotation", clazz.getName());
            throw new InitializationError(String.format("Class '%s' must have a MapperClasses annotation", clazz.getName()));
        }
        return annotation.value();
    }
}
