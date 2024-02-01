# Use the official OpenJDK 17 base image
FROM openjdk:17.0.2-jdk
WORKDIR /app
#RUN ./mvnw clean install
# Copy the current directory contents into the container at /app
COPY . /app

# Build the Spring Boot application


# Expose port 8080
EXPOSE 8080

# The command to run the application when the container starts
CMD ["java", "-jar", "target/StarSystemTravelTimeService-0.0.1-SNAPSHOT.jar"]
