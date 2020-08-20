FROM openjdk:11-jre-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev", "APP_ENCRYPTION_PASSWORD=test", "/app.jar"]