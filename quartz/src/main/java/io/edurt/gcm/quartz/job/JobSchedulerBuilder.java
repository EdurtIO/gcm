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

import com.google.inject.ProvisionException;
import io.edurt.gcm.quartz.annotation.Scheduled;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import javax.inject.Inject;

import java.util.TimeZone;

import static java.lang.String.format;
import static java.util.TimeZone.getDefault;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;
import static org.quartz.utils.Key.DEFAULT_GROUP;

public final class JobSchedulerBuilder
{
    private final Class<? extends Job> jobClass;
    private String jobName = Scheduled.DEFAULT;
    private String jobGroup = DEFAULT_GROUP;
    private boolean requestRecovery = false;
    private boolean storeDurably = false;
    private String triggerName = Scheduled.DEFAULT;
    private String triggerGroup = DEFAULT_GROUP;
    private String cronExpression;
    private TimeZone timeZone = getDefault();
    private int priority = 0;
    private Trigger trigger;
    private boolean updateExistingTrigger = false;

    public JobSchedulerBuilder(final Class<? extends Job> jobClass)
    {
        this.jobClass = jobClass;
    }

    public JobSchedulerBuilder withJobName(String jobName)
    {
        this.jobName = jobName;
        return this;
    }

    public JobSchedulerBuilder withJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
        return this;
    }

    public JobSchedulerBuilder withRequestRecovery(boolean requestRecovery)
    {
        this.requestRecovery = requestRecovery;
        return this;
    }

    public JobSchedulerBuilder withStoreDurably(boolean storeDurably)
    {
        this.storeDurably = storeDurably;
        return this;
    }

    public JobSchedulerBuilder withTriggerName(String triggerName)
    {
        this.triggerName = triggerName;
        return this;
    }

    public JobSchedulerBuilder withTriggerGroup(String triggerGroup)
    {
        this.triggerGroup = triggerGroup;
        return this;
    }

    public JobSchedulerBuilder withCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
        return this;
    }

    public JobSchedulerBuilder withTimeZone(TimeZone timeZone)
    {
        this.timeZone = timeZone;
        return this;
    }

    public JobSchedulerBuilder withPriority(int priority)
    {
        this.priority = priority;
        return this;
    }

    public JobSchedulerBuilder withTrigger(Trigger trigger)
    {
        this.trigger = trigger;
        return this;
    }

    public JobSchedulerBuilder updateExistingTrigger()
    {
        this.updateExistingTrigger = true;
        return this;
    }

    @Inject
    public void schedule(Scheduler scheduler)
            throws Exception
    {
        if (cronExpression == null && trigger == null) {
            throw new ProvisionException(format("Impossible to schedule Job '%s' without cron expression",
                    jobClass.getName()));
        }
        if (cronExpression != null && trigger != null) {
            throw new ProvisionException(format("Impossible to schedule Job '%s' with cron expression " +
                    "and an associated Trigger at the same time", jobClass.getName()));
        }
        JobKey jobKey = jobKey(Scheduled.DEFAULT.equals(jobName) ? jobClass.getName() : jobName, jobGroup);
        TriggerKey triggerKey = triggerKey(Scheduled.DEFAULT.equals(triggerName) ? jobClass.getCanonicalName() : triggerName, triggerGroup);
        if (updateExistingTrigger && scheduler.checkExists(triggerKey)) {
            scheduler.unscheduleJob(triggerKey);
        }
        scheduler.scheduleJob(newJob(jobClass)
                        .withIdentity(jobKey)
                        .requestRecovery(requestRecovery)
                        .storeDurably(storeDurably).build(),
                (trigger == null) ?
                        newTrigger()
                                .withIdentity(triggerKey)
                                .withSchedule(cronSchedule(cronExpression)
                                        .inTimeZone(timeZone))
                                .withPriority(priority)
                                .build()
                        : trigger);
    }
}
