FROM gradle:8.1.1-jdk11 AS build
WORKDIR /app
COPY . /app
RUN gradle clean shadowJar --no-daemon

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=build /app/build/libs/Crowdfunding-1.0.0-all.jar Crowdfunding.jar

EXPOSE 9000

CMD ["java", "-jar", "Crowdfunding.jar"]
