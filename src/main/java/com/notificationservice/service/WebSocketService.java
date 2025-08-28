package com.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket Service for real-time notifications
 * <p>
 * This service handles WebSocket messaging for real-time notifications
 * to connected clients via STOMP protocol.
 */
@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Send notification to specific topic
     */
    public void sendNotification(String topic, String message) {
        try {
            logger.info("Sending WebSocket notification to topic: {}", topic);

            // Create notification payload
            Map<String, Object> notification = new HashMap<>();
            notification.put("message", message);
            notification.put("timestamp", LocalDateTime.now().toString());
            notification.put("topic", topic);

            // Send to topic
            messagingTemplate.convertAndSend("/topic/" + topic, notification);

            logger.info("WebSocket notification sent successfully to topic: {}", topic);

        } catch (Exception e) {
            logger.error("Failed to send WebSocket notification to topic: {}", topic, e);
            throw new RuntimeException("Failed to send WebSocket notification", e);
        }
    }

    /**
     * Send notification to specific user
     */
    public void sendNotificationToUser(String username, String message) {
        try {
            logger.info("Sending WebSocket notification to user: {}", username);

            // Create notification payload
            Map<String, Object> notification = new HashMap<>();
            notification.put("message", message);
            notification.put("timestamp", LocalDateTime.now().toString());
            notification.put("recipient", username);

            // Send to specific user
            messagingTemplate.convertAndSendToUser(username, "/topic/notifications", notification);

            logger.info("WebSocket notification sent successfully to user: {}", username);

        } catch (Exception e) {
            logger.error("Failed to send WebSocket notification to user: {}", username, e);
            throw new RuntimeException("Failed to send WebSocket notification to user", e);
        }
    }

    /**
     * Broadcast notification to all connected clients
     */
    public void broadcastNotification(String message) {
        try {
            logger.info("Broadcasting WebSocket notification to all clients");

            // Create notification payload
            Map<String, Object> notification = new HashMap<>();
            notification.put("message", message);
            notification.put("timestamp", LocalDateTime.now().toString());
            notification.put("type", "broadcast");

            // Broadcast to all clients
            messagingTemplate.convertAndSend("/topic/broadcast", notification);

            logger.info("WebSocket notification broadcasted successfully");

        } catch (Exception e) {
            logger.error("Failed to broadcast WebSocket notification", e);
            throw new RuntimeException("Failed to broadcast WebSocket notification", e);
        }
    }

    /**
     * Send custom payload to topic
     */
    public void sendCustomPayload(String topic, Object payload) {
        try {
            logger.info("Sending custom WebSocket payload to topic: {}", topic);

            messagingTemplate.convertAndSend("/topic/" + topic, payload);

            logger.info("Custom WebSocket payload sent successfully to topic: {}", topic);

        } catch (Exception e) {
            logger.error("Failed to send custom WebSocket payload to topic: {}", topic, e);
            throw new RuntimeException("Failed to send custom WebSocket payload", e);
        }
    }
}