## Spring example

## Build and Running

Set the release version as you wish (if you are creating a new release) or based on the latest version in GitHub.

```
export RELEASE_VERSION=0.0.0 
../../gradlew build
java -cp build/libs/spring-app-example-$RELEASE_VERSION-all.jar examples.SpringAppDemoApplication
```