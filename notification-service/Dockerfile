FROM openjdk:17-jdk-alpine

COPY build/libs/*.jar api-gateway.jar

ENTRYPOINT ["java","-jar","/api-gateway.jar"]