FROM gradle:jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle .. .
RUN gradle jar --no-daemon

FROM openjdk:21-jdk-slim AS package
WORKDIR /app
COPY --from=builder /app/build/libs/user_service.jar /app/user_service.jar
ENTRYPOINT ["java", "-jar", "/app/user_service.jar"]