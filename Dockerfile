# Etapa 1: Construcción (Build)
FROM gradle:7.6-jdk17 AS build
WORKDIR /app

# archivos de configuración de Gradle
COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle bootJar --no-daemon

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Gradle guarda el archivo generado en build/libs/
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]