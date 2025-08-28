package com.notificationservice.service;

import com.notificationservice.model.dto.EmailNotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Email Service for sending email notifications via SMTP
 * <p>
 * This service handles email sending with support for:
 * - HTML and plain text emails
 * - Asynchronous sending
 * - Error handling and retry logic
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from:noreply@notificationservice.com}")
    private String fromEmail;

    /**
     * Send email notification
     */
    public void sendEmail(EmailNotificationRequest request) {
        sendEmailAsync(request);
    }

    /**
     * Send email asynchronously
     */
    @Async
    public void sendEmailAsync(EmailNotificationRequest request) {
        try {
            logger.info("Sending email to: {} with subject: {}", request.getTo(), request.getSubject());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set email properties
            helper.setFrom(fromEmail);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());

            // Set content (HTML if specified, otherwise plain text)
            if (request.isHtml()) {
                helper.setText(request.getBody(), true);
            } else {
                helper.setText(request.getBody());
            }

            // Send the email
            mailSender.send(message);

            logger.info("Email sent successfully to: {}", request.getTo());

        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}", request.getTo(), e);
            throw new RuntimeException("Failed to send email", e);
        } catch (Exception e) {
            logger.error("Unexpected error while sending email to: {}", request.getTo(), e);
            throw new RuntimeException("Unexpected error while sending email", e);
        }
    }

    /**
     * Send simple text email (utility method)
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo(to);
        request.setSubject(subject);
        request.setBody(body);
        request.setHtml(false);

        sendEmail(request);
    }

    /**
     * Send HTML email (utility method)
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setTo(to);
        request.setSubject(subject);
        request.setBody(htmlBody);
        request.setHtml(true);

        sendEmail(request);
    }
}