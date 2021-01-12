---
title: MySQL(Hikaricp)
category: MySQL
order: 1
requirements:
    build: Any
    plan: Free
    hosting: Any
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
jdbc.mysql.dataSource.cachePrepStmts=true
jdbc.mysql.dataSource.prepStmtCacheSize=250
jdbc.mysql.dataSource.prepStmtCacheSqlLimit=2048
jdbc.mysql.dataSource.useServerPrepStmts=true
```

|Name|Description|Default|
|---|---|---|
|jdbc.mysql.url|MySQL connection server connection string, need to carry database information|`jdbc:mysql://localhost:3306/test`|
|jdbc.mysql.username|User name to connect to MySQL service|`root`|
|jdbc.mysql.password|Password to connect to MySQL service|`123`|
|jdbc.mysql.minimumIdle|-|`10`|
|jdbc.mysql.maximumPoolSize|-|`100`|
|jdbc.mysql.connectionTestQuery|-|`select 1`|
|jdbc.mysql.dataSource.cachePrepStmts|-|`true`|
|jdbc.mysql.dataSource.prepStmtCacheSize|-|`250`|
|jdbc.mysql.dataSource.prepStmtCacheSqlLimit|-|`2048`|
|jdbc.mysql.dataSource.useServerPrepStmts|-|`true`|

### Example

```java 
String path = "/etc/conf/catalog/mysql.properties";
Injector injector = Guice.createInjector(new ConfigurationModule(path), new HikariDataSourceModule());
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/mysql.properties`
{: .explainer}
