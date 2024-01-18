# Use an official Maven image as a build image
FROM maven:3.8.1-openjdk-17-slim AS build

# Set the working directory
WORKDIR /app

# Copy the source code
COPY src /app/src

# Copy the pom.xml file
COPY pom.xml /app

# Build the application
RUN mvn clean install -DskipTests

# Use an official Eclipse Temurin (AdoptOpenJDK) image as a runtime image
FROM eclipse-temurin:17

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build image
COPY --from=build /app/target/Codingtest-0.0.1-SNAPSHOT.jar /app

# Expose the port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "Codingtest-0.0.1-SNAPSHOT.jar"]
