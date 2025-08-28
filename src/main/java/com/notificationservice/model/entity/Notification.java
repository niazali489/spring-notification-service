package com.notificationservice.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Notification Entity for tracking sent notifications
 * <p>
 * This entity stores information about all notifications sent
 * through the system for auditing and tracking purposes.
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String type; // EMAIL, WEBSOCKET, QUEUE

    @Column(nullable = false)
    private String recipient;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 20)
    private String status; // SENT, FAILED, PENDING, QUEUED

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "error_message")
    private String errorMessage;

    // Constructors
    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(String type, String recipient, String content, String status) {
        this();
        this.type = type;
        this.recipient = recipient;
        this.content = content;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", recipient='" + recipient + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}