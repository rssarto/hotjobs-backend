FROM maven:3.5.2-jdk-8-alpine

RUN   apk update && apk add ca-certificates wget && update-ca-certificates

RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub

RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.29-r0/glibc-2.29-r0.apk
RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.29-r0/glibc-bin-2.29-r0.apk

RUN apk add glibc-2.29-r0.apk glibc-bin-2.29-r0.apk


VOLUME /tmp
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package
COPY ./target/hotjobs-backend-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]