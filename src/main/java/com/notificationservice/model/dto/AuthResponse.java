package com.notificationservice.model.dto;

/**
 * DTO for authentication responses
 */
public class AuthResponse {

    private String token;
    private String type;
    private String username;
    private String message;

    // Constructors
    public AuthResponse() {
    }

    public AuthResponse(String token, String type, String username, String message) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.message = message;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}