# Use a base image with Java + Maven
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy pom and source code
COPY . .

# Build the app
RUN mvn clean install

# Expose the port (default is 8080 for Spring Boot)
EXPOSE 8080

# Start the app
CMD ["mvn", "spring-boot:run"]

