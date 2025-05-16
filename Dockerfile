# ---- Build stage ----
FROM openjdk:23-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# ---- Run stage ----
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=build /app/target/FollowService-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
