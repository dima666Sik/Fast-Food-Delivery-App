FROM openjdk:17-alpine

WORKDIR /app

COPY target/email-sender-service-1.0-SNAPSHOT.jar email-sender-service.jar

EXPOSE 8097

ENTRYPOINT ["java", "-jar", "email-sender-service.jar"]