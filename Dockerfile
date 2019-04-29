FROM maven:3.5.2-jdk-8-alpine

VOLUME /tmp
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN pwd
RUN mvn -Dmaven.test.skip=true package
COPY /target/hotjobs-backend-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]