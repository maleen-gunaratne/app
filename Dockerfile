FROM openjdk:21
WORKDIR /app
COPY target/weather-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]