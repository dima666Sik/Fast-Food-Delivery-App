# Use a base image with Amazon Corretto 17 and Maven installed
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the parent POM
COPY ../pom.xml /app

# Copy all modules and their POMs
COPY ./product-service /app/product-service
COPY ../jwt-decode-library /app/jwt-decode-library
COPY ../feign-clients /app/feign-clients

# Build the entire project
RUN mvn clean package -DskipTests

# Use lightweight OpenJDK image for the runtime environment
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR files from the build stage
COPY --from=build /app/product-service/target/*.jar ./product-service.jar
COPY --from=build /app/jwt-decode-library/target/*.jar ./jwt-decode-library.jar
COPY --from=build /app/feign-clients/target/*.jar ./feign-clients.jar

EXPOSE 8081

# Example: Run the product-service (you can adjust it as needed)
CMD ["java", "-jar", "product-service.jar"]
