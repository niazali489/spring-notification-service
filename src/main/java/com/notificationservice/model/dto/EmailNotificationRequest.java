package com.notificationservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for email notification requests
 */
public class EmailNotificationRequest {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String to;

    @NotBlank(message = "Subject is required")
    @Size(max = 255, message = "Subject must be less than 255 characters")
    private String subject;

    @NotBlank(message = "Email body is required")
    @Size(max = 10000, message = "Email body must be less than 10000 characters")
    private String body;

    private boolean html = false;

    // Constructors
    public EmailNotificationRequest() {
    }

    public EmailNotificationRequest(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public EmailNotificationRequest(String to, String subject, String body, boolean html) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.html = html;
    }

    // Getters and Setters
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "EmailNotificationRequest{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", html=" + html +
                '}';
    }
}