# image reference
FROM openjdk:22-jdk
# copy the .jar file into the Dockerfile
ADD target/coursework-tracker-api.jar coursework-tracker-api.jar
# execute an entry point command
ENTRYPOINT ["java", "-jar", "coursework-tracker-api.jar"]