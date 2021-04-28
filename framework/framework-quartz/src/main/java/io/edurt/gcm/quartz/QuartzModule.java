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
package io.edurt.gcm.quartz;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import io.edurt.gcm.common.jdk.Classs;
import io.edurt.gcm.common.utils.ObjectUtils;
import io.edurt.gcm.common.utils.PropertiesUtils;
import io.edurt.gcm.quartz.annotation.Scheduled;
import io.edurt.gcm.quartz.configuration.QuartzConfiguration;
import io.edurt.gcm.quartz.configuration.QuartzConfigurationDefault;
import io.edurt.gcm.quartz.job.JobInjectorFactory;
import io.edurt.gcm.quartz.job.JobSchedulerBuilder;
import io.edurt.gcm.quartz.scheduler.SchedulerConfiguration;
import io.edurt.gcm.quartz.scheduler.SchedulerConfigurationBuilder;
import io.edurt.gcm.quartz.scheduler.SchedulerProvider;
import org.quartz.Job;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

public class QuartzModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzModule.class);
    private final String configuration;
    private Multibinder<JobListener> jobListeners;
    private Multibinder<TriggerListener> triggerListeners;
    private Multibinder<SchedulerListener> schedulerListeners;
    private SchedulerConfiguration schedulerConfiguration;

    public QuartzModule(String configuration)
    {
        this.configuration = configuration;
    }

    public QuartzModule()
    {
        this.configuration = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "catalog",
                "quartz.properties");
    }

    @Override
    protected void configure()
    {
        Properties configuration = PropertiesUtils.loadProperties(this.configuration);
        jobListeners = Multibinder.newSetBinder(binder(), JobListener.class);
        triggerListeners = Multibinder.newSetBinder(binder(), TriggerListener.class);
        schedulerListeners = Multibinder.newSetBinder(binder(), SchedulerListener.class);
        schedulerConfiguration = new SchedulerConfiguration();
        try {
            schedule(PropertiesUtils.getStringValue(configuration,
                    QuartzConfiguration.SCAN_JOB_PACKAGE,
                    QuartzConfigurationDefault.SCAN_JOB_PACKAGE));
            bind(JobFactory.class).to(JobInjectorFactory.class).in(Scopes.SINGLETON);
            bind(Scheduler.class).toProvider(SchedulerProvider.class).asEagerSingleton();
            bind(SchedulerConfiguration.class).toInstance(schedulerConfiguration);
        }
        finally {
            jobListeners = null;
            triggerListeners = null;
            schedulerListeners = null;
            schedulerConfiguration = null;
        }
    }

    /**
     * Part of the EDSL builder language for configuring {@code Job}s.
     * Here is a typical example of scheduling {@code Job}s when creating your Guice injector:
     *
     * <pre>
     * Guice.createInjector(..., new QuartzModule() {
     *
     *     {@literal @}Override
     *     protected void schedule() {
     *       <b>scheduleJob(MyJobImpl.class).withCronExpression("0/2 * * * * ?");</b>
     *     }
     *
     * });
     * </pre>
     *
     * @see JobSchedulerBuilder
     */
    public void schedule(String packages)
    {
        LOGGER.info("Scan job from {}", packages);
        Set<Class<?>> classes = Classs.scanClassInPackage(packages);
        if (ObjectUtils.isEmpty(classes) || classes.size() < 1) {
            LOGGER.warn("Scan router is empty from {}", packages);
        }
        classes.forEach(v -> scheduleJob((Class<? extends Job>) v));
    }

    /**
     * Allows to configure the scheduler.
     *
     * <pre>
     * Guice.createInjector(..., new QuartzModule() {
     *
     *     {@literal @}Override
     *     protected void schedule() {
     *       configureScheduler().withManualStart().withProperties(...);
     *     }
     *
     * });
     * </pre>
     *
     * @since 1.1
     */
    protected final SchedulerConfigurationBuilder configureScheduler()
    {
        return schedulerConfiguration;
    }

    /**
     * Add the {@code JobListener} binding.
     *
     * @param jobListenerType The {@code JobListener} class has to be bound
     */
    protected final void addJobListener(Class<? extends JobListener> jobListenerType)
    {
        doBind(jobListeners, jobListenerType);
    }

    /**
     * Add the {@code TriggerListener} binding.
     *
     * @param triggerListenerType The {@code TriggerListener} class has to be bound
     */
    protected final void addTriggerListener(Class<? extends TriggerListener> triggerListenerType)
    {
        doBind(triggerListeners, triggerListenerType);
    }

    /**
     * Add the {@code SchedulerListener} binding.
     *
     * @param schedulerListenerType The {@code SchedulerListener} class has to be bound
     */
    protected final void addSchedulerListener(Class<? extends SchedulerListener> schedulerListenerType)
    {
        doBind(schedulerListeners, schedulerListenerType);
    }

    /**
     * Allows {@code Job} scheduling, delegating Guice create the {@code Job} instance
     * and inject members.
     * <p>
     * If given {@code Job} class is annotated with {@link Scheduled}, then {@code Job}
     * and related {@code Trigger} values will be extracted from it.
     *
     * @param jobClass The {@code Job} has to be scheduled
     * @return The {@code Job} builder
     */
    protected final JobSchedulerBuilder scheduleJob(Class<? extends Job> jobClass)
    {
        JobSchedulerBuilder builder = new JobSchedulerBuilder(jobClass);
        if (jobClass.isAnnotationPresent(Scheduled.class)) {
            Scheduled scheduled = jobClass.getAnnotation(Scheduled.class);
            builder.withJobName(scheduled.jobName())
                    .withJobGroup(scheduled.jobGroup())
                    .withRequestRecovery(scheduled.requestRecovery())
                    .withStoreDurably(scheduled.storeDurably())
                    .withCronExpression(scheduled.cronExpression())
                    .withTriggerName(scheduled.triggerName());
            if (!Scheduled.DEFAULT.equals(scheduled.timeZoneId())) {
                TimeZone timeZone = TimeZone.getTimeZone(scheduled.timeZoneId());
                if (timeZone != null) {
                    builder.withTimeZone(timeZone);
                }
            }
            requestInjection(builder);
            LOGGER.info("Create job <{}> Success!", jobClass.getName());
        }
        return builder;
    }

    protected final <T> void doBind(Multibinder<T> binder, Class<? extends T> type)
    {
        binder.addBinding().to(type);
    }
}
