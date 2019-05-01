#Multi-stage build - https://docs.docker.com/develop/develop-images/dockerfile_best-practices/
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/hotjobs-backend-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/usr/local/lib/app.jar"]


