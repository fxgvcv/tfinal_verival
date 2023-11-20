FROM openjdk:11-jre-slim
COPY build/libs/informativo-para-imigrantes-0.0.1-SNAPSHOT.jar informativo-para-imigrantes-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["sh","-c", "java -jar informativo-para-imigrantes-0.0.1-SNAPSHOT.jar"]
