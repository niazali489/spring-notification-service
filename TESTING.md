# Spring Notification Service - Testing Documentation

## Table of Contents

1. [Testing Overview](#testing-overview)
2. [Unit Tests](#unit-tests)
3. [Integration Tests](#integration-tests)
4. [API Testing](#api-testing)
5. [WebSocket Testing](#websocket-testing)
6. [Load Testing](#load-testing)
7. [Manual Testing Scenarios](#manual-testing-scenarios)
8. [Automated Testing](#automated-testing)
9. [Test Data](#test-data)
10. [CI/CD Testing](#cicd-testing)

---

## Testing Overview

### Test Structure

```
src/test/java/com/notificationservice/
├── NotificationServiceApplicationTests.java
├── controller/
│   ├── NotificationControllerTest.java
│   └── AuthControllerTest.java
├── service/
│   ├── NotificationServiceTest.java
│   ├── EmailServiceTest.java
│   ├── WebSocketServiceTest.java
│   └── QueueServiceTest.java
└── repository/
    ├── NotificationRepositoryTest.java
    └── UserRepositoryTest.java
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=NotificationServiceTest

# Run tests with coverage
./mvnw test jacoco:report

# Run integration tests only
./mvnw test -Dgroups=integration
```

---

## Unit Tests

### Service Layer Tests

**NotificationServiceTest.java**

```bash
# Test email notification success
./mvnw test -Dtest=NotificationServiceTest#testSendEmailNotification_Success

# Test real-time notification failure
./mvnw test -Dtest=NotificationServiceTest#testSendRealtimeNotification_Failure

# Test queue notification processing
./mvnw test -Dtest=NotificationServiceTest#testSendQueueNotification_Success
```

**EmailServiceTest.java**

```bash
# Test SMTP email sending
./mvnw test -Dtest=EmailServiceTest#testSendEmail_Success

# Test HTML email formatting
./mvnw test -Dtest=EmailServiceTest#testSendHtmlEmail

# Test email validation
./mvnw test -Dtest=EmailServiceTest#testSendEmail_InvalidRecipient
```

### Controller Tests

**NotificationControllerTest.java**

```bash
# Test authenticated endpoints
./mvnw test -Dtest=NotificationControllerTest#testSendEmailNotification_Success

# Test validation errors
./mvnw test -Dtest=NotificationControllerTest#testSendEmailNotification_ValidationError

# Test unauthorized access
./mvnw test -Dtest=NotificationControllerTest#testSendNotification_Unauthorized
```

### Repository Tests

```bash
# Test user repository operations
./mvnw test -Dtest=UserRepositoryTest

# Test notification queries
./mvnw test -Dtest=NotificationRepositoryTest
```

---

## Integration Tests

### Database Integration

```bash
# Test with H2 database
./mvnw test -Dspring.profiles.active=test

# Test Flyway migrations
./mvnw test -Dtest=FlywayMigrationTest
```

### Full Application Context

```bash
# Test complete Spring context loading
./mvnw test -Dtest=NotificationServiceApplicationTests

# Test with security configuration
./mvnw test -Dtest=SecurityIntegrationTest
```

---

## API Testing

### Using curl Commands

#### 1. Health Check (No Auth Required)

```bash
curl -X GET http://localhost:8080/actuator/health \
  -H "Content-Type: application/json"

# Expected Response:
# {"status":"UP"}
```

#### 2. User Registration

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'

# Expected Response:
# {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "type": "Bearer",
#   "username": "testuser",
#   "message": "Registration successful"
# }
```

#### 3. User Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'

# Copy the token from response for next requests
export TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### 4. Send Email Notification

```bash
curl -X POST http://localhost:8080/api/notify/email \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "to": "recipient@example.com",
    "subject": "Test Email",
    "body": "This is a test email from the notification service",
    "html": false
  }'

# Expected Response:
# {
#   "status": "success",
#   "message": "Email notification sent successfully",
#   "recipient": "recipient@example.com"
# }
```

#### 5. Send WebSocket Notification

```bash
curl -X POST http://localhost:8080/api/notify/realtime \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "topic": "user-notifications",
    "message": "You have a new message!",
    "recipient": "testuser"
  }'

# Expected Response:
# {
#   "status": "success",
#   "message": "Real-time notification sent successfully",
#   "topic": "user-notifications"
# }
```

#### 6. Send Queue Notification

```bash
curl -X POST http://localhost:8080/api/notify/queue \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "type": "EMAIL",
    "recipient": "queue@example.com",
    "message": "Order Shipped|Your order has been shipped!",
    "priority": 1
  }'

# Expected Response:
# {
#   "status": "success",
#   "message": "Queue notification sent successfully",
#   "type": "EMAIL"
# }
```

### Error Testing

#### 7. Test Invalid Authentication

```bash
curl -X POST http://localhost:8080/api/notify/email \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer invalid_token" \
  -d '{
    "to": "test@example.com",
    "subject": "Test",
    "body": "Test"
  }'

# Expected Response: 401 Unauthorized
```

#### 8. Test Validation Errors

```bash
curl -X POST http://localhost:8080/api/notify/email \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "to": "invalid-email",
    "subject": "",
    "body": ""
  }'

# Expected Response: 400 Bad Request with validation errors
```

---

## WebSocket Testing

### JavaScript Test Client

```html
<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Test Client</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs/bundles/stomp.umd.min.js"></script>
</head>
<body>
    <div id="messages"></div>
    <button onclick="connect()">Connect</button>
    <button onclick="disconnect()">Disconnect</button>
    <button onclick="sendTestMessage()">Send Test Message</button>

    <script>
        let stompClient = null;

        function connect() {
            const socket = new SockJS('http://localhost:8080/ws');
            stompClient = Stomp.over(socket);
            
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                addMessage('Connected to WebSocket');
                
                // Subscribe to user notifications
                stompClient.subscribe('/topic/user-notifications', function (message) {
                    const notification = JSON.parse(message.body);
                    addMessage('User Notification: ' + notification.message);
                });
                
                // Subscribe to system broadcasts
                stompClient.subscribe('/topic/system-announcements', function (message) {
                    const announcement = JSON.parse(message.body);
                    addMessage('System: ' + announcement.message);
                });
            });
        }

        function disconnect() {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            addMessage('Disconnected from WebSocket');
        }

        function sendTestMessage() {
            // This would typically be triggered by the REST API
            // Use the curl command for /api/notify/realtime to test
            addMessage('Use REST API to send WebSocket notifications');
        }

        function addMessage(message) {
            const div = document.createElement('div');
            div.textContent = new Date().toLocaleTimeString() + ' - ' + message;
            document.getElementById('messages').appendChild(div);
        }
    </script>
</body>
</html>
```

---

## Load Testing

### Using Apache Bench (ab)

```bash
# Test registration endpoint
ab -n 100 -c 10 -H "Content-Type: application/json" \
  -p registration.json http://localhost:8080/api/auth/register

# Test health endpoint
ab -n 1000 -c 50 http://localhost:8080/actuator/health
```

### Using JMeter

Create a JMeter test plan with:

1. Thread Group (100 users, ramp-up 10s)
2. HTTP Request for registration
3. HTTP Request for login
4. HTTP Request for email notification
5. Listeners for results

### Performance Benchmarks

Expected performance on standard hardware:

- **Registration/Login**: 500+ requests/second
- **Email Notifications**: 100+ requests/second
- **WebSocket Notifications**: 1000+ messages/second
- **Health Checks**: 2000+ requests/second

---

## Manual Testing Scenarios

### Scenario 1: User Registration Flow

1. Start application
2. Register new user with valid data
3. Verify user created in database
4. Verify JWT token returned
5. Test login with same credentials
6. Test duplicate registration (should fail)

### Scenario 2: Email Notification Flow

1. Register and login user
2. Send email notification via API
3. Check application logs for email sending
4. Verify notification record in database
5. Test with invalid email format (should fail)
6. Test without authentication (should fail)

### Scenario 3: WebSocket Notification Flow

1. Connect WebSocket client
2. Subscribe to notification topic
3. Send WebSocket notification via API
4. Verify message received by client
5. Test broadcast functionality
6. Test client disconnection handling

### Scenario 4: Queue Processing Flow

1. Start application with RabbitMQ
2. Send queue notification via API
3. Verify message in RabbitMQ queue
4. Verify message processed correctly
5. Test with RabbitMQ offline (fallback)
6. Monitor queue processing metrics

### Scenario 5: Error Handling

1. Test with invalid JWT tokens
2. Test with malformed request data
3. Test with database unavailable
4. Test with email server unavailable
5. Verify proper error responses
6. Check error logging

---

## Automated Testing

### GitHub Actions Workflow

```yaml
name: Test Suite

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest

    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: test_db
        ports:
          - 3306:3306

      rabbitmq:
        image: rabbitmq:3.12
        ports:
          - 5672:5672

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Run tests
        run: ./mvnw clean test

      - name: Generate test report
        run: ./mvnw jacoco:report

      - name: Upload coverage reports
        uses: codecov/codecov-action@v3
```

### Test Configuration

```yaml
# src/test/resources/application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password: password

  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: false

  flyway:
    enabled: false

  mail:
    host: localhost
    port: 2525

logging:
  level:
    com.notificationservice: DEBUG
```

---

## Test Data

### Sample Users

```json
{
  "admin": {
    "username": "admin",
    "password": "admin123",
    "email": "admin@notificationservice.com"
  },
  "testuser": {
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  },
  "developer": {
    "username": "developer",
    "password": "dev123",
    "email": "dev@example.com"
  }
}
```

### Sample Notifications

```json
{
  "email_simple": {
    "to": "recipient@example.com",
    "subject": "Welcome",
    "body": "Welcome to our service!",
    "html": false
  },
  "email_html": {
    "to": "customer@example.com",
    "subject": "Order Confirmation",
    "body": "<h1>Thank You!</h1><p>Your order has been confirmed.</p>",
    "html": true
  },
  "websocket_notification": {
    "topic": "user-alerts",
    "message": "You have a new message!",
    "recipient": "testuser"
  },
  "queue_notification": {
    "type": "EMAIL",
    "recipient": "queue@example.com",
    "message": "Shipping Update|Your package is on the way!",
    "priority": 1
  }
}
```

---

## CI/CD Testing

### Pre-commit Hooks

```bash
#!/bin/sh
# .git/hooks/pre-commit
./mvnw clean test
if [ $? -ne 0 ]; then
  echo "Tests failed. Commit aborted."
  exit 1
fi
```

### Docker Test Environment

```yaml
# docker-compose.test.yml
version: '3.8'
services:
  test-app:
    build: .
    environment:
      - SPRING_PROFILES_ACTIVE=test
    depends_on:
      - test-mysql
      - test-rabbitmq
    command: ./mvnw test

  test-mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: test
      MYSQL_DATABASE: test_db

  test-rabbitmq:
    image: rabbitmq:3.12
```

### Test Reports

Generated test reports available at:

- **Coverage Report**: `target/site/jacoco/index.html`
- **Surefire Report**: `target/site/surefire-report.html`
- **Test Results**: `target/surefire-reports/`

---

## Test Maintenance

### Adding New Tests

1. Follow naming convention: `TestClass#testMethod_Scenario`
2. Use `@Test` annotation with descriptive names
3. Include setup and teardown methods
4. Mock external dependencies
5. Assert expected outcomes clearly

### Test Best Practices

1. **Isolation**: Each test should be independent
2. **Repeatability**: Tests should produce consistent results
3. **Fast Execution**: Keep unit tests under 100ms
4. **Clear Assertions**: Use descriptive assertion messages
5. **Comprehensive Coverage**: Aim for 80%+ code coverage

### Debugging Failed Tests

1. Run individual test: `./mvnw test -Dtest=TestClass#testMethod`
2. Enable debug logging: `-Dlogging.level.com.notificationservice=DEBUG`
3. Use IDE debugging capabilities
4. Check test logs in `target/surefire-reports/`
5. Verify test environment setup

This comprehensive testing documentation ensures the Spring Boot notification service is thoroughly tested and
production-ready. Enjoy!!!