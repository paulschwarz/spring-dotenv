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
- Keep production behavior unchanged (real environment variables always win)
- Follow [The Twelve-Factor App](https://12factor.net/config) principle of
  *configuration via environment*

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
    <artifactId>springboot3-dotenv</artifactId>
</dependency>
</dependencies>
```

#### Gradle

```kotlin
dependencies {
    implementation(platform("me.paulschwarz:spring-dotenv-bom:$version"))
    implementation("me.paulschwarz:springboot3-dotenv")
}
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

---

## Configuration (optional)

All configuration is optional. Defaults are sensible.

| System property                         | Environment variable                       | Default value |
|-----------------------------------------|--------------------------------------------|---------------|
| `springdotenv.enabled`                  | `SPRINGDOTENV_ENABLED`                     | `true`        |
| `springdotenv.directory`                | `SPRINGDOTENV_DIRECTORY`                   |               |
| `springdotenv.filename`                 | `SPRINGDOTENV_FILENAME`                    | `.env`        |
| `springdotenv.ignoreIfMalformed`        | `SPRINGDOTENV_IGNORE_IF_MALFORMED`         | `false`       |
| `springdotenv.ignoreIfMissing`          | `SPRINGDOTENV_IGNORE_IF_MISSING`           | `true`        |
| `springdotenv.exportToSystemProperties` | `SPRINGDOTENV_EXPORT_TO_SYSTEM_PROPERTIES` | `false`       |

### Notable options

- `springdotenv.enabled`  
  Defaults to `true`. Set to `false` to completely disable integration.
- `springdotenv.exportToSystemProperties` If enabled, variables loaded from `.env` are also exported to
`System.getProperties()`.
- `springdotenv.ignoreIfMissing` Defaults to `true`. Set to `false` to fail fast when `.env` is absent.

---

### Notes on prefixes

Older versions required properties to be prefixed (for example `env.EXAMPLE_NAME`).

This is no longer the case. Prefixes have been removed.

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
