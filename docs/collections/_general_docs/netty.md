---
title: Netty
category: General
order: 5
---

Netty component is used to inject configuration into Guice container. After the configuration is loaded, it can be used globally.

### Feature

- Support custom path
- more [Netty changes](/2021/01/22/v1.0.5/#netty)

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-netty</artifactId>
    <version>[1.0.3, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure the Netty connector, create a catalog properties file in `conf/catalog/netty.properties` named, for example, netty.properties, to mount the Netty connector as the netty catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

```java 
netty.host=localhost
netty.port=8080
netty.controller.package=io.edurt.gcm.netty.controller
netty.router.print=true
netty.view.path=classpath:/template/
netty.view.suffix=.html
```

|Name|Description|Default|
|---|---|---|
|netty.host|Local address bound after service startup|`localhost`|
|netty.port|Local port bound after service startup|`8080`|
|netty.controller.package|Controller package scan path|`io.edurt.gcm.netty.controller`|
|netty.router.print|Print system route list|`false`|
|netty.view.path|Resolve the path address of the view|`classpath:/template/`|
|netty.view.suffix|View suffix|`.html`|

### Example

```java 
Injector injector = Guice.createInjector(new NettyModule());
final GcmNettyApplication server = injector.getInstance(GcmNettyApplication.class);
server.start();
```

Unlike other components, the netty component needs to build a service process.

Controller interface example:

```java 
package io.edurt.gcm.netty.controller;

import io.edurt.gcm.netty.annotation.RequestMapping;
import io.edurt.gcm.netty.annotation.RequestParam;
import io.edurt.gcm.netty.annotation.ResponseBody;
import io.edurt.gcm.netty.annotation.RestController;
import io.edurt.gcm.netty.type.RequestMethod;

@RestController
public class TestController
{
    @ResponseBody
    @RequestMapping(value = {"/api/test", "/api/test1"}, method = {RequestMethod.POST, RequestMethod.GET})
    public Object println(@RequestParam("message") String message)
    {
        return message;
    }
}
```

We need to use @RestController and @RequestMapping, @RequestParam combination annotation for remote API injection.

> Warning: `path` The absolute path of the configuration file
{: .explainer}

### Available annotation

- `@RestController`: Used to mark whether the class is injected into the system as an interface provider
- `@RequestMapping`: The access path used to mark the interface address
- `@RequestBody`: Tag data request format, generally JSON
- `@RequestParam`: Access parameters, such as `?name=12&title=13`
- `@Controller`: Used to mark the view type interface
