FROM gradle:jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim AS package
WORKDIR /app
COPY --from=builder /app/build/libs/gateway.jar /app/gateway.jar
ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]