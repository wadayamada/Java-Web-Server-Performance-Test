FROM openjdk:11
WORKDIR /app
COPY WebServer.java /app/
RUN javac WebServer.java
EXPOSE 8081
CMD ["java", "WebServer"]
