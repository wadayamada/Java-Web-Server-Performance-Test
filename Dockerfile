FROM openjdk:11
WORKDIR /app
COPY src/main/java/Server.java /app/
RUN javac Server.java
EXPOSE 8081
CMD ["java", "Server"]
