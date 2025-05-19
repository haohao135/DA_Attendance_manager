# Stage 1: Build project trong container dùng Maven
FROM maven:3.8.7-openjdk-17 AS builder

# Đặt thư mục làm việc
WORKDIR /app

# Copy pom.xml và source code vào container
COPY pom.xml .
COPY src ./src

# Build project (bỏ qua test để nhanh)
RUN mvn clean package -DskipTests

# Stage 2: Tạo image chạy app
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy file jar đã build từ stage trước sang
COPY --from=builder /app/target/*.jar app.jar

# Set biến môi trường PORT để Spring Boot lấy đúng port
ENV PORT=8080

EXPOSE 8080

# Lệnh chạy app, đọc port từ biến môi trường PORT
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]
