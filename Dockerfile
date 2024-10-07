# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine
# Add Maintainer Info
LABEL maintainer="nabeel.amd93@gmail.com"
# Add a volume pointing to /tmp
VOLUME /tmp
# Make port 9097 available to the world outside this container
EXPOSE 9097
# The application's jar file
ARG JAR_FILE=/target/stock-proces.jar
# Add the application jar to the container
ADD ${JAR_FILE} app.jar
# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]