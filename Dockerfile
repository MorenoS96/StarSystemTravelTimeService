# Use the official OpenJDK base image
FROM openjdk:23-slim

# Set the working directory inside the container
WORKDIR /app


# Copy the compiled JAR file into the container at /app
COPY target/StarSystemTravelTimeService-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar"]
