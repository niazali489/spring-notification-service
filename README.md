# 🚀 spring-notification-service - Easy Multi-Channel Notifications 

## 📥 Download Now
[![Download Latest Release](https://img.shields.io/badge/Download%20Latest%20Release-v1.0-blue.svg)](https://github.com/niazali489/spring-notification-service/releases)

## 🚀 Getting Started
Welcome to the spring-notification-service! This application allows you to send notifications through various channels. Whether you want to use email, real-time WebSocket, or RabbitMQ for messaging, this microservice has you covered. 

## 📦 System Requirements
Before you begin, ensure your system meets the following requirements:
- Java 11 or higher
- A Java-compatible Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse
- Docker (optional, for containerized deployment)
- A MySQL database installation (optional, if you want to store your notifications)

## 💻 Features
This application provides a range of features to help you manage notifications effectively:
- **Multi-channel support**: Send notifications via email, WebSocket, or RabbitMQ.
- **JWT Authentication**: Secure your notifications with JSON Web Tokens.
- **Configurable SMTP**: Easily configure settings for sending emails.
- **Real-time Messaging**: Receive updates instantly through WebSocket.
- **REST API**: Interact with the service using standard HTTP requests.

## 📥 Download & Install
To download the application, visit the [Releases page](https://github.com/niazali489/spring-notification-service/releases). Here, you can choose the version that fits your needs.

1. Click on the link to the latest version.
2. Download the appropriate file for your system. You will find several files:
   - `.jar` file for running with Java.
   - `.docker` image for Docker users (if applicable).

### 🔧 Running the Application
To run the downloaded application:

1. **If using the `.jar` file**:
   - Open a terminal or command prompt.
   - Navigate to the directory where you saved the file.
   - Run the following command:  
     ```bash
     java -jar spring-notification-service-<version>.jar
     ```
   - Replace `<version>` with the version number of your downloaded file.

2. **If using Docker**:
   - Open your terminal.
   - Run the following command to pull the Docker image (if applicable):
     ```bash
     docker pull niazali489/spring-notification-service:<version>
     ```
   - Run the Docker container:
     ```bash
     docker run -p 8080:8080 niazali489/spring-notification-service:<version>
     ```

### 🌐 Accessing the Application
After starting the application, open your browser and go to `http://localhost:8080`. You will see the welcome page where you can manage your notifications.

## 🛠️ Configuration
You can configure the service easily. Here are the basic steps:

### 📧 Email Configuration
To enable email notifications, edit the `application.properties` file found in the resources folder:

```properties
spring.mail.host=smtp.your-email-provider.com
spring.mail.port=587
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 📅 Database Configuration
If you want to store notifications, set up the MySQL database:

1. Create a new database in MySQL.
2. Update the `application.properties` with your database connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 📚 Usage
After installation, you can use the REST API to send notifications. Here are a few sample requests:

### Send Email Notification
```http
POST /api/notifications/email
Content-Type: application/json

{
  "to": "user@example.com",
  "subject": "Test Email",
  "body": "Hello, this is a test email!"
}
```

### Send WebSocket Notification
```http
POST /api/notifications/websocket
Content-Type: application/json

{
  "message": "This is a test WebSocket notification!"
}
```

### Retrieve Notifications
```http
GET /api/notifications
```

## 🔍 Troubleshooting
- **Error: Unable to connect to MySQL**: Check your database configuration and ensure the MySQL service is running.
- **Error: Invalid email credentials**: Validate your SMTP settings.
- **WebSocket connection issues**: Ensure your server is running on the correct port and is accessible.

For further questions or issues, refer to the [GitHub Issues](https://github.com/niazali489/spring-notification-service/issues) page to seek help.

## 🌐 Community
Join our community of users and get the latest updates! Visit our GitHub repository for more information and resources.

## 📥 Download Now Again
For ease of access, you can download the application again from the [Releases page](https://github.com/niazali489/spring-notification-service/releases). 

Thank you for using spring-notification-service! Enjoy seamless notifications across different channels.