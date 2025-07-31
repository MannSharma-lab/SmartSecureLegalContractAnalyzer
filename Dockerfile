# Use Maven to build the app
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run the built jar file
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/SmartSecureLegalContractAnalyzer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]