# ---- Build stage ----
FROM openjdk:25-ea-4-jdk-oraclelinux9 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# ---- Run stage ---- 23-jdk-slim
FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
COPY --from=build /app/target/FollowService-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
