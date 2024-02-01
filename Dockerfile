# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the executable jar file and the application.properties file to the container
COPY target/StarSystemTravelTimeService-0.0.1-SNAPSHOT.jar /app/

# Set the command to run the Spring Boot application
CMD ["java", "-jar", "StarSystemTravelTimeService-0.0.1-SNAPSHOT.jar"]