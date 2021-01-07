# gcm(incubator)

Google Guice component management System!

### Support Module

- [x] Configuration
- [x] MySQL
- [x] Redis

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
