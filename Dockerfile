FROM maven:3.8.8-eclipse-temurin-17

WORKDIR /app/line-webhook

COPY . .

RUN mvn clean install -DskipTests

EXPOSE 8081

CMD ["mvn", "spring-boot:run"]