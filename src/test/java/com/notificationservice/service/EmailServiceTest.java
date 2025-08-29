package com.notificationservice.service;

import com.notificationservice.model.dto.EmailNotificationRequest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmailService
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "test@example.com");
    }

    @Test
    void testSendEmail_Success() {
        // Given
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo("recipient@example.com");
        request.setSubject("Test Subject");
        request.setBody("Test Body");
        request.setHtml(false);

        // When
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Then
        assertDoesNotThrow(() -> emailService.sendEmail(request));
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void testSendSimpleEmail() {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // When
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Then
        assertDoesNotThrow(() -> emailService.sendSimpleEmail(to, subject, body));
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void testSendHtmlEmail() {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String htmlBody = "<html><body><h1>Test</h1></body></html>";

        // When
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Then
        assertDoesNotThrow(() -> emailService.sendHtmlEmail(to, subject, htmlBody));
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }
}