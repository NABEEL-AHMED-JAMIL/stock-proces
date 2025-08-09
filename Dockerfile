# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine
LABEL maintainer="nabeel.amd93@gmail.com"

# Create /tmp/efs directory
RUN mkdir -p /tmp/efs
# Add a volume pointing to /tmp/efs
VOLUME /tmp/efs

# Create /tmp/logs directory
RUN mkdir -p /tmp/logs
# Add a volume pointing to /tmp/logs
VOLUME /tmp/logs

# Make port 9097 available to the world outside this container
EXPOSE 9097

# The application's jar file
ARG JAR_FILE=/target/stock-proces.jar

# Add the application jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file and set tmp dir
ENTRYPOINT ["java","-Djava.io.tmpdir=/tmp/efs","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
