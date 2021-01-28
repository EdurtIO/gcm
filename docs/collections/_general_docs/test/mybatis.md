---
title: MyBatis
category: Test
order: 5
---

MyBatis Test framework is used to inject test case into Guice container, JUnit runtime environment is the interface extension of JUnit to other framework test environment.  After the configuration is loaded, it can be used globally.

### Feature

- Support database rollback operation

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
such as,the `TestMyBatisModule` is our testing class.

```java 
@RunWith(value = MyBatisRunner.class)
@JunitModuleLoader(value = {TestMybatisModule.class})
@MapperClasses(value = {TestMapper.class})
```

### Example

```java 
@RunWith(value = MyBatisRunner.class)
@JunitModuleLoader(value = {TestMybatisModule.class})
@MapperClasses(value = {TestMapper.class})
public class TestMyBatisRunner
{
    @Inject
    private TestMapper mapper;

    @Test
    public void findAll()
    {
        this.mapper.findAll().forEach(v -> System.out.println(v));
    }

    @Test
    public void save()
    {
        Assert.assertTrue(this.mapper.save() == 2);
    }
}
```

`@MapperClasses`: Mapper test class for marking scan loads

> Hint: MyBatisRunner class can scan all classes to ensure the validity of user-defined annotation
{: .explainer}
