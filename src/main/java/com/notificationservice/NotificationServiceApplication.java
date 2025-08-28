package com.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for Notification & Messaging Service
 * <p>
 * This microservice provides:
 * - Real-time WebSocket notifications for in-app events
 * - Email notifications via SMTP integration
 * - Queue-based message dispatching with RabbitMQ
 * - JWT-based authentication for API security
 * - MySQL database for production, H2 for development
 *
 * @author NotificationService Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableAsync
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}