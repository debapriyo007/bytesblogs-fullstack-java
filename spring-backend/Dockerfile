# Stage 1: Build the application
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app

# Copy the pom.xml to download dependencies and cache this layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application package (skipping tests for speed)
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Create the runtime container
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (the default server port configured in application.yaml)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
