# 🗝️ spring-dotenv 
![CI](https://github.com/paulschwarz/spring-dotenv/workflows/CI/badge.svg)
![CD](https://github.com/paulschwarz/spring-dotenv/workflows/CD/badge.svg)
![Bintray](https://img.shields.io/bintray/v/paulschwarz/maven/spring-dotenv?color=blue) 
![Maven Central](https://img.shields.io/maven-central/v/me.paulschwarz/spring-dotenv?color=blue)
![GitHub](https://img.shields.io/github/license/paulschwarz/spring-dotenv?color=orange)
![GitHub stars](https://img.shields.io/github/stars/paulschwarz/spring-dotenv?color=yellowgreen)

<a href='https://bintray.com/paulschwarz/maven/spring-dotenv?source=watch'>
  <img alt='Get automatic notifications about new "spring-dotenv" versions' src='https://www.bintray.com/docs/images/bintray_badge_bw.png'>
</a>

Provides a Spring [PropertySource](https://github.com/spring-projects/spring-framework/blob/v5.2.3.RELEASE/spring-core/src/main/java/org/springframework/core/env/PropertySource.java) that delegates to the excellent [java-dotenv](https://github.com/cdimascio/java-dotenv) library.

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

[Installation instructions here https://bintray.com/paulschwarz/maven/spring-dotenv](https://bintray.com/paulschwarz/maven/spring-dotenv)

## Usage

Refer to the [demo application](application).

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

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](LICENSE)

## Acknowledgements

[Laravel Configuration](https://laravel.com/docs/master/configuration) for a great example of integrating dotenv with a framework.  
The dotenv libraries for [Ruby](https://github.com/bkeepers/dotenv) and [Java](https://github.com/cdimascio/java-dotenv).    
Spring Boot's [RandomValuePropertySource](https://github.com/spring-projects/spring-boot/blob/v2.2.4.RELEASE/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/env/RandomValuePropertySource.java) for an example of a custom property source.  
[Michał Bychawski](https://www.linkedin.com/in/michał-bychawski-541733aa) for help putting this together.  
