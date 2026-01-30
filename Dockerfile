FROM eclipse-temurin:21-jdk-alpine
ADD target/demo1.jar demo1.jar
ENTRYPOINT ["java", "-jar", "demo1.jar"]