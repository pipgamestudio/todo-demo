FROM openjdk:17-slim
EXPOSE 80
ARG JAR_FILE=target/springboot-firestoredb-todo-1.0.3.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
