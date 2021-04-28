package io.edurt.gcm.quartz.job;

import io.edurt.gcm.quartz.annotation.Scheduled;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Singleton
@Scheduled(jobName = "testJob", cronExpression = "*/5 * * * * ?")
public class TestJob
        implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException
    {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = formatter.format(time);
        System.out.println(String.format("Current time %s", startTime));
    }
}
