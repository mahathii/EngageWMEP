# Use a Maven image to build the application
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src


# Build the application
RUN mvn clean package -DskipTests

# Use a minimal JDK image to run the application
FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built .jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
