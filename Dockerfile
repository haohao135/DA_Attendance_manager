FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN apk add --no-cache \
    fontconfig \
    ttf-dejavu \
    libx11 \
    libxext \
    libxrender

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.awt.headless=true", "-Dserver.port=8080", "-jar", "/app/app.jar"]
