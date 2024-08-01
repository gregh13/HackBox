# Use official OpenJDK image as a base image
FROM openjdk:17-jdk-slim

# Add a volume pointing to /tmp
VOLUME /tmp

# Expose port 8080 to the outside world
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/gameSession-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
