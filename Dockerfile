FROM amazoncorretto:17
WORKDIR /app
COPY ./target/sentra-policy-0.0.1-SNAPSHOT.jar /app/app.jar

ENV SPRING_PROFILES_ACTIVE=staging
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","-jar","/app/app.jar"]