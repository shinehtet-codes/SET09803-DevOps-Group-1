FROM eclipse-temurin:25-jdk

COPY ./target/classes /tmp/classes

WORKDIR /tmp/classes

ENTRYPOINT ["java", "org.devops.App"]