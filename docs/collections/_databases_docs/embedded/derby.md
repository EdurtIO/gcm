---
title: Derby
category: Embedded
order: 1
---

Derby component is used to inject Derby into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support Derby version for 10.x

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-derby</artifactId>
    <version>[1.1.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Derby connector, create a catalog properties file in `conf/catalog/derby.properties` named, for example, derby.properties, to mount the Derby connector as the Derby catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
jdbc.derby.driverClassName=org.apache.derby.jdbc.EmbeddedDriver
jdbc.derby.url=jdbc:derby:derbyDB;create=true
jdbc.derby.username=root
jdbc.derby.password=123
jdbc.derby.minimumIdle=10
jdbc.derby.maximumPoolSize=100
jdbc.derby.connectionTestQuery=select 1
jdbc.derby.cachePrepStmts=true
jdbc.derby.prepStmtCacheSize=250
jdbc.derby.prepStmtCacheSqlLimit=2048
jdbc.derby.useServerPrepStmts=true
jdbc.derby.scan.mapper.package=io.edurt.gcm.derby.mapper
```

|Name|Description|Default|
|---|---|---|
|jdbc.derby.url|derby connection server connection string, need to carry database information|`jdbc:derby:derbyDB;create=true`|
|jdbc.derby.username|User name to connect to derby service|`root`|
|jdbc.derby.password|Password to connect to derby service|`123`|
|jdbc.derby.minimumIdle|-|`10`|
|jdbc.derby.maximumPoolSize|-|`100`|
|jdbc.derby.connectionTestQuery|-|`select 1`|
|jdbc.derby.cachePrepStmts|-|`true`|
|jdbc.derby.prepStmtCacheSize|-|`250`|
|jdbc.derby.prepStmtCacheSqlLimit|-|`2048`|
|jdbc.derby.useServerPrepStmts|-|`true`|
|jdbc.derby.scan.mapper.package|Scan the mapper file for the specified source directory|`io.edurt.gcm.derby.mapper`|

### Example

```java 
String path = "/etc/conf/catalog/derby.properties";
Injector injector = Guice.createInjector(new DerbyModule(path));
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/derby.properties`
{: .explainer}

### Available annotation

- `@DerbySource`: It is used to distinguish the data sources used in the case of multiple sources

