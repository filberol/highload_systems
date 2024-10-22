FROM gradle:jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle jar --no-daemon

FROM builder AS tester
RUN gradle test --no-daemon

FROM openjdk:21-jdk-slim AS package
WORKDIR /app
COPY --from=builder /app/build/libs/highload_systems.jar /app/highload_systems.jar
ENTRYPOINT ["java", "-jar", "/app/highload_systems.jar"]