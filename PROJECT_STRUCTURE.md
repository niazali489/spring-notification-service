# Notification & Messaging Service - Project Structure

## Complete Maven Project Structure

```
notification-service/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── .gitignore
├── README.md
└── src/
    ├── main/
    │   ├── java/com/notificationservice/
    │   │   ├── NotificationServiceApplication.java
    │   │   ├── config/
    │   │   │   ├── WebSocketConfig.java
    │   │   │   ├── RabbitMQConfig.java
    │   │   │   ├── EmailConfig.java
    │   │   │   ├── SecurityConfig.java
    │   │   │   └── JwtConfig.java
    │   │   ├── controller/
    │   │   │   ├── NotificationController.java
    │   │   │   └── AuthController.java
    │   │   ├── service/
    │   │   │   ├── NotificationService.java
    │   │   │   ├── EmailService.java
    │   │   │   ├── WebSocketService.java
    │   │   │   ├── QueueService.java
    │   │   │   └── AuthService.java
    │   │   ├── model/
    │   │   │   ├── entity/
    │   │   │   │   ├── Notification.java
    │   │   │   │   └── User.java
    │   │   │   └── dto/
    │   │   │       ├── EmailNotificationRequest.java
    │   │   │       ├── RealtimeNotificationRequest.java
    │   │   │       ├── QueueNotificationRequest.java
    │   │   │       ├── AuthRequest.java
    │   │   │       └── AuthResponse.java
    │   │   ├── repository/
    │   │   │   ├── NotificationRepository.java
    │   │   │   └── UserRepository.java
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   ├── NotificationException.java
    │   │   │   └── AuthenticationException.java
    │   │   └── util/
    │   │       └── JwtUtil.java
    │   └── resources/
    │       ├── application.yml
    │       ├── application-dev.yml
    │       ├── application-prod.yml
    │       └── logback-spring.xml
    └── test/
        └── java/com/notificationservice/
            ├── NotificationServiceApplicationTests.java
            ├── service/
            │   ├── NotificationServiceTest.java
            │   └── EmailServiceTest.java
            └── controller/
                └── NotificationControllerTest.java
```

This is a production-ready Spring Boot microservice with:

- **Real-time WebSocket notifications**
- **Email notifications via SMTP**
- **RabbitMQ queue-based messaging**
- **JWT authentication**
- **MySQL + H2 database support**
- **Complete test suite**
- **Docker containerization**