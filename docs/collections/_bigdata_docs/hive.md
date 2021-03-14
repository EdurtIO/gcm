---
title: Hive
category: General
order: 3
---

Hive component is used to inject Hive into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support Hive version for 2.x - 3.x
- Support custom configuration

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-hive</artifactId>
    <version>[1.0.9, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Hive connector, create a catalog properties file in `conf/catalog/hive.properties` named, for example, hive.properties, to mount the Hive connector as the hive catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
jdbc.hive.driverClassName=org.apache.hive.jdbc.HiveDriver
jdbc.hive.url=jdbc:hive2://localhost:10000/default
jdbc.hive.username=
jdbc.hive.password=
jdbc.hive.minimumIdle=10
jdbc.hive.maximumPoolSize=100
jdbc.hive.connectionTestQuery=select 1
jdbc.hive.cachePrepStmts=true
jdbc.hive.prepStmtCacheSize=250
jdbc.hive.prepStmtCacheSqlLimit=2048
jdbc.hive.useServerPrepStmts=true
```

|Name|Description|Default|
|---|---|---|
|jdbc.hive.url|Hive connection server connection string, need to carry database information|`jdbc:hive2://localhost:10000/default`|
|jdbc.hive.username|User name to connect to hive service|` `|
|jdbc.hive.password|Password to connect to hive service|` `|
|jdbc.hive.minimumIdle|-|`10`|
|jdbc.hive.maximumPoolSize|-|`100`|
|jdbc.hive.connectionTestQuery|-|`select 1`|
|jdbc.hive.cachePrepStmts|-|`true`|
|jdbc.hive.prepStmtCacheSize|-|`250`|
|jdbc.hive.prepStmtCacheSqlLimit|-|`2048`|
|jdbc.hive.useServerPrepStmts|-|`true`|
|jdbc.hive.scan.mapper.package|Scan the mapper file for the specified source directory|`io.edurt.gcm.hive.mapper`|

### Example

```java 
Injector injector = Guice.createInjector(new HiveModule());
```

custom configuration

```java 
String path = "/etc/conf/catalog/hive.properties";
Injector injector = Guice.createInjector(new HiveModule(path));
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/hive.properties`
{: .explainer}
