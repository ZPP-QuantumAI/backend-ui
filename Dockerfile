# Use a base image with Gradle and Java pre-installed
FROM gradle:jdk21 AS builder

# Set the working directory
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle .
COPY settings.gradle .

# Download and cache the dependencies
RUN gradle --no-daemon dependencies

# Copy the source code
COPY src/ src/

# Build the application
RUN gradle --no-daemon build

# Use a lightweight base image for the final application
FROM eclipse-temurin:21-jdk

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/backend-ui*.jar ./your-app.jar

EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "your-app.jar"]
