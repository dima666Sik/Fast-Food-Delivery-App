FROM openjdk:17-alpine

WORKDIR /app

COPY target/chat-helper-ai-service-1.0-SNAPSHOT.jar chat-helper-ai-service.jar

EXPOSE 8165

ENTRYPOINT ["java", "-jar", "chat-helper-ai-service.jar"]