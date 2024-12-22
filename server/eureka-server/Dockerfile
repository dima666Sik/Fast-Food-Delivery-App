FROM openjdk:17-alpine

WORKDIR /app

COPY target/eureka-server-1.0-SNAPSHOT.jar eureka-server.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
