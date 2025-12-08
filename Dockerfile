# build stage
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# runtime stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Use the PORT environment variable that Render provides
ENV PORT=8080
EXPOSE $PORT

# Set the command to run your app
ENTRYPOINT ["java", "-jar", "app.jar"]
