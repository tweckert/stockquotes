#
# docker build --no-cache -t stockquotes:latest .
#

#
# stage #1
#

# initialize build and set base image for first stage
FROM maven:3.8.6-openjdk-8-slim as MAVEN_BUILD
WORKDIR /opt/build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean install -Dmaven.test.skip=true

#
# stage #2
#
FROM adoptopenjdk/openjdk8:alpine-jre as stage2
WORKDIR /opt/app
RUN mkdir -p /opt/yahoo/download
RUN apk --no-cache add curl
COPY --from=MAVEN_BUILD /opt/build/target/stockquotes-0.0.1-SNAPSHOT.war /opt/app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/app/stockquotes-0.0.1-SNAPSHOT.war"]