FROM eclipse-temurin:25-jdk
COPY ./target/world-population-reports-v0.1-alpha-3.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "world-population-reports-v0.1-alpha-3.jar"]