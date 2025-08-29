package com.notificationservice.service;

import com.notificationservice.model.dto.EmailNotificationRequest;
import com.notificationservice.model.dto.QueueNotificationRequest;
import com.notificationservice.model.dto.RealtimeNotificationRequest;
import com.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NotificationService
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private WebSocketService webSocketService;

    @Mock
    private QueueService queueService;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        // Setup test data if needed
    }

    @Test
    void testSendEmailNotification_Success() {
        // Given
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo("test@example.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        // When
        doNothing().when(emailService).sendEmail(any(EmailNotificationRequest.class));
        when(notificationRepository.save(any())).thenReturn(null);

        // Then
        assertDoesNotThrow(() -> notificationService.sendEmailNotification(request));
        verify(emailService).sendEmail(request);
        verify(notificationRepository).save(any());
    }

    @Test
    void testSendEmailNotification_Failure() {
        // Given
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo("test@example.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");

        // When
        doThrow(new RuntimeException("Email sending failed")).when(emailService).sendEmail(any(EmailNotificationRequest.class));
        when(notificationRepository.save(any())).thenReturn(null);

        // Then
        assertThrows(RuntimeException.class, () -> notificationService.sendEmailNotification(request));
        verify(emailService).sendEmail(request);
        verify(notificationRepository, times(2)).save(any()); // Once for failure record
    }

    @Test
    void testSendRealtimeNotification_Success() {
        // Given
        RealtimeNotificationRequest request = new RealtimeNotificationRequest();
        request.setTopic("test-topic");
        request.setMessage("Test message");

        // When
        doNothing().when(webSocketService).sendNotification(anyString(), anyString());
        when(notificationRepository.save(any())).thenReturn(null);

        // Then
        assertDoesNotThrow(() -> notificationService.sendRealtimeNotification(request));
        verify(webSocketService).sendNotification(request.getTopic(), request.getMessage());
        verify(notificationRepository).save(any());
    }

    @Test
    void testSendQueueNotification_Success() {
        // Given
        QueueNotificationRequest request = new QueueNotificationRequest();
        request.setType("EMAIL");
        request.setRecipient("test@example.com");
        request.setMessage("Test message");

        // When
        doNothing().when(queueService).sendToQueue(any(QueueNotificationRequest.class));
        when(notificationRepository.save(any())).thenReturn(null);

        // Then
        assertDoesNotThrow(() -> notificationService.sendQueueNotification(request));
        verify(queueService).sendToQueue(request);
        verify(notificationRepository).save(any());
    }
}