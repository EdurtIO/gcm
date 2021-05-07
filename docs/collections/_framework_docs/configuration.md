---
title: Configuration
category: Framework
order: 2
---

Configuration component is used to inject configuration into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support custom path

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-configuration</artifactId>
    <version>[1.0.0, )</version>
</dependency>
```

> Warning:  >= 1.2.0 has been replaced with the following usage
{: .explainer}

```xml

<dependency>
    <groupId>io.edurt.gcm.framework</groupId>
    <artifactId>gcm-framework-configuration</artifactId>
    <version>[1.2.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure, create a module, replacing file path as appropriate for your setup:

```java 
String path = "/etc/conf/catalog/mysql.properties";
Guice.createInjector(new ConfigurationModule(path));
```

### Example

```java 
String path = "/etc/conf/catalog/mysql.properties";
Injector injector = Guice.createInjector(new ConfigurationModule(path));
```

> Warning: `path` The absolute path of the configuration file
{: .explainer}
