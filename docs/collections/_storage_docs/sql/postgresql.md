---
title: PostgreSQL(Hikaricp)
category: Sql
order: 2
---

PostgreSQL component is used to inject PostgreSQL into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support PostgreSQL version for 42.x

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-postgresql</artifactId>
    <version>[1.0.4, )</version>
</dependency>
```

> Warning:  >= 1.2.0 has been replaced with the following usage
{: .explainer}

```xml

<dependency>
    <groupId>io.edurt.gcm.storage</groupId>
    <artifactId>gcm-storage-postgresql</artifactId>
    <version>[1.2.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the PostgreSQL connector, create a catalog properties file in `conf/catalog/postgresql.properties` named, for example, postgresql.properties, to mount the PostgreSQL connector as the postgresql catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
jdbc.postgresql.driverClassName=org.postgresql.Driver
jdbc.postgresql.url=jdbc:postgresql://localhost:3306/test
jdbc.postgresql.username=root
jdbc.postgresql.password=123
jdbc.postgresql.minimumIdle=10
jdbc.postgresql.maximumPoolSize=100
jdbc.postgresql.connectionTestQuery=select 1
jdbc.postgresql.cachePrepStmts=true
jdbc.postgresql.prepStmtCacheSize=250
jdbc.postgresql.prepStmtCacheSqlLimit=2048
jdbc.postgresql.useServerPrepStmts=true
```

|Name|Description|Default|
|---|---|---|
|jdbc.postgresql.url|PostgreSQL connection server connection string, need to carry database information|`jdbc:postgresql://localhost:3306/test`|
|jdbc.postgresql.username|User name to connect to PostgreSQL service|`root`|
|jdbc.postgresql.password|Password to connect to PostgreSQL service|`123`|
|jdbc.postgresql.minimumIdle|-|`10`|
|jdbc.postgresql.maximumPoolSize|-|`100`|
|jdbc.postgresql.connectionTestQuery|-|`select 1`|
|jdbc.postgresql.cachePrepStmts|-|`true`|
|jdbc.postgresql.prepStmtCacheSize|-|`250`|
|jdbc.postgresql.prepStmtCacheSqlLimit|-|`2048`|
|jdbc.postgresql.useServerPrepStmts|-|`true`|
|jdbc.postgresql.scan.mapper.package|Scan the mapper file for the specified source directory|`io.edurt.gcm.postgresql.hikari.mapper`|

### Example

```java 
String path = "/etc/conf/catalog/postgresql.properties";
Injector injector = Guice.createInjector(new ConfigurationModule(path), new HikariPostgreSQLModule());
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/postgresql.properties`
{: .explainer}

### Available annotation

- `@PostgresSource`: It is used to distinguish the data sources used in the case of multiple sources

