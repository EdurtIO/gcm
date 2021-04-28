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
package io.edurt.gcm.quartz.scheduler;

import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Set;

public final class SchedulerProvider
        implements Provider<Scheduler>
{
    private final Scheduler scheduler;

    @Inject
    public SchedulerProvider(SchedulerConfiguration schedulerConfiguration)
            throws SchedulerException
    {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        if (schedulerConfiguration.getProperties() != null) {
            schedulerFactory.initialize(schedulerConfiguration.getProperties());
        }
        this.scheduler = schedulerFactory.getScheduler();
        if (!schedulerConfiguration.startManually()) {
            scheduler.start();
        }
    }

    @Inject
    public void setJobFactory(JobFactory jobFactory)
            throws SchedulerException
    {
        scheduler.setJobFactory(jobFactory);
    }

    @com.google.inject.Inject(optional = true)
    public void addJobListeners(Set<JobListener> jobListeners)
            throws SchedulerException
    {
        for (JobListener jobListener : jobListeners) {
            scheduler.getListenerManager().addJobListener(jobListener);
        }
    }

    @com.google.inject.Inject(optional = true)
    public void addSchedulerListeners(Set<SchedulerListener> schedulerListeners)
            throws SchedulerException
    {
        for (SchedulerListener schedulerListener : schedulerListeners) {
            scheduler.getListenerManager().addSchedulerListener(schedulerListener);
        }
    }

    @com.google.inject.Inject(optional = true)
    public void addTriggerListeners(Set<TriggerListener> triggerListeners)
            throws SchedulerException
    {
        for (TriggerListener triggerListener : triggerListeners) {
            scheduler.getListenerManager().addTriggerListener(triggerListener);
        }
    }

    public Scheduler get()
    {
        return scheduler;
    }
}
