package com.notificationservice.service;

import com.notificationservice.config.RabbitMQConfig;
import com.notificationservice.model.dto.QueueNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Queue Service for RabbitMQ-based message dispatching
 * <p>
 * This service handles:
 * - Sending messages to RabbitMQ queues
 * - Processing messages from queues
 * - Fallback to async processing when RabbitMQ is unavailable
 */
@Service
public class QueueService {

    private static final Logger logger = LoggerFactory.getLogger(QueueService.class);

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebSocketService webSocketService;

    /**
     * Send notification to queue
     */
    public void sendToQueue(QueueNotificationRequest request) {
        if (rabbitTemplate != null) {
            // Send to RabbitMQ if available
            sendToRabbitMQ(request);
        } else {
            // Fallback to direct async processing
            logger.warn("RabbitMQ not available, processing notification directly");
            processNotificationDirectly(request);
        }
    }

    /**
     * Send message to RabbitMQ
     */
    @ConditionalOnProperty(name = "spring.rabbitmq.host")
    private void sendToRabbitMQ(QueueNotificationRequest request) {
        try {
            logger.info("Sending notification to RabbitMQ queue: {}", request.getType());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                    request
            );

            logger.info("Notification sent to RabbitMQ queue successfully: {}", request.getType());

        } catch (Exception e) {
            logger.error("Failed to send notification to RabbitMQ, falling back to direct processing", e);
            processNotificationDirectly(request);
        }
    }

    /**
     * Process RabbitMQ messages
     */
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    @ConditionalOnProperty(name = "spring.rabbitmq.host")
    public void processQueueMessage(QueueNotificationRequest request) {
        try {
            logger.info("Processing queued notification: {}", request.getType());

            processNotificationDirectly(request);

            logger.info("Queued notification processed successfully: {}", request.getType());

        } catch (Exception e) {
            logger.error("Failed to process queued notification: {}", request.getType(), e);
            // In a production system, you might want to implement dead letter queue here
        }
    }

    /**
     * Process notification directly (fallback method)
     */
    @Async
    public void processNotificationDirectly(QueueNotificationRequest request) {
        try {
            logger.info("Processing notification directly: {}", request.getType());

            switch (request.getType().toUpperCase()) {
                case "EMAIL":
                    processEmailNotification(request);
                    break;
                case "WEBSOCKET":
                    processWebSocketNotification(request);
                    break;
                case "BROADCAST":
                    processBroadcastNotification(request);
                    break;
                default:
                    logger.warn("Unknown notification type: {}", request.getType());
            }

        } catch (Exception e) {
            logger.error("Failed to process notification directly: {}", request.getType(), e);
            throw new RuntimeException("Failed to process notification", e);
        }
    }

    /**
     * Process email notification from queue
     */
    private void processEmailNotification(QueueNotificationRequest request) {
        try {
            // Extract email details from request
            String[] parts = request.getMessage().split("\\|", 2);
            String subject = parts.length > 1 ? parts[0] : "Notification";
            String body = parts.length > 1 ? parts[1] : request.getMessage();

            emailService.sendSimpleEmail(request.getRecipient(), subject, body);

        } catch (Exception e) {
            logger.error("Failed to process email notification from queue", e);
            throw e;
        }
    }

    /**
     * Process WebSocket notification from queue
     */
    private void processWebSocketNotification(QueueNotificationRequest request) {
        try {
            webSocketService.sendNotificationToUser(request.getRecipient(), request.getMessage());
        } catch (Exception e) {
            logger.error("Failed to process WebSocket notification from queue", e);
            throw e;
        }
    }

    /**
     * Process broadcast notification from queue
     */
    private void processBroadcastNotification(QueueNotificationRequest request) {
        try {
            webSocketService.broadcastNotification(request.getMessage());
        } catch (Exception e) {
            logger.error("Failed to process broadcast notification from queue", e);
            throw e;
        }
    }
}