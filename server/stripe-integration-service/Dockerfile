FROM openjdk:17-alpine

WORKDIR /app

COPY target/stripe-integration-service-1.0-SNAPSHOT.jar stripe-integration-service.jar

EXPOSE 8165

ENTRYPOINT ["java", "-jar", "stripe-integration-service.jar"]