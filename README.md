# 🌱🔐 Spring-Dotenv

[![CI](https://github.com/paulschwarz/spring-dotenv/actions/workflows/ci.yml/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions/workflows/ci.yml)
[![Release](https://github.com/paulschwarz/spring-dotenv/actions/workflows/release.yml/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions/workflows/release.yml)

[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/spring-dotenv?label=spring-dotenv)](https://search.maven.org/artifact/me.paulschwarz/spring-dotenv)
[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/springboot3-dotenv?label=springboot3-dotenv)](https://search.maven.org/artifact/me.paulschwarz/springboot3-dotenv)
[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/springboot4-dotenv?label=springboot4-dotenv)](https://search.maven.org/artifact/me.paulschwarz/springboot4-dotenv)

[![GitHub](https://img.shields.io/github/license/paulschwarz/spring-dotenv)](https://github.com/paulschwarz/spring-dotenv/blob/main/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/paulschwarz/spring-dotenv)](https://github.com/paulschwarz/spring-dotenv/stargazers)

---

## What this library does

**Spring-Dotenv** integrates the excellent
[dotenv-java](https://github.com/cdimascio/dotenv-java) library into the Spring
ecosystem by exposing `.env` files as a Spring
[`PropertySource`](https://docs.spring.io/spring-framework/reference/core/beans/environment.html).

It lets you:

- Load environment variables from a local `.env` file during development
- Keep production behavior unchanged (real environment variables always win because `.env` values are added after system environment variables, preserving standard Spring precedence)
- Follow [The Twelve-Factor App](https://12factor.net/config) principle of *configuration via environment*

The `.env` file is strictly a **development convenience**. It should never be
committed to source control.

📄 See the [CHANGELOG.md](CHANGELOG.md) for breaking changes, upgrades, and release notes.

---

## Requirements

- **Java 17 or newer**
- Spring Framework or Spring Boot 3 / 4

If you need Java 8 or 11 support, use an older release.

---

## Modules

This project is published as multiple artifacts. Choose **exactly one** at runtime.

- **`spring-dotenv`**  
  Core library for Spring Framework (no Spring Boot auto-integration)

- **`springboot3-dotenv`**  
  Auto-integrates with Spring Boot 3

- **`springboot4-dotenv`**  
  Auto-integrates with Spring Boot 4

A BOM is provided to keep versions aligned across modules.

---

## Installation

### Recommended (BOM-based)

#### Gradle

```kotlin
dependencies {
    developmentOnly(platform("me.paulschwarz:spring-dotenv-bom:$version"))
    developmentOnly("me.paulschwarz:springboot4-dotenv")
}
```

#### Maven

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>me.paulschwarz</groupId>
            <artifactId>spring-dotenv-bom</artifactId>
            <version>$version</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>me.paulschwarz</groupId>
        <artifactId>springboot4-dotenv</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## First-time set up

Add `.env` to your `.gitignore`:

```gitignore
.env
```

It’s common to commit a `.env.example` file instead, documenting the available
variables without leaking secrets.

---

## Usage

Assume a simple Spring application that needs a configurable value:

```java

@Value("${example.name}")
String name;
```

Declare the property in `application.yml`:

```yaml
example:
  name: ${EXAMPLE_NAME:World}
```

Create a local `.env` file:

```dotenv
EXAMPLE_NAME=Paul
```

At runtime:

- If `EXAMPLE_NAME` is set in the real environment, **that value wins**
- Otherwise, the value from `.env` is used
- If neither is present, the default (`World`) applies

This preserves production safety while making local development frictionless.

Keys loaded from `.env` participate in relaxed name resolution **when running under Spring Boot (3 or 4)**.
In plain Spring Framework applications, `.env` keys are resolved **strictly**.

For example, under Spring Boot:

```dotenv
MY_SERVICE_URL=https://example.com
```

can be accessed as:

```properties
my.service.url
```

---

## Configuration (optional)

All configuration is optional. Defaults are sensible.

### Spring Framework vs Spring Boot

- **Spring Boot 3 / 4**
  - Relaxed binding is applied to:
    - `springdotenv.*` configuration keys
    - variables defined inside `.env` files
  - Standard Spring Boot rules apply (kebab-case, camelCase, uppercase environment variables).

- **Plain Spring Framework**
  - Configuration is **strict**:
    - `springdotenv.*` keys must match exactly
    - `.env` entries must match exactly
  - No relaxed binding is applied.

| System property                         | Environment variable                       | Default value |
|-----------------------------------------|--------------------------------------------|---------------|
| `springdotenv.enabled`                  | `SPRINGDOTENV_ENABLED`                     | `true`        |
| `springdotenv.directory`                | `SPRINGDOTENV_DIRECTORY`                   |               |
| `springdotenv.filename`                 | `SPRINGDOTENV_FILENAME`                    | `.env`        |
| `springdotenv.ignoreIfMalformed`        | `SPRINGDOTENV_IGNORE_IF_MALFORMED`         | `false`       |
| `springdotenv.ignoreIfMissing`          | `SPRINGDOTENV_IGNORE_IF_MISSING`           | `true`        |
| `springdotenv.exportToSystemProperties` | `SPRINGDOTENV_EXPORT_TO_SYSTEM_PROPERTIES` | `false`       |

⚠️ **Binding semantics note**  
The keys shown above support relaxed binding **only in Spring Boot applications**.  
When using the core `spring-dotenv` module without Spring Boot, these keys must be specified exactly.

### Notable options

- `springdotenv.enabled`  
  Defaults to `true`. Set to `false` to completely disable integration.

- `springdotenv.exportToSystemProperties`  
  If enabled, variables loaded from `.env` are also exported to `System.getProperties()`.  
  This field was previously known as `systemProperties` but was renamed for clarity. 
  It is deprecated but still supported with warning.  
  If both the legacy and the canonical property are set, the canonical key (`springdotenv.exportToSystemProperties`) takes precedence.  
  When `exportToSystemProperties` is enabled, `.env` variables are exported verbatim. Relaxed name resolution is applied only when values are read via Spring’s Environment.

- `springdotenv.ignoreIfMissing`  
  Defaults to `true`. Set to `false` to fail fast when `.env` is absent.

---

### Notes on prefixes

Older versions required properties to be prefixed (for example `env.EXAMPLE_NAME`).

This is no longer the case. Prefixes have been removed.

---

### Configuration binding semantics

Spring-Dotenv follows the configuration rules of the runtime it integrates with.

#### Spring Boot applications

When used with **Spring Boot 3 or 4**, all `springdotenv.*` configuration options **and `.env` file entries** participate in **standard Spring Boot relaxed binding**.

This means configuration keys are interchangeable across common naming styles:

- `springdotenv.ignore-if-missing` 
- `springdotenv.ignoreIfMissing` 
- `SPRINGDOTENV_IGNORE_IF_MISSING`

Likewise, variables defined in `.env` files are resolved using the same relaxed rules when accessed through the Spring `Environment`. For example:

```dotenv
MY_SERVICE_URL=https://example.com
```

can be consumed as:

```properties
my.service.url
```

This behavior is delegated to Spring Boot’s native `Binder` and matches Boot’s built-in configuration semantics.

#### Spring Framework (non-Boot) applications

When using the **core** `spring-dotenv` **module without Spring Boot**, all configuration is **strict**:

- `springdotenv.*` keys must match exactly  
- `.env` entries are resolved by exact name only  
- No relaxed binding or env-var style name conversion is applied

This distinction is intentional and mirrors the behavior of Spring Framework itself.

---

## Building locally

```shell
./gradlew build
```

---

## Contributing

Pull requests are welcome.

For substantial changes, please open an issue first to discuss scope and approach.
Tests are expected for behavioral changes.

---

## License

MIT © Paul Schwarz
