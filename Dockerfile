# 1. JDK 21 기반 이미지 사용
FROM eclipse-temurin:21-jdk-jammy

# 2. 컨테이너 내부 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 빌드 결과 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 4. 환경변수 설정
ENV SERVER_PORT=8080

# 5. 컨테이너 포트 노출
EXPOSE ${SERVER_PORT}

# 6. JAR 실행
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${SERVER_PORT}"]
