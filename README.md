# Notification & Messaging Service

A production-ready Spring Boot microservice for handling notifications via multiple channels including email, real-time
WebSocket, and queue-based messaging with RabbitMQ.

## 🚀 Features

- **📧 Email Notifications**: SMTP-based email sending with HTML/plain text support
- **⚡ Real-time Notifications**: WebSocket-based real-time messaging via STOMP
- **🔄 Queue-based Processing**: RabbitMQ message queuing with fallback to async processing
- **🔒 JWT Authentication**: Secure API access with JWT tokens
- **💾 Database Support**: MySQL for production, H2 for development
- **🐳 Docker Ready**: Full containerization with Docker Compose
- **📊 Monitoring**: Spring Boot Actuator with health checks and metrics
- **🧪 Comprehensive Testing**: Unit tests with JUnit 5 and Mockito

## 📋 Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Docker & Docker Compose** (optional, for containerized setup)
- **MySQL 8.0+** (for production)
- **RabbitMQ 3.12+** (optional, falls back to async processing)

## 🛠️ Quick Start

### 1. Clone and Setup

```bash
# Clone the repository
git clone https://github.com/kenzycodex/spring-notification-service.git


cd spring-notification-service

# Run with Maven (uses H2 database)
mvn spring-boot:run

# 🎉 Service running at http://localhost:8080
```

### 2. Run Locally (Development)

```bash
# Build the project
mvn clean compile

# Run with development profile (uses H2 database)
mvn spring-boot:run

# Or specify profile explicitly
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The service will start on `http://localhost:8080`

### 3. Run with Docker Compose (Production-like)

```bash
# Create environment file
echo "MAIL_USERNAME=your-email@gmail.com" > .env
echo "MAIL_PASSWORD=your-app-password" >> .env
echo "MAIL_FROM=noreply@yourcompany.com" >> .env
echo "JWT_SECRET=your-very-long-secure-secret-key-here" >> .env

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f notification-service
```

## 🔧 Configuration

### Environment Variables

| Variable                 | Description               | Default                 | Required      |
|--------------------------|---------------------------|-------------------------|---------------|
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | `dev`                   | No            |
| `DB_HOST`                | MySQL host                | `mysql`                 | Prod only     |
| `DB_PORT`                | MySQL port                | `3306`                  | Prod only     |
| `DB_NAME`                | Database name             | `notification_db`       | Prod only     |
| `DB_USERNAME`            | Database username         | `notification_user`     | Prod only     |
| `DB_PASSWORD`            | Database password         | `notification_password` | Prod only     |
| `RABBITMQ_HOST`          | RabbitMQ host             | `rabbitmq`              | No            |
| `RABBITMQ_USERNAME`      | RabbitMQ username         | `notification_user`     | No            |
| `RABBITMQ_PASSWORD`      | RabbitMQ password         | `notification_password` | No            |
| `MAIL_HOST`              | SMTP host                 | `smtp.gmail.com`        | No            |
| `MAIL_USERNAME`          | SMTP username             | -                       | Yes for email |
| `MAIL_PASSWORD`          | SMTP password             | -                       | Yes for email |
| `MAIL_FROM`              | From email address        | -                       | No            |
| `JWT_SECRET`             | JWT secret key            | -                       | Yes           |
| `JWT_EXPIRATION`         | JWT expiration (seconds)  | `3600`                  | No            |

### Email Setup (Gmail Example)

1. Enable 2-Factor Authentication on your Gmail account
2. Generate an App Password: `Google Account > Security > App passwords`
3. Use your Gmail address as `MAIL_USERNAME`
4. Use the generated app password as `MAIL_PASSWORD`

## 🔌 API Endpoints

### Authentication

#### Register User

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "user@example.com"
}
```

#### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "testuser",
  "message": "Authentication successful"
}
```

### Notifications (Require Authentication)

Add header: `Authorization: Bearer {your-jwt-token}`

#### Send Email Notification

```http
POST /api/notify/email
Content-Type: application/json

{
  "to": "recipient@example.com",
  "subject": "Test Notification",
  "body": "This is a test email notification",
  "html": false
}
```

#### Send Real-time Notification

```http
POST /api/notify/realtime
Content-Type: application/json

{
  "topic": "user-notifications",
  "message": "New message for you!",
  "recipient": "username"
}
```

#### Send Queue Notification

```http
POST /api/notify/queue
Content-Type: application/json

{
  "type": "EMAIL",
  "recipient": "user@example.com",
  "message": "Subject|Email body content",
  "priority": 0
}
```

### Health Check

```http
GET /api/notify/health
```

## 🔗 WebSocket Connection

Connect to WebSocket endpoint for real-time notifications:

```javascript
// Connect to WebSocket
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to a topic
    stompClient.subscribe('/topic/user-notifications', function (message) {
        const notification = JSON.parse(message.body);
        console.log('Received notification:', notification);
    });
});
```

