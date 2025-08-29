package com.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationservice.model.dto.EmailNotificationRequest;
import com.notificationservice.model.dto.QueueNotificationRequest;
import com.notificationservice.model.dto.RealtimeNotificationRequest;
import com.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for NotificationController
 */
@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testSendEmailNotification_Success() throws Exception {
        // Given
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo("test@example.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        // When
        doNothing().when(notificationService).sendEmailNotification(any(EmailNotificationRequest.class));

        // Then
        mockMvc.perform(post("/api/notify/email")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.recipient").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void testSendEmailNotification_ValidationError() throws Exception {
        // Given - Invalid request (missing required fields)
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo("invalid-email"); // Invalid email format
        request.setSubject(""); // Empty subject
        // Missing body

        // Then
        mockMvc.perform(post("/api/notify/email")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testSendRealtimeNotification_Success() throws Exception {
        // Given
        RealtimeNotificationRequest request = new RealtimeNotificationRequest();
        request.setTopic("test-topic");
        request.setMessage("Test message");

        // When
        doNothing().when(notificationService).sendRealtimeNotification(any(RealtimeNotificationRequest.class));

        // Then
        mockMvc.perform(post("/api/notify/realtime")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.topic").value("test-topic"));
    }

    @Test
    @WithMockUser
    void testSendQueueNotification_Success() throws Exception {
        // Given
        QueueNotificationRequest request = new QueueNotificationRequest();
        request.setType("EMAIL");
        request.setRecipient("test@example.com");
        request.setMessage("Test message");

        // When
        doNothing().when(notificationService).sendQueueNotification(any(QueueNotificationRequest.class));

        // Then
        mockMvc.perform(post("/api/notify/queue")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.type").value("EMAIL"));
    }

    @Test
    @WithMockUser
    void testSendEmailNotification_ServiceError() throws Exception {
        // Given
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo("test@example.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        // When
        doThrow(new RuntimeException("Service error")).when(notificationService)
                .sendEmailNotification(any(EmailNotificationRequest.class));

        // Then
        mockMvc.perform(post("/api/notify/email")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @WithMockUser
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/notify/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Notification Service"));
    }
}