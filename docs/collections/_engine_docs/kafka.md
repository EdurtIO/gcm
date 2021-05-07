---
title: Kafka
category: Engine
order: 2
---

Kafka component is used to inject Kafka into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support Kafka version for all
- Support custom configuration
- Provide independent clients
- Support automatic release of resource connections

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm.engine</groupId>
    <artifactId>gcm-engine-kafka</artifactId>
    <version>[1.2.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Kafka connector, create a catalog properties file in `conf/catalog/kafka.properties` named, for example, kafka.properties, to mount the kafka connector as the kafka catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
component.kafka.servers=localhost:9092
component.kafka.ack=all
component.kafka.retries=0
component.kafka.batch.size=16384
component.kafka.linger.ms=1
component.kafka.buffer.memory=33554432
component.kafka.key.serializer=org.apache.kafka.common.serialization.StringSerializer
component.kafka.value.serializer=org.apache.kafka.common.serialization.StringSerializer
```

### Example

```java 
String path = "/etc/conf/catalog/kafka.properties";
Injector injector = Guice.createInjector(new KafkaModule(path));

@Inject
private KafkaProduceClient kafkaProduceClient;

kafkaProduceClient.sendMessage("test", "String");
```

> Warning: `path` The absolute path of the configuration file, usually `conf/catalog/kafka.properties`
{: .explainer}
