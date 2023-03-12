FROM openjdk:8-jdk-alpine
COPY target/ad-trade-processor-0.0.1-SNAPSHOT.jar /ad-trade-processor-0.0.1-SNAPSHOT.jar
EXPOSE 8092
CMD ["java", "-jar", "ad-trade-processor-0.0.1-SNAPSHOT.jar"]