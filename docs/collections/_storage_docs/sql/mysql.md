---
title: MySQL(Hikaricp)
category: Sql
order: 1
---

MySQL component is used to inject MySQL into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support MySQL version for 5.x

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-mysql</artifactId>
    <version>[1.0.0, )</version>
</dependency>
```

> Warning:  >= 1.2.0 has been replaced with the following usage
{: .explainer}

```xml

<dependency>
    <groupId>io.edurt.gcm.storage</groupId>
    <artifactId>gcm-storage-mysql</artifactId>
    <version>[1.2.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the MySQL connector, create a catalog properties file in `conf/catalog/mysql.properties` named, for example, mysql.properties, to mount the MySQL connector as the mysql catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
jdbc.mysql.driverClassName=com.mysql.jdbc.Driver
jdbc.mysql.url=jdbc:mysql://localhost:3306/test
jdbc.mysql.username=root
jdbc.mysql.password=123
jdbc.mysql.minimumIdle=10
jdbc.mysql.maximumPoolSize=100
jdbc.mysql.connectionTestQuery=select 1
jdbc.mysql.cachePrepStmts=true
jdbc.mysql.prepStmtCacheSize=250
jdbc.mysql.prepStmtCacheSqlLimit=2048
jdbc.mysql.useServerPrepStmts=true
```

|Name|Description|Default|
|---|---|---|
|jdbc.mysql.url|MySQL connection server connection string, need to carry database information|`jdbc:mysql://localhost:3306/test`|
|jdbc.mysql.username|User name to connect to MySQL service|`root`|
|jdbc.mysql.password|Password to connect to MySQL service|`123`|
|jdbc.mysql.minimumIdle|-|`10`|
|jdbc.mysql.maximumPoolSize|-|`100`|
|jdbc.mysql.connectionTestQuery|-|`select 1`|
|jdbc.mysql.cachePrepStmts|-|`true`|
|jdbc.mysql.prepStmtCacheSize|-|`250`|
|jdbc.mysql.prepStmtCacheSqlLimit|-|`2048`|
|jdbc.mysql.useServerPrepStmts|-|`true`|
|jdbc.mysql.scan.mapper.package|Scan the mapper file for the specified source directory|`io.edurt.gcm.mysql.hikari.mapper`|

### Example

```java 
String path = "/etc/conf/catalog/mysql.properties";
Injector injector = Guice.createInjector(new ConfigurationModule(path), new HikariMySQLModule());
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/mysql.properties`
{: .explainer}
