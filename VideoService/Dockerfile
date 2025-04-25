# here we use a multi stage build in order to build app inside conatiner

# then discard evruthing from the previous stage(build stage) except for thr buit JAR file

# RUN this JAR file

################  Stage 1: Build the application ################
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application (creates the JAR file)
RUN mvn clean package -DskipTests

################  Stage 2: Run the application ################
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy only the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8084

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]