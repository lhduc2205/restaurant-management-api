FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY ./target/restaurant-management-api-0.1.jar /app/restaurant-management-api-0.1.jar

ENTRYPOINT ["java", "-jar", "restaurant-management-api-0.1.jar"]