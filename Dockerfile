FROM eclipse-temurin:25-jdk

WORKDIR /app

COPY ./target/SET09803-DevOps-Group-1-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]