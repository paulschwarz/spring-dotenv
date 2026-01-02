### Install

**Recommended:** use the BOM to keep versions aligned.

#### Gradle (BOM + choose one module)

```kotlin
dependencies {
    developmentOnly(platform("me.paulschwarz:spring-dotenv-bom:${VERSION}"))

    // Choose one:
    // developmentOnly("me.paulschwarz:spring-dotenv")
    // developmentOnly("me.paulschwarz:springboot3-dotenv")
    // developmentOnly("me.paulschwarz:springboot4-dotenv")
    developmentOnly("me.paulschwarz:springboot3-dotenv")
}
```

#### Maven (BOM + choose one module)

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>me.paulschwarz</groupId>
            <artifactId>spring-dotenv-bom</artifactId>
            <version>${VERSION}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Then add **one**:

- **Spring Framework (no Boot):** `me.paulschwarz:spring-dotenv`
- **Spring Boot 3:** `me.paulschwarz:springboot3-dotenv`
- **Spring Boot 4:** `me.paulschwarz:springboot4-dotenv`

```xml

<dependency>
    <groupId>me.paulschwarz</groupId>
    <artifactId>springboot3-dotenv</artifactId>
    <optional>true</optional>
</dependency>
```
