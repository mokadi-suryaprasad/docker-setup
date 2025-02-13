# Single Stage building

# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim
# Set the working directory in container to /app
WORKDIR /app
# Copy the current directory contents into the container's /app directory
COPY . .
# Run maven to package the application, skipping tests
RUN ./mvnw package -DskipTests
# Expose port 8080 for the application
EXPOSE 8080
# Set the default command to run the Java application
CMD [ "Java", "-jar", "target/*.jar" ]

# Multi Stage building

# Stage 1: Build the application using Maven and OpenJDK 17
FROM maven:3.8.5-openjdk-17-slim AS build
# Set working directory inside the container to /app
WORKDIR /app
# Copy the current directory contents into the container
COPY . .
# Run Maven to package the application , skipping tests
RUN ./mvnw package -DskipTests
# Stage 2: Create the run runtime image (use JRE for smaller size)
FROM openjdk:17-alpine
# Set the working directory for the runtime environment
WORKDIR /app
# Copy the JAR files from the build stage 
COPY --from=build /app/target/*.jar app.jar
# Expose port 8080 for the application to communicate
EXPOSE 8080
# Set the default command to run the Java application
CMD ["java", "-jar", "app.jar"]

