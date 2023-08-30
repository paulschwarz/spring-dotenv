# 🗝 spring-dotenv 
[![CI](https://github.com/paulschwarz/spring-dotenv/workflows/CI/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions?query=workflow%3ACI)
[![CD](https://github.com/paulschwarz/spring-dotenv/workflows/CD/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions?query=workflow%3ACD)
[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/spring-dotenv?color=blue)](https://search.maven.org/artifact/me.paulschwarz/spring-dotenv)
[![GitHub](https://img.shields.io/github/license/paulschwarz/spring-dotenv?color=orange)](https://github.com/paulschwarz/spring-dotenv/blob/master/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/paulschwarz/spring-dotenv?color=yellowgreen)](https://github.com/paulschwarz/spring-dotenv/stargazers)

# Breaking changes in version 3

- The `env.` prefix is no longer default. It is now expected that you provide no prefix in your configuration files.
- By default the .env contents and the contents of the system enviroment are loaded into Java system properties.
- The property source is loaded earlier into the Spring lifecycle allowing dotenv to be effective before the application context is ready.

Provides a Spring [PropertySource](https://github.com/spring-projects/spring-framework/blob/v5.2.3.RELEASE/spring-core/src/main/java/org/springframework/core/env/PropertySource.java) that delegates to the excellent [dotenv-java](https://github.com/cdimascio/dotenv-java) library.

Storing [configuration in the environment](http://12factor.net/config) is one of the tenets of a [twelve-factor app](http://12factor.net). Anything that is likely to change between deployment environments – such as resource handles for databases or credentials for external services – should be extracted from the code into environment variables.

It is not always practical to set environment variables on development machines or continuous integration servers. Dotenv loads variables from a .env file for your convenience during development.

## Installation

The current version requires JDK 11 or newer.
If you your project depends on an older JDK, use
[spring-dotenv 3.0.0](https://github.com/paulschwarz/spring-dotenv/releases/tag/v3.0.0).

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

Refer to the [demo applications](examples).

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

This library supports the same configuration values as the underlying [dotenv-java configuration](https://github.com/cdimascio/dotenv-java#configuration-options). Configuration is completely optional, however if you need to override defaults, you may do so by adding the following to **/src/main/resources/.env.properties**:

```properties
directory=<string>
filename=<string>
ignoreIfMalformed=<boolean>
ignoreIfMissing=<boolean>
systemProperties=<boolean>
prefix=<string>
```

`directory` can be practical when you are using mono repo, then you have to define the folder name with spring project. 

By default, this library sets `ignoreIfMissing` to `true`. You may change this behaviour as follows:

```properties
ignoreIfMissing=false
```

Prior to version 3, the library expected properties to be prefixed with `env.`. The default behaviour going forward is to not use a prefix. If you require a prefix, then you can provide one like this:


```properties
prefix=env.
```

## Building spring-dotenv

Inside a terminal at the project root directory, type the following command.
Set the release version as you wish (if you are creating a new release) or based on the latest version in GitHub.

```shell
RELEASE_VERSION=0.0.0 ./gradlew build
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](LICENSE)

## Acknowledgements

[Laravel Configuration](https://laravel.com/docs/master/configuration) for a great example of integrating dotenv with a framework.  
The dotenv libraries for [Ruby](https://github.com/bkeepers/dotenv) and [Java](https://github.com/cdimascio/dotenv-java).    
Spring Boot's [RandomValuePropertySource](https://github.com/spring-projects/spring-boot/blob/v2.2.4.RELEASE/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/env/RandomValuePropertySource.java) for an example of a custom property source.  
[Michał Bychawski](https://www.linkedin.com/in/michał-bychawski-541733aa) for help putting this together.  
[Dan Zheng](https://github.com/clevertension) for contributing sample applications.
