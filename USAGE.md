# Spring Notification Service - Usage Documentation

## Table of Contents

1. [Quick Start](#quick-start)
2. [Environment Setup](#environment-setup)
3. [Running the Application](#running-the-application)
4. [API Authentication](#api-authentication)
5. [Notification Endpoints](#notification-endpoints)
6. [WebSocket Integration](#websocket-integration)
7. [Database Operations](#database-operations)
8. [Monitoring & Health Checks](#monitoring--health-checks)
9. [Configuration Options](#configuration-options)
10. [Troubleshooting](#troubleshooting)

---

## Quick Start

### Prerequisites

- Java 21+
- Docker & Docker Compose (optional)
- Git

### 1-Minute Setup

```bash
# Clone the repository
git clone https://github.com/kenzycodex/spring-notification-service.git
cd spring-notification-service

# Run with Maven wrapper (H2 database)
./mvnw spring-boot:run

# Application available at: http://localhost:8080
```

---

## Environment Setup

### Local Development (H2 Database)

No additional setup required. The application runs with an in-memory H2 database.

### Production Setup (Docker Compose)

1. **Create .env file** in project root:

```bash
# Copy the complete .env template provided
cp .env.example .env
# Edit .env with your actual values
```

2. **Start all services**:

```bash
docker-compose up -d
```

This starts:

- MySQL database on port 3306
- RabbitMQ on ports 5672 (AMQP) and 15672 (Management UI)
- Spring Boot application on port 8080

---

## Running the Application

### Development Mode

```bash
# Run with development profile (H2 database)
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Mode

```bash
# Build JAR
./mvnw clean package

# Run JAR
java -jar -Dspring.profiles.active=prod target/notification-service-1.0.0.jar
```

### Docker Mode

```bash
# Build Docker image
docker build -t notification-service:latest .

# Run with Docker Compose
docker-compose up -d
```

---

## API Authentication

The API uses JWT (JSON Web Token) authentication. All notification endpoints require authentication.

### User Registration

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "password": "securePassword123",
  "email": "john@example.com"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "johndoe",
  "message": "Registration successful"
}
```

### User Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "securePassword123"
}
```

### Using the Token

Include the JWT token in the Authorization header for all protected endpoints:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Notification Endpoints

### Email Notifications

Send email notifications via SMTP.

```http
POST /api/notify/email
Content-Type: application/json
Authorization: Bearer {token}

{
  "to": "recipient@example.com",
  "subject": "Welcome to Our Service",
  "body": "Thank you for registering!",
  "html": false
}
```

**HTML Email Example:**

```json
{
  "to": "customer@example.com",
  "subject": "Order Confirmation",
  "body": "<h1>Order Confirmed</h1><p>Your order #12345 has been confirmed.</p>",
  "html": true
}
```

### Real-time WebSocket Notifications

Send real-time notifications to connected WebSocket clients.

```http
POST /api/notify/realtime
Content-Type: application/json
Authorization: Bearer {token}

{
  "topic": "user-notifications",
  "message": "You have a new message!",
  "recipient": "johndoe"
}
```

**Broadcast to All Users:**

```json
{
  "topic": "system-announcements",
  "message": "System maintenance scheduled for tonight at 2 AM"
}
```

### Queue-based Notifications

Send notifications through RabbitMQ for asynchronous processing.

```http
POST /api/notify/queue
Content-Type: application/json
Authorization: Bearer {token}

{
  "type": "EMAIL",
  "recipient": "user@example.com",
  "message": "Order Shipped|Your order has been shipped and is on its way!",
  "priority": 1
}
```

**Queue Types:**

- `EMAIL`: Processes as email notification
- `WEBSOCKET`: Processes as WebSocket notification
- `BROADCAST`: Broadcasts to all connected clients

**Priority Levels:**

- `1`: High priority
- `0`: Normal priority (default)
- `-1`: Low priority

---

## WebSocket Integration

### Connecting to WebSocket

**JavaScript Example:**

```javascript
// Connect using SockJS + STOMP
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    
    // Subscribe to user-specific notifications
    stompClient.subscribe('/topic/user-notifications', function (message) {
        const notification = JSON.parse(message.body);
        displayNotification(notification);
    });
    
    // Subscribe to system broadcasts
    stompClient.subscribe('/topic/system-announcements', function (message) {
        const announcement = JSON.parse(message.body);
        showAnnouncement(announcement);
    });
});

function displayNotification(notification) {
    console.log('Received:', notification.message);
    console.log('Timestamp:', notification.timestamp);
}
```

### WebSocket Topics

- `/topic/user-notifications`: User-specific notifications
- `/topic/system-announcements`: System-wide broadcasts
- `/topic/{custom-topic}`: Custom topic subscriptions

---

## Database Operations

### H2 Console (Development)

When running in development mode, access the H2 console:

- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** jdbc:h2:mem:devdb
- **Username:** sa
- **Password:** (leave empty)

### MySQL (Production)

Connection details:

- **Host:** localhost:3306 (or mysql:3306 in Docker)
- **Database:** notification_db
- **Username:** notification_user
- **Password:** notification_password

### Database Schema

The application uses Flyway migrations to manage database schema:

**Users Table:**

- `id` (BIGINT, Primary Key)
- `username` (VARCHAR(50), Unique)
- `password` (VARCHAR(255), BCrypt encoded)
- `email` (VARCHAR(100))
- `active` (BOOLEAN)
- `created_at` (TIMESTAMP)
- `last_login` (TIMESTAMP)

**Notifications Table:**

- `id` (BIGINT, Primary Key)
- `type` (VARCHAR(50)) - EMAIL, WEBSOCKET, QUEUE
- `recipient` (VARCHAR(255))
- `content` (TEXT)
- `status` (VARCHAR(20)) - SENT, FAILED, PENDING
- `created_at` (TIMESTAMP)
- `sent_at` (TIMESTAMP)
- `error_message` (VARCHAR(500))

---

## Monitoring & Health Checks

### Health Check Endpoint

```http
GET /actuator/health
```

**Response:**

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    },
    "rabbit": {
      "status": "UP"
    }
  }
}
```

### Application Metrics

```http
GET /actuator/metrics
```

### Custom Health Check

```http
GET /api/notify/health
```

### RabbitMQ Management UI

When RabbitMQ is running via Docker:

- **URL:** http://localhost:15672
- **Username:** notification_user
- **Password:** notification_password

---

## Configuration Options

### Application Profiles

- **dev:** Development mode with H2 database
- **prod:** Production mode with MySQL database
- **test:** Testing mode with H2 database

### Email Configuration

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### JWT Configuration

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: ${JWT_EXPIRATION:3600}  # 1 hour default
```

### RabbitMQ Configuration

```yaml
spring:
  rabbitmq:
    host: ${RABBITMQ_HOST:localhost}
    port: ${RABBITMQ_PORT:5672}
    username: ${RABBITMQ_USERNAME:guest}
    password: ${RABBITMQ_PASSWORD:guest}
```

---

## Troubleshooting

### Common Issues

#### 1. Application Won't Start

**Error:** "Port 8080 is already in use"

```bash
# Find process using port 8080
netstat -ano | findstr :8080
# Kill the process
taskkill /PID <process_id> /F
```

#### 2. Database Connection Failed

**Error:** "Connection refused" or "Access denied"

- Verify MySQL is running: `docker ps`
- Check credentials in .env file
- Ensure database exists

#### 3. RabbitMQ Connection Failed

**Error:** "Connection refused: getsockopt"

- This is expected if RabbitMQ is not running
- Application falls back to direct async processing
- Start RabbitMQ: `docker run -d --name rabbitmq -p 5672:5672 rabbitmq:3.12`

#### 4. JWT Token Invalid

**Error:** "Authentication failed"

- Token may be expired (default: 1 hour)
- Re-authenticate to get a new token
- Check JWT_SECRET configuration

#### 5. Email Not Sending

**Error:** "Authentication failed" or "Connection timeout"

- Verify SMTP credentials
- For Gmail, use App Passwords, not regular password
- Check firewall/network settings

### Debug Mode

Enable debug logging:

```bash
# Command line
./mvnw spring-boot:run -Dlogging.level.com.notificationservice=DEBUG

# Or in application.yml
logging:
  level:
    com.notificationservice: DEBUG
```

### Log Files

- **Development:** `logs/notification-service-dev.log`
- **Production:** `/var/log/notification-service.log`

### Support Resources

- Application logs: Check log files for detailed error messages
- Database: Use H2 console or MySQL client to inspect data
- RabbitMQ: Use management UI to monitor queues and messages
- Actuator endpoints: Monitor application health and metrics

---

## Performance Tips

1. **Email Batching:** For bulk emails, use queue-based notifications
2. **WebSocket Scaling:** Consider using Redis for WebSocket session management in multi-instance deployments
3. **Database Optimization:** Add appropriate indexes for high-traffic queries
4. **Connection Pooling:** Tune HikariCP settings for database connections
5. **Memory Management:** Monitor JVM memory usage and adjust heap size as needed

---

## Security Considerations

1. **JWT Secrets:** Use strong, unique secrets in production
2. **HTTPS:** Always use HTTPS in production environments
3. **Database Security:** Use strong passwords and restrict database access
4. **CORS Configuration:** Configure appropriate CORS settings for your domains
5. **Rate Limiting:** Consider implementing rate limiting for API endpoints