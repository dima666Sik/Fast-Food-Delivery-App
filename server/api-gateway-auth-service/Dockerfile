# Use a base image with Amazon Corretto 17 and Maven installed
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and src directories to the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -X

# Use lightweight OpenJDK image for the runtime environment
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build stage to the runtime container
COPY --from=build /app/target/*.jar ./api-gateway.jar

# Specify the command to run application
CMD ["java", "-jar", "api-gateway.jar"]