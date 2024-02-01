FROM maven:3.8.4-openjdk-23-slim AS build
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download dependencies (this step can be cached if the pom.xml file hasn't changed)
RUN mvn dependency:go-offline

# Copy the application source code
COPY src src

# Build the application
RUN mvn package

# Use a smaller base image for the runtime
FROM openjdk:23-slim
WORKDIR /app

# Copy the JAR file from the build stage to the runtime image
COPY --from=build /app/target/StarSystemTravelTimeService-0.0.1-SNAPSHOT.jar .

# Expose port 8080
EXPOSE 8080

# The command to run the application when the container starts
CMD ["java", "-jar", "StarSystemTravelTimeService-0.0.1-SNAPSHOT.jar"]
