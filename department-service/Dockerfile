FROM gradle:jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle .. .
RUN gradle jar --no-daemon

FROM openjdk:21-jdk-slim AS package
WORKDIR /app
COPY --from=builder /app/build/libs/oxygen_service.jar /app/oxygen_service.jar
ENTRYPOINT ["java", "-jar", "/app/oxygen_service.jar"]