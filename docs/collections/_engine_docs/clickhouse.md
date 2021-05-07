---
title: ClickHouse(Hikaricp)
category: Engine
order: 2
---

ClickHouse component is used to inject ClickHouse into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support ClickHouse version for 19.x
- Support Hikaricp datasource

### How to use

```xml

<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-clickhouse</artifactId>
    <version>[1.0.5, )</version>
</dependency>
```

> Warning:  >= 1.2.0 has been replaced with the following usage
{: .explainer}

```xml

<dependency>
    <groupId>io.edurt.gcm.engine</groupId>
    <artifactId>gcm-engine-clickhouse</artifactId>
    <version>[1.2.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the ClickHouse connector, create a catalog properties file in `conf/catalog/clickhouse.properties` named, for example, clickhouse.properties, to mount the ClickHouse
connector as the clickhouse catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
jdbc.clickhouse.driverClassName=ru.yandex.clickhouse.ClickHouseDriver
jdbc.clickhouse.url=jdbc:clickhouse://localhost:8123/default
jdbc.clickhouse.username=
jdbc.clickhouse.password=
jdbc.clickhouse.minimumIdle=10
jdbc.clickhouse.maximumPoolSize=100
jdbc.clickhouse.connectionTestQuery=select 1
jdbc.clickhouse.cachePrepStmts=true
jdbc.clickhouse.prepStmtCacheSize=250
jdbc.clickhouse.prepStmtCacheSqlLimit=2048
jdbc.clickhouse.useServerPrepStmts=true
```

|Name|Description|Default|
|---|---|---|
|jdbc.clickhouse.url|ClickHouse connection server connection string, need to carry database information|`jdbc:clickhouse://localhost:8123/default`|
|jdbc.clickhouse.username|User name to connect to ClickHouse service|` `|
|jdbc.clickhouse.password|Password to connect to ClickHouse service|` `|
|jdbc.clickhouse.minimumIdle|-|`10`|
|jdbc.clickhouse.maximumPoolSize|-|`100`|
|jdbc.clickhouse.connectionTestQuery|-|`select 1`|
|jdbc.clickhouse.cachePrepStmts|-|`true`|
|jdbc.clickhouse.prepStmtCacheSize|-|`250`|
|jdbc.clickhouse.prepStmtCacheSqlLimit|-|`2048`|
|jdbc.clickhouse.useServerPrepStmts|-|`true`|
|jdbc.clickhouse.scan.mapper.package|Scan the mapper file for the specified source directory|`io.edurt.gcm.clickhouse.mapper`|

### Example

```java 
Injector injector = Guice.createInjector(new ClickHouseModule());
```

custom configuration

```java 
String path = "/etc/conf/catalog/clickhouse.properties";
Injector injector = Guice.createInjector(new ClickHouseModule(path));
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/clickhouse.properties`
{: .explainer}

### Available annotation

- `@ClickHouseSource`: It is used to distinguish the data sources used in the case of multiple sources
