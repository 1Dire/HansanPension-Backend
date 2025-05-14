FROM openjdk:17-jdk-slim
VOLUME /tmp
# JAR 파일 이름을 고정
COPY build/libs/hansanpension-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


# 컨테이너가 사용할 포트 설정
EXPOSE 8080