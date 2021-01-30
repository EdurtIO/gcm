---
title: Junit
category: Test
order: 5
---

Junit Test framework is used to inject test case into Guice container, JUnit runtime environment is the interface extension of JUnit to other framework test environment.  After the configuration is loaded, it can be used globally.

### Feature

- Support multiple module

### How to use

```xml
<dependency>
    <groupId>io.edurt.gcm</groupId>
    <artifactId>gcm-test</artifactId>
    <version>[1.5.0, )</version>
</dependency>
```

Add the above configuration information to the pom.xml Reload the download dependency information in the file.

### Configuration

To configure, create a test class, Add run class bound annoation, custom annotation and their binding related modules.
such as,the `TestJunit` is our testing class.

```java 
@RunWith(JunitRunner.class)
@JunitModuleLoader(value = {TestJunitModule.class})
```

### Example

```java 
@RunWith(JunitRunner.class) 
@JunitModuleLoader(value = {TestJunitModule.class})
public class TestJunit
{
    @Inject
    private TestService testService;
    private String message;
    @Test
    public void println(){
        this.testService.println(message);
    }
}
```

`@JunitModuleLoader`: It is used to mark scan and load predefined Guice test module

> Hint: JunitRunner class can scan all classes to ensure the validity of user-defined annotation
{: .explainer}
