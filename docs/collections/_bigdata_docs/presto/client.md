---
title: Presto(ClientAPI)
category: Presto
order: 2
---

Presto(ClientAPI) is used to inject Presto into Guice container. After the configuration is loaded, it can be used globally. It is based on Presto client.

### Feature

- Support the display of accurate error information
- Complete data feedback

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-presto</artifactId>
    <version>[1.0.6, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Presto connector, create a catalog properties file in `conf/catalog/presto.properties` named, for example, presto.properties, to mount the Presto
connector as the presto catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
presto.url=http://localhost:8081
presto.backup.url=http://localhost:8082
presto.username=default
presto.password=
presto.catalog=hive
presto.schema=default
```

|Name|Description|Default|
|---|---|---|
|presto.url|Presto cluster HTTP access address|`http://localhost:8081`|
|presto.backup.url|Presto cluster alternate HTTP access address|`http://localhost:8082`|
|presto.username|User to connect to Presto service|`default`|
|presto.password|Password to connect to Presto service|` `|
|presto.catalog|Presto default query data source|`hive`|
|presto.schema|Database under the data source of Presto default query|`default`|

### Example

```java 
Injector injector = Guice.createInjector(new PrestoApiModule());
```

custom configuration

```java 
String path = "/etc/conf/catalog/presto.properties";
Injector injector = Guice.createInjector(new PrestoApiModule(path));
```

use client

```java 
@Inject
private PrestoApiClient apiClient;
    
apiClient.execute("SELECT * FROM system.runtime.nodes LIMIT 1")
```

response example

```java 
{
    columns=[node_id, http_uri, node_version, coordinator, state], 
    values=[[presto-worker-001, http://localhost:8081, 341, false, active]], startTime=1611936568724, endTime=1611936568788, taskId=20210129_160928_25055_khehp, elapsedTime=64
}
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/presto.properties`
{: .explainer}
