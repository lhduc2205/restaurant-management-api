FROM openjdk:17-jdk-alpine

ENV SERVER_PORT=8080
ENV DB_HOST=localhost
ENV DB_PORT=5432

LABEL authors="lhduc"

WORKDIR /app

COPY target/restaurant-management-api-0.1.jar .

EXPOSE $SERVER_PORT

ENTRYPOINT ["java", "-jar", "restaurant-management-api-0.1.jar"]