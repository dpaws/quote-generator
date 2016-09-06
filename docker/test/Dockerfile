FROM openjdk:8-jdk-alpine
MAINTAINER Justin Menga <justin.menga@gmail.com>

# Install system dependencies
RUN echo "http://nl.alpinelinux.org/alpine/edge/community/" >> /etc/apk/repositories && \
    apk update && \
    apk add --no-cache bash maven

# Copy just the POM first
COPY pom.xml /app/
WORKDIR /app

# Install dependencies 
# We call versions:set to force dependences for the version plugin to be installed
RUN mvn versions:set clean install

# Copy the application source and set the app version
COPY src /app/src
ARG app_version
RUN mvn versions:set -DnewVersion=${app_version}