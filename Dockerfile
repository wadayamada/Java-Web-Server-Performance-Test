FROM gradle:jdk21
COPY . ./
RUN ./gradlew installDist
EXPOSE 8081
CMD ["build/install/Java-Web-Server-Performance-Test/bin/Java-Web-Server-Performance-Test"]
