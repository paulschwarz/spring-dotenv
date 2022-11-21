# 🗝 spring-dotenv 
[![CI](https://github.com/paulschwarz/spring-dotenv/workflows/CI/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions?query=workflow%3ACI)
[![CD](https://github.com/paulschwarz/spring-dotenv/workflows/CD/badge.svg)](https://github.com/paulschwarz/spring-dotenv/actions?query=workflow%3ACD)
[![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/spring-dotenv?color=blue)](https://search.maven.org/artifact/me.paulschwarz/spring-dotenv)
[![GitHub](https://img.shields.io/github/license/paulschwarz/spring-dotenv?color=orange)](https://github.com/paulschwarz/spring-dotenv/blob/master/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/paulschwarz/spring-dotenv?color=yellowgreen)](https://github.com/paulschwarz/spring-dotenv/stargazers)

Provides a Spring [PropertySource](https://github.com/spring-projects/spring-framework/blob/v5.2.3.RELEASE/spring-core/src/main/java/org/springframework/core/env/PropertySource.java) that delegates to the excellent [dotenv-java](https://github.com/cdimascio/dotenv-java) library.

Storing [configuration in the environment](http://12factor.net/config) is one of the tenets of a [twelve-factor app](http://12factor.net). Anything that is likely to change between deployment environments – such as resource handles for databases or credentials for external services – should be extracted from the code into environment variables.

It is not always practical to set environment variables on development machines or continuous integration servers. Dotenv loads variables from a .env file for your convenience during development.

## Installation

### ... but first!

Add this to .gitignore

```gitignore
### Local Configuration ###
.env
```

Loading environment variables from the .env is for your development convenience and the file should not be committed to source control.

It is common, however, to commit a .env.example file to source control which documents the available variables and gives developers an understanding of how to create their own local .env file.

#### Maven and Gradle

[*Installation instructions*](https://github.com/paulschwarz/spring-dotenv/releases/latest)
    
## Usage

Refer to the [demo application](examples/application).

Imagine a simple application which outputs "Hello, `example.name`". We can use Spring to load the property `example.name` from an application.properties file or an application.yml file.

Now imagine we want to extract that `example.name` so that it is loaded from the environment instead of hard-coded into application.properties.

If a value for `example.name` is set in the environment, we certainly want to use that value. However, for your convenience during development, you may declare that value in a .env file and it will be loaded from there. It is important to understand the precedence; a variable set in the environment will always override a value set in the .env file.  

Spring provides different ways to read properties. This example uses the `@Value` annotation, but feel free to use whichever ways suit your case.

```java
@Value("${example.name}")
String name;
```

With Spring, you may provide properties in a .properties file or a .yml file.

Notice the reference to `env` here. This is the property source which invokes the dotenv library to load values from the environment and .env file.

Add to application.yml

```yaml
example:
  name: ${env.EXAMPLE_NAME}
```

or with a default value of "World"

```yaml
example:
  name: ${env.EXAMPLE_NAME:World}
```

And of course, a .properties file works too

```properties
example.name = ${env.EXAMPLE_NAME}
```

At this point, we've told Spring to load the value `example.name` from the environment. It must be supplied, otherwise you will get an exception.

Now create a .env file

```properties
EXAMPLE_NAME=Paul
```

This file is *yours* and does not belong in source control. Its entire purpose is to allow you to set environment variables during development.

Now set the variable in the environment and notice that it has higher precedence than the value set in the .env file.

```bash
export EXAMPLE_NAME=World
```

## Configuration (optional)

This library supports the same configuration values as the underlying [dotenv-java configuration](https://github.com/cdimascio/dotenv-java#configuration-options). Configuration is completely optional, however if you need to override defaults, you may do so by adding the following to your application.yml (or .properties):

```yaml
.env:
  directory: <string>
  filename: <string>
  ignoreIfMalformed: <boolean>
  ignoreIfMissing: <boolean>
  systemProperties: <boolean>
  prefix: <string>
```

By default, this library sets `ignoreIfMissing` to `true`. You may change this behaviour as follows:

```yaml
.env:
  ignoreIfMissing: false
```

This library expects properties to be prefixed with `env.` as follows. However, you may configure a different prefix.

```yaml
.env:
  prefix: ""

example:
  name: ${EXAMPLE_NAME:World}
```

If you prefer .properties files:

```properties
.env.directory: <string>
.env.filename: <string>
.env.ignoreIfMalformed: <boolean>
.env.ignoreIfMissing: <boolean>
.env.systemProperties: <boolean>
.env.prefix: <string>
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
