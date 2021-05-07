---
title: Redis
category: Engine
order: 2
---

Redis component is used to inject Redis into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support Redis version for all
- Support custom configuration
- Provide independent clients
- Support automatic release of resource connections

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-redis</artifactId>
    <version>[1.0.0, )</version>
</dependency>
```

> Warning:  >= 1.2.0 has been replaced with the following usage
{: .explainer}

```xml

<dependency>
    <groupId>io.edurt.gcm.engine</groupId>
    <artifactId>gcm-engine-redis</artifactId>
    <version>[1.2.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Redis connector, create a catalog properties file in `conf/catalog/redis.properties` named, for example, redis.properties, to mount the Redis connector as the redis catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
redis.database=0
redis.host=localhost
redis.port=6379
redis.password=
redis.pool.max-active=10
redis.pool.max-idle=10
redis.pool.max-wait=10
redis.pool.min-idle=10
redis.timeout=60
```

|Name|Description|Default|
|---|---|---|
|redis.database|Connect to the database specified by the Redis server|`0`|
|redis.host|Connect to the Redis Server host or ip|`localhost`|
|redis.port|Port to connect to redis service|`6379`|
|redis.password|Password to connect to redis service|` `|
|redis.pool.max-active|The maximum number of connections to the Redis server|`10`|
|redis.pool.max-idle|The maximum number of free connections to the Redis server|`10`|
|redis.pool.max-wait|Maximum number of waits to connect to the Redis server|`10`|
|redis.pool.min-idle|Minimum number of free connections to the Redis server|`10`|
|redis.timeout|The timeout for connection to the Redis server is in seconds|`60`|

### Example

```java 
String path = "/etc/conf/catalog/redis.properties";
Injector injector = Guice.createInjector(new ConfigurationModule(path), new RedisModule());

@Inject
private RedisClient client;

client.hashGetAll("redis-key");
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/redis.properties`
{: .explainer}
