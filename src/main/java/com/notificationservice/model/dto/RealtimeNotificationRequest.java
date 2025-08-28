package com.notificationservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for real-time WebSocket notification requests
 */
public class RealtimeNotificationRequest {

    @NotBlank(message = "Topic is required")
    @Size(max = 100, message = "Topic must be less than 100 characters")
    private String topic;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message must be less than 1000 characters")
    private String message;

    private String recipient; // Optional - for targeted notifications

    // Constructors
    public RealtimeNotificationRequest() {
    }

    public RealtimeNotificationRequest(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    public RealtimeNotificationRequest(String topic, String message, String recipient) {
        this.topic = topic;
        this.message = message;
        this.recipient = recipient;
    }

    // Getters and Setters
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "RealtimeNotificationRequest{" +
                "topic='" + topic + '\'' +
                ", message='" + message + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}