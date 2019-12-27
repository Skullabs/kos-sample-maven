# Kos Sample using Maven 
A simple project written in Kotlin using Maven, so developers can use
it as a blueprint project. Although written in Kotlin, it should be
easy to convert it to Java.

This project contains:
- A simple REST API
- An in-memory repository (naively based on HashMap)
- An exception handler

## Requirements
This project was configured to use JDK 11 but can be easily downgrade
to JDK 8 with a bit of tweak in `pom.kts` file. In case you're not familiar
with Maven Polyglot syntax written in Kotlin Script, please check `sample.pom.xml`
which contains the very same content but written in XML.

## Running the App
```shell
$ ./mvnw clean package
$ java -jar target/application.jar
```
