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
package io.edurt.gcm.quartz.job;

import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public final class JobInjectorFactory
        implements JobFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JobInjectorFactory.class);

    @Inject
    private Injector injector;

    public void setInjector(Injector injector)
    {
        this.injector = injector;
    }

    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
    {
        LOGGER.debug("Create new job for class <{}>", bundle.getJobDetail().getJobClass());
        Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
        return this.injector.getInstance(jobClass);
    }
}
