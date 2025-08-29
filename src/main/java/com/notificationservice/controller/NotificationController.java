package com.notificationservice.controller;

import com.notificationservice.model.dto.EmailNotificationRequest;
import com.notificationservice.model.dto.QueueNotificationRequest;
import com.notificationservice.model.dto.RealtimeNotificationRequest;
import com.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for notification endpoints
 * <p>
 * Provides endpoints for:
 * - Email notifications
 * - Real-time WebSocket notifications
 * - Queue-based notifications
 */
@RestController
@RequestMapping("/api/notify")
@CrossOrigin(origins = "*")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * Send email notification
     * POST /api/notify/email
     */
    @PostMapping("/email")
    public ResponseEntity<?> sendEmailNotification(@Valid @RequestBody EmailNotificationRequest request) {
        try {
            logger.info("Received email notification request for: {}", request.getTo());

            notificationService.sendEmailNotification(request);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Email notification sent successfully",
                    "recipient", request.getTo()
            ));
        } catch (Exception e) {
            logger.error("Failed to send email notification", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to send email notification: " + e.getMessage()
            ));
        }
    }

    /**
     * Send real-time WebSocket notification
     * POST /api/notify/realtime
     */
    @PostMapping("/realtime")
    public ResponseEntity<?> sendRealtimeNotification(@Valid @RequestBody RealtimeNotificationRequest request) {
        try {
            logger.info("Received real-time notification request for topic: {}", request.getTopic());

            notificationService.sendRealtimeNotification(request);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Real-time notification sent successfully",
                    "topic", request.getTopic()
            ));
        } catch (Exception e) {
            logger.error("Failed to send real-time notification", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to send real-time notification: " + e.getMessage()
            ));
        }
    }

    /**
     * Send queue-based notification
     * POST /api/notify/queue
     */
    @PostMapping("/queue")
    public ResponseEntity<?> sendQueueNotification(@Valid @RequestBody QueueNotificationRequest request) {
        try {
            logger.info("Received queue notification request of type: {}", request.getType());

            notificationService.sendQueueNotification(request);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Queue notification sent successfully",
                    "type", request.getType()
            ));
        } catch (Exception e) {
            logger.error("Failed to send queue notification", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to send queue notification: " + e.getMessage()
            ));
        }
    }

    /**
     * Health check endpoint
     * GET /api/notify/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Notification Service",
                "timestamp", System.currentTimeMillis()
        ));
    }
}