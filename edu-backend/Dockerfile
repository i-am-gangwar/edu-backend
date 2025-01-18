# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the edu-backend folder to the container
COPY edu-backend-0.0.1-SNAPSHOT.jar /app/edu-backend.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/edu-backend.jar"]
