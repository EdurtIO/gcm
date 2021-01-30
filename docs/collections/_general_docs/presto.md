---
title: Presto
category: General
order: 2
---

Support open source distributed SQL query engine `presto`, which is suitable for interactive analysis and query. The data volume supports GB to Pb bytes.

### Feature

- Support presto engine

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-presto</artifactId>
    <name>Gcm for Presto Component</name>
    <description>Connect presto server and read and write data!</description>
    <version>[1.5.0, )</version>
</dependency>
```

### Configuration

To configure, create a module, replacing file path as appropriate for your setup:
```java 
String path = "/etc/conf/catalog/presto.properties";
Guice.createInjector(new ConfigurationModule(path));
```

in "presto.properties" file, wo can set the following parameters:
```java 
url,backup.url,username,password,retry,catalog,schema,source
```

### Example

```java 
String path = "/etc/conf/catalog/presto.properties";
Injector injector = Guice.createInjector(new ConfigurationModule(path));
```

```java
// in PrestoApiClient class
@RunWith(JunitRunner.class)
@JunitModuleLoader(value = {PrestoApiModule.class})
public class TestPrestoApiClient
{
    @Inject
    private PrestoApiClient apiClient;
}
```

> Warning: `path` The absolute path of the configuration file
{: .explainer}