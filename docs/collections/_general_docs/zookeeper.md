---
title: Zookeeper
category: General
order: 2
---

Zookeeper component is used to inject Zookeeper into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support custom configuration
- Provide independent clients
- Support automatic release of resource connections

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-zookeeper</artifactId>
    <version>[1.0.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Zookeeper connector, create a catalog properties file in `conf/catalog/zookeeper.properties` named, for example, zookeeper.properties, to mount the Redis connector as the zookeeper catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
zookeeper.service=localhost:2181
zookeeper.session-timeout=5000
zookeeper.connection-timeout=5000
zookeeper.retry=3
zookeeper.namespace=default
```

|Name|Description|Default|
|---|---|---|
|zookeeper.service|Connect to the database specified by the Zookeeper server, Multiple use, segmentation|`localhost:2181`|
|zookeeper.session-timeout|Session timeout, in milliseconds|`5000`|
|zookeeper.connection-timeout|Service connection timeout, in milliseconds|`5000`|
|zookeeper.retry|Service connection failure retries|`3`|
|zookeeper.namespace|Node default space|`default`|

### Example

```java 
String path = "/etc/conf/catalog/zookeeper.properties";
Injector injector = Guice.createInjector(new ConfigurationModule(path), new ZookeeperModule());

@Inject
private ZookeeperClient client;

client.createNode("zookeeper");
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/zookeeper.properties`
{: .explainer}
