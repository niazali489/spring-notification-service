package com.notificationservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for queue-based notification requests
 */
public class QueueNotificationRequest {

    @NotBlank(message = "Type is required")
    @Size(max = 50, message = "Type must be less than 50 characters")
    private String type; // EMAIL, WEBSOCKET, BROADCAST

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must be less than 2000 characters")
    private String message;

    @NotBlank(message = "Recipient is required")
    @Size(max = 255, message = "Recipient must be less than 255 characters")
    private String recipient;

    private int priority = 0; // 0 = normal, 1 = high, -1 = low

    // Constructors
    public QueueNotificationRequest() {
    }

    public QueueNotificationRequest(String type, String message, String recipient) {
        this.type = type;
        this.message = message;
        this.recipient = recipient;
    }

    public QueueNotificationRequest(String type, String message, String recipient, int priority) {
        this.type = type;
        this.message = message;
        this.recipient = recipient;
        this.priority = priority;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "QueueNotificationRequest{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", recipient='" + recipient + '\'' +
                ", priority=" + priority +
                '}';
    }
}