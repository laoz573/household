FROM openjdk:17

WORKDIR /app

COPY target/HouseHold.war /app/
COPY target/dependency/webapp-runner-10.1.23.0.jar /app/

EXPOSE 8080

CMD ["java", "-jar", "webapp-runner-10.1.23.0.jar", "HouseHold.war"]
