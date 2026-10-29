FROM docker.io/gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle bootJar --no-daemon

FROM docker.io/eclipse-temurin:21-jre-alpine
RUN addgroup -g 1001 appgroup && \
    adduser -u 1001 -G appgroup -S appuser -D
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

