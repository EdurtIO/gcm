package io.edurt.gcm.quartz.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.quartz.utils.Key.DEFAULT_GROUP;

/**
 * {@code Job} classes annotated with {@code Scheduled} will be automatically scheduled.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scheduled
{
    String DEFAULT = "gcm-quartz-default";

    /**
     * The {@code Job} name, must be unique within the group.
     */
    String jobName() default DEFAULT;

    /**
     * The {@code Job} group.
     */
    String jobGroup() default DEFAULT_GROUP;

    /**
     * Instructs the {@code Scheduler} whether or not the {@code Job} should
     * be re-executed if a {@code recovery} or {@code fail-over} situation is
     * encountered.
     */
    boolean requestRecovery() default false;

    /**
     * Whether or not the {@code Job} should remain stored after it is
     * orphaned (no {@code Trigger}s point to it).
     */
    boolean storeDurably() default false;

    /**
     * The {@code Trigger} name, must be unique within the group.
     */
    String triggerName() default DEFAULT;

    /**
     * The {@code Trigger} group.
     */
    String triggerGroup() default DEFAULT_GROUP;

    /**
     * The cron expression to base the schedule on.
     */
    String cronExpression();

    /**
     * The time zone for which the {@code cronExpression}
     * of this {@code CronTrigger} will be resolved.
     */
    String timeZoneId() default DEFAULT;

    /**
     * The {@code Trigger}'s priority.  When more than one {@code Trigger} have the same
     * fire time, the scheduler will fire the one with the highest priority
     * first.
     */
    int priority() default 0;
}
