FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test
RUN ls -la /app/build/libs/

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
RUN ls -la /app/
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080