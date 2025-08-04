FROM openjdk:21-slim
ENV TZ=America/Sao_Paulo

WORKDIR /app

COPY target/*.jar app.jar
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

EXPOSE 8081

RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["./wait-for-it.sh", "mariadb", "3306", "--", "java", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "-jar", "app.jar"]