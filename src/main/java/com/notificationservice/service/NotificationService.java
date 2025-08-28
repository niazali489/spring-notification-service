package com.notificationservice.service;

import com.notificationservice.model.dto.EmailNotificationRequest;
import com.notificationservice.model.dto.QueueNotificationRequest;
import com.notificationservice.model.dto.RealtimeNotificationRequest;
import com.notificationservice.model.entity.Notification;
import com.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Main Notification Service that coordinates different notification types
 * <p>
 * This service acts as a facade for different notification mechanisms:
 * - Email notifications via EmailService
 * - Real-time notifications via WebSocketService
 * - Queue-based notifications via QueueService
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Send email notification
     */
    public void sendEmailNotification(EmailNotificationRequest request) {
        try {
            logger.info("Processing email notification to: {}", request.getTo());

            // Send email
            emailService.sendEmail(request);

            // Save notification record
            saveNotificationRecord("EMAIL", request.getTo(), request.getSubject(), "SENT");

            logger.info("Email notification sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            logger.error("Failed to send email notification to: {}", request.getTo(), e);
            saveNotificationRecord("EMAIL", request.getTo(), request.getSubject(), "FAILED");
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    /**
     * Send real-time WebSocket notification
     */
    public void sendRealtimeNotification(RealtimeNotificationRequest request) {
        try {
            logger.info("Processing real-time notification for topic: {}", request.getTopic());

            // Send WebSocket message
            webSocketService.sendNotification(request.getTopic(), request.getMessage());

            // Save notification record
            saveNotificationRecord("WEBSOCKET", request.getTopic(), request.getMessage(), "SENT");

            logger.info("Real-time notification sent successfully for topic: {}", request.getTopic());
        } catch (Exception e) {
            logger.error("Failed to send real-time notification for topic: {}", request.getTopic(), e);
            saveNotificationRecord("WEBSOCKET", request.getTopic(), request.getMessage(), "FAILED");
            throw new RuntimeException("Failed to send real-time notification", e);
        }
    }

    /**
     * Send queue-based notification
     */
    public void sendQueueNotification(QueueNotificationRequest request) {
        try {
            logger.info("Processing queue notification of type: {}", request.getType());

            // Send to queue
            queueService.sendToQueue(request);

            // Save notification record
            saveNotificationRecord("QUEUE", request.getRecipient(),
                    request.getType() + ": " + request.getMessage(), "QUEUED");

            logger.info("Queue notification sent successfully of type: {}", request.getType());
        } catch (Exception e) {
            logger.error("Failed to send queue notification of type: {}", request.getType(), e);
            saveNotificationRecord("QUEUE", request.getRecipient(),
                    request.getType() + ": " + request.getMessage(), "FAILED");
            throw new RuntimeException("Failed to send queue notification", e);
        }
    }

    /**
     * Save notification record to database
     */
    private void saveNotificationRecord(String type, String recipient, String content, String status) {
        try {
            Notification notification = new Notification();
            notification.setType(type);
            notification.setRecipient(recipient);
            notification.setContent(content);
            notification.setStatus(status);
            notification.setCreatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
        } catch (Exception e) {
            logger.error("Failed to save notification record", e);
            // Don't throw exception here to avoid breaking the main flow
        }
    }
}