# Use Maven to build the application
FROM maven:3.8.6-openjdk-17 AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -DskipTests

# Use OpenJDK as the base image for the application
FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8080

# Copy the built JAR file from the build stage
COPY --from=build /usr/src/app/target/gameSession-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
