# Use a base image with Amazon Corretto 17 and Maven installed
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the parent POM
COPY ../pom.xml /app

# Copy all modules and their POMs
COPY ./order-service /app/order-service
COPY ../jwt-decode-library /app/jwt-decode-library
COPY ../feign-clients /app/feign-clients

# Build the entire project
RUN mvn clean package -DskipTests

# Use lightweight OpenJDK image for the runtime environment
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR files from the build stage
COPY --from=build /app/order-service/target/*.jar ./order-service.jar
COPY --from=build /app/jwt-decode-library/target/*.jar ./jwt-decode-library.jar
COPY --from=build /app/feign-clients/target/*.jar ./feign-clients.jar

EXPOSE 8082

# Example: Run the order-service (you can adjust it as needed)
CMD ["java", "-jar", "order-service.jar"]
