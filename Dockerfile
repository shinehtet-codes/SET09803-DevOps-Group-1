FROM eclipse-temurin:25-jdk
COPY ./target/world-population-reports.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "world-population-reports.jar"]
