# Etapa 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# Etapa 2: Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

COPY --from=build /app/src/test/resources/application.yml test-config.yml

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.config.location=file:/app/test-config.yml", "-jar", "app.jar"]