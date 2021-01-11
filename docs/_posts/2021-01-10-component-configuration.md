---
date: 2021-01-10
title: Configuration Component
description: Gcm for Configuration Component
categories:
  - Components
resources:
  - name: Source code
    link: https://github.com/EdurtIO/incubator-gcm/tree/master/configuration
type: Document
order_number: 1
---

Configuration is used to load the system configuration into Guice container. After the configuration is loaded, it can be used globally.

## Feature

- Support custom path

## How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-configuration</artifactId>
    <version>[1.0.2, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

Example:

```java 
String path = "config";
Injector injector = Guice.createInjector(new ConfigurationModule(path));
```

> Warning: `path` Absolute path
