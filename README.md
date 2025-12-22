# 🗝 spring-dotenv

[![CI](https://github.com/paulschwarz/spring-dotenv/actions/workflows/ci.yml/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions/workflows/ci.yml)
[![Release](https://github.com/paulschwarz/spring-dotenv/actions/workflows/release.yml/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions/workflows/release.yml)

[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/spring-dotenv?label=spring-dotenv)](https://search.maven.org/artifact/me.paulschwarz/spring-dotenv)
[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/springboot3-dotenv?label=springboot3-dotenv)](https://search.maven.org/artifact/me.paulschwarz/springboot3-dotenv)
[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/springboot4-dotenv?label=springboot4-dotenv)](https://search.maven.org/artifact/me.paulschwarz/springboot4-dotenv)

[![GitHub](https://img.shields.io/github/license/paulschwarz/spring-dotenv)](https://github.com/paulschwarz/spring-dotenv/blob/main/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/paulschwarz/spring-dotenv)](https://github.com/paulschwarz/spring-dotenv/stargazers)

Provides a Spring [PropertySource](https://github.com/spring-projects/spring-framework/blob/main/spring-core/src/main/java/org/springframework/core/env/PropertySource.java) that delegates to the excellent [dotenv-java](https://github.com/cdimascio/dotenv-java/blob/master/README.md) library.

Storing [configuration in the environment](http://12factor.net/config) is one of the tenets of a [twelve-factor app](http://12factor.net). Anything that is likely to change between deployment environments – such as resource handles for databases or credentials for external services – should be extracted from the code into environment variables.

It is not always practical to set environment variables on development machines or continuous integration servers. Dotenv loads variables from a .env file for your convenience during development.

## Installation

The current version requires JDK 17 or newer.
If you your project depends on an older JDK, use 
[previous release](https://github.com/paulschwarz/spring-dotenv/releases).

### ... but first!

Add this to .gitignore

```gitignore
### Local Configuration ###
.env
```

Loading environment variables from the .env is for your development convenience and the file should not be committed to source control.

It is common, however, to commit a .env.example file to source control which documents the available variables and gives developers an understanding of how to create their own local .env file.

### Maven

```xml
<dependency>
  <groupId>me.paulschwarz</groupId>
  <artifactId>spring-dotenv</artifactId>
  <version>{version}</version>
</dependency>
```

### Gradle

```groovy
implementation "me.paulschwarz:spring-dotenv:${version}"
```

#### Maven and Gradle

[*Installation instructions*](https://github.com/paulschwarz/spring-dotenv/releases/latest)
    
## Usage

Imagine a simple application which outputs "Hello, `example.name`". We can use Spring to load the property `example.name` from an application.properties file or an application.yml file.

Now imagine we want to extract that `example.name` so that it is loaded from the environment instead of hard-coded into application.properties.

If a value for `example.name` is set in the environment, we certainly want to use that value. However, for your convenience during development, you may declare that value in a .env file and it will be loaded from there. It is important to understand the precedence; a variable set in the environment will always override a value set in the .env file.  

Spring provides different ways to read properties. This example uses the `@Value` annotation, but feel free to use whichever techniques suit your case.

```java
@Value("${example.name}")
String name;
```

With Spring, you may provide properties in a .properties file or a .yml file.

Add to application.yml

```yaml
example:
  name: ${EXAMPLE_NAME}
```

or with a default value of "World"

```yaml
example:
  name: ${EXAMPLE_NAME:World}
```

And of course, a .properties file works too

```properties
example.name = ${EXAMPLE_NAME:World}
```

At this point, we've told Spring to load the value `example.name` from the environment. It must be supplied, otherwise you will get an exception.

Now create a .env file

```properties
EXAMPLE_NAME=Paul
```

This file is *yours* and does not belong in source control. Its entire purpose is to allow you to set environment variables locally in your development environment.

Now set the variable in the environment and notice that it has higher precedence than the value set in the .env file.

```bash
export EXAMPLE_NAME=World
```

## Configuration (optional)

This library supports similar configuration options as the underlying [dotenv-java configuration](https://github.com/cdimascio/dotenv-java#configuration-options). Configuration is completely optional, however if you need to override defaults, you may do so by proving system properties:

```properties
springdotenv.enabled=<boolean>
springdotenv.directory=<string>
springdotenv.filename=<string>
springdotenv.ignoreIfMalformed=<boolean>
springdotenv.ignoreIfMissing=<boolean>
springdotenv.exportToSystemProperties=<boolean>
```

If you prefer to use environment variables, the equivalents are available as:

```properties
SPRINGDOTENV_ENABLED
SPRINGDOTENV_DIRECTORY
SPRINGDOTENV_FILENAME
SPRINGDOTENV_IGNORE_IF_MALFORMED
SPRINGDOTENV_IGNORE_IF_MISSING
SPRINGDOTENV_EXPORT_TO_SYSTEM_PROPERTIES
```

By default, this library sets `ignoreIfMissing` to `true`. You may change this behavior as follows:

```properties
springdotenv.ignoreIfMissing=false
```

## Building spring-dotenv

Inside a terminal at the project root directory, type the following command.

```shell
./gradlew build
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](LICENSE)