## 🧪 Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

### Test Coverage

```bash
mvn jacoco:report
```

### Manual API Testing

Use the provided Postman collection or curl commands:

```bash
# Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123","email":"test@example.com"}'

# Login and get token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}' | jq -r '.token')

# Send email notification
curl -X POST http://localhost:8080/api/notify/email \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"to":"test@example.com","subject":"Test","body":"Hello World"}'
```

## 🔧 Development

### IDE Setup (IntelliJ IDEA)

1. **Import Project**: File → Open → Select `pom.xml`
2. **Set JDK**: File → Project Structure → Project → SDK: Java 17
3. **Enable Annotation Processing**: Settings → Build → Compiler → Annotation Processors
4. **Install Plugins**: Spring Boot, Database Navigator (optional)

### Code Style

- Follow Spring Boot best practices
- Use meaningful variable and method names
- Add JavaDoc for public methods
- Maintain test coverage above 80%

### Adding New Features

1. **Create Feature Branch**: `git checkout -b feature/your-feature`
2. **Add Service Layer**: Business logic in `service` package
3. **Add Controller**: REST endpoints in `controller` package
4. **Add Tests**: Unit tests for all new code
5. **Update Documentation**: README and API docs

## 🚀 Deployment

### Production Deployment

1. **Build JAR**:

```bash
mvn clean package -DskipTests
```

1. **Run with Production Profile**:

```bash
java -jar -Dspring.profiles.active=prod target/notification-service-1.0.0.jar
```

1. **Docker Deployment**:

```bash
# Build image
docker build -t notification-service:1.0.0 .

# Run container
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-mysql-host \
  -e MAIL_USERNAME=your-email@gmail.com \
  -e MAIL_PASSWORD=your-app-password \
  -e JWT_SECRET=your-secure-secret \
  notification-service:1.0.0
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: notification-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: notification-service
  template:
    metadata:
      labels:
        app: notification-service
    spec:
      containers:
        - name: notification-service
          image: notification-service:1.0.0
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "prod"
            - name: DB_HOST
              value: "mysql-service"
          # Add other environment variables
```

## 📊 Monitoring

### Health Checks

- Application: `GET /actuator/health`
- Database: Included in health endpoint
- RabbitMQ: Included if configured

### Metrics

- Prometheus metrics: `/actuator/prometheus`
- Application metrics: `/actuator/metrics`

### Logging

- Log files: `logs/notification-service.log`
- Log levels configurable per environment
- Structured JSON logging in production

## 🔒 Security

- JWT tokens for API authentication
- Secure password encoding with BCrypt
- CORS configuration for cross-origin requests
- Input validation on all endpoints
- SQL injection prevention with JPA
- XSS protection with proper encoding

## 🐛 Troubleshooting

### Common Issues

1. **Email Not Sending**
    - Check SMTP credentials
    - Verify Gmail app password
    - Check firewall settings

2. **Database Connection Failed**
    - Verify MySQL is running
    - Check connection parameters
    - Ensure database exists

3. **RabbitMQ Connection Issues**
    - Service falls back to async processing
    - Check RabbitMQ server status
    - Verify connection credentials

4. **JWT Token Issues**
    - Ensure JWT_SECRET is set
    - Check token expiration
    - Verify Authorization header format

### Debug Mode

Enable debug logging:

```yaml
logging:
  level:
    com.notificationservice: DEBUG
    org.springframework.web: DEBUG
```

## 📚 Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   REST Client   │────│  NotificationAPI │────│   JWT Security  │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │
                   ┌────────────┴────────────┐
                   │  NotificationService    │
                   └────────────┬────────────┘
                                │
        ┌───────────────────────┼───────────────────────┐
        │                       │                       │
┌───────▼──────┐    ┌──────────▼─────────┐    ┌────────▼────────┐
│ EmailService │    │ WebSocketService   │    │  QueueService   │
└──────────────┘    └────────────────────┘    └─────────────────┘
        │                       │                       │
┌───────▼──────┐    ┌──────────▼─────────┐    ┌────────▼────────┐
│     SMTP     │    │    WebSocket       │    │    RabbitMQ     │
└──────────────┘    └────────────────────┘    └─────────────────┘
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:

- Create an issue in the repository
- Email: kenzycodex@gmail.com
- Documentation: README Docs

---

## ✅ Deployment Checklist

- [ ] Java 17+ installed
- [ ] Maven dependencies resolved
- [ ] Database configuration set
- [ ] Email SMTP configured
- [ ] JWT secret configured
- [ ] RabbitMQ setup (optional)
- [ ] Docker images built
- [ ] Environment variables set
- [ ] Health checks passing
- [ ] Tests passing
- [ ] Security review completed