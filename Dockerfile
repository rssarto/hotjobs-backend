#FROM maven:3.5.2-jdk-8-alpine

#VOLUME /tmp
#COPY pom.xml /tmp/
#COPY src /tmp/src/
#WORKDIR /tmp/
#RUN pwd
#RUN mvn -Dmaven.test.skip=true package
#RUN ls -la

#COPY ./target/hotjobs-backend-0.0.1-SNAPSHOT.jar /app.jar
#ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]

FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml -Dmaven.test.skip=true clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/hotjobs-backend-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/usr/local/lib/app.jar"]

