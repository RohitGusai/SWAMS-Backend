FROM eclipse-temurin:21-jre-alpine AS build


WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

COPY src ./src

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java","-jar","app.jar"]
