# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./


RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

RUN ./mvnw clean package
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]