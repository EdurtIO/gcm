# gcm(incubator)

Google Guice component management System!

gcm is an acronym for Guice Component Management, which is used to simplify various service components based on the Guice container.
It allows you to quickly use certain components without having to write the logic of certain components.
It is just a set of Bridges integrated into the Guice container, and the specific business implementation needs to be customized by users.
It's easy to get started, it's quick, and it doesn't have too much redundancy, so you can inject it into an existing Guice container anytime, anywhere.
Come and experience it!

### Status

[![GitHub license](https://img.shields.io/github/license/EdurtIO/incubator-gcm?label=license&style=for-the-badge)](https://github.com/EdurtIO/incubator-gcm/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/EdurtIO/incubator-gcm?label=GitHub%20Issues&style=for-the-badge)](https://github.com/EdurtIO/incubator-gcm/issues)
[![GitHub forks](https://img.shields.io/github/forks/EdurtIO/incubator-gcm?label=github%20forks&style=for-the-badge)](https://github.com/EdurtIO/incubator-gcm/network)
[![GitHub stars](https://img.shields.io/github/stars/EdurtIO/incubator-gcm?label=github%20stars&style=for-the-badge)](https://github.com/EdurtIO/incubator-gcm/stargazers)

[![Fork me on Gitee](https://gitee.com/EdurtIO/incubator-gcm/widgets/widget_4.svg)](https://gitee.com/EdurtIO/incubator-gcm)
[Fork me on GitHub](https://gitee.com/EdurtIO/incubator-gcm)

### How to compile?

---

use shell script:

```shell
./mvnw clean install package
```

if you skip test, please run:

```shell
./mvnw clean install package -DskipTests
```

check that the code is formatted correctly, please run:

```shell
./mvnw clean install checkstyle:check -DskipTests
```

check the code for code-level bugs, please run:

```shell
./mvnw clean install findbugs:check -DskipTests
```

### How to use code style by IntelliJ IDEA?

---

- Open IntelliJ IDEA to select the top `Preferences..` or use the shortcut `Command + ,`
- Select `Editor` --> `Code Style` in turn
- Select `Scheme` --> `Import Scheme` --> `IntelliJ IDEA code style XML`
- Select the `idea-code-style.xml` file in the project root directory
