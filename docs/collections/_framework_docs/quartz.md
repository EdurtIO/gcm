---
title: Quartz
category: Framework
order: 2
---

Quartz component is used to inject quartz into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support custom path

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm.framework</groupId>
    <artifactId>gcm-framework-quartz</artifactId>
    <version>[1.2.0, )</version>
</dependency>
```

Add the above quartz information to the pom.xml Reload the download dependency information in the file.
### Configuration

To configure the quartz connector, create a catalog properties file in `conf/catalog/quartz.properties` named, for example, quartz.properties, to mount the quartz connector as the quartz catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
quartz.scan.job.package=io.edurt.gcm.quartz.job
```

|Name|Description|Default|
|---|---|---|
|quartz.scan.job.package|Job package scan path|`io.edurt.gcm.quartz.job`|

### Example

```java 
String path = "/etc/conf/catalog/quartz.properties";
Guice.createInjector(new QuartzModule(path));
```

### Job Example

```java 
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
```

> Warning: `path` The absolute path of the configuration file
{: .explainer}

### Available annotation

- `@Scheduled`: Used to mark whether the class is injected into the system as an interface provider