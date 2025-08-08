# Use OpenJDK 24 as the base image
FROM openjdk:24-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY build/libs/*.jar devices-system.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "devices-system.jar"]