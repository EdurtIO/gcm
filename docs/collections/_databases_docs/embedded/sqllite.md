---
title: SQLLite
category: Embedded
order: 3
---

sqllite component is used to inject sqllite into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support sqllite version

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-sqllite</artifactId>
    <version>[1.1.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the sqllite connector, create a catalog properties file in `conf/catalog/sqllite.properties` named, for example, sqllite.properties, to mount the sqllite connector as the sqllite catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
jdbc.sqllite.driverClassName=org.sqlite.JDBC
jdbc.sqllite.url=jdbc:sqlite:test
jdbc.sqllite.username=root
jdbc.sqllite.password=123
jdbc.sqllite.minimumIdle=10
jdbc.sqllite.maximumPoolSize=100
jdbc.sqllite.connectionTestQuery=select 1
jdbc.sqllite.cachePrepStmts=true
jdbc.sqllite.prepStmtCacheSize=250
jdbc.sqllite.prepStmtCacheSqlLimit=2048
jdbc.sqllite.useServerPrepStmts=true
jdbc.sqllite.scan.mapper.package=io.edurt.gcm.sqllite.mapper
```

|Name|Description|Default|
|---|---|---|
|jdbc.sqllite.url|sqllite connection server connection string, need to carry database information|`jdbc:sqlite:test`|
|jdbc.sqllite.username|User name to connect to sqllite service|`root`|
|jdbc.sqllite.password|Password to connect to sqllite service|`123`|
|jdbc.sqllite.minimumIdle|-|`10`|
|jdbc.sqllite.maximumPoolSize|-|`100`|
|jdbc.sqllite.connectionTestQuery|-|`select 1`|
|jdbc.sqllite.cachePrepStmts|-|`true`|
|jdbc.sqllite.prepStmtCacheSize|-|`250`|
|jdbc.sqllite.prepStmtCacheSqlLimit|-|`2048`|
|jdbc.sqllite.useServerPrepStmts|-|`true`|
|jdbc.sqllite.scan.mapper.package|Scan the mapper file for the specified source directory|`io.edurt.gcm.sqllite.mapper`|

### Example

```java 
String path = "/etc/conf/catalog/sqllite.properties";
Injector injector = Guice.createInjector(new sqlliteModule(path));
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/sqllite.properties`
{: .explainer}

### Available annotation

- `@SQLLiteSource`: It is used to distinguish the data sources used in the case of multiple sources
