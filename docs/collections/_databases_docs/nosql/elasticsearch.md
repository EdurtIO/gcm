---
title: Elasticsearch
category: NoSQL
order: 3
---

Elasticsearch component is used to inject Elasticsearch into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support Elasticsearch version for 5.x
- Support custom configuration

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-elasticsearch</artifactId>
    <version>[1.0.9, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Elasticsearch connector, create a catalog properties file in `conf/catalog/elasticsearch.properties` named, for example, elasticsearch.properties, to mount the Elasticsearch connector as the elasticsearch catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
component.elasticsearch.urls=localhost:9200,localhost:9300
component.elasticsearch.scheme=http
```

|Name|Description|Default|
|---|---|---|
|component.elasticsearch.urls|Connect to the database specified by the Elasticsearch server,Multiple address use`,`split. Each address structure is host `address:port`|`localhost:9200,localhost:9300`|
|component.elasticsearch.scheme|Protocol used to connect Elasticsearch service|`http`|

### Example

```java 
String path = "/etc/conf/catalog/elasticsearch.properties";
Injector injector = Guice.createInjector(new ElasticsearchModule(path));

@Inject
private elasticsearch client;
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/elasticsearch.properties`
{: .explainer}
