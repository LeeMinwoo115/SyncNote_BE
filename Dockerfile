FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -jar app.jar --server.port=${PORT:-8080} --server.address=0.0.0.0"]