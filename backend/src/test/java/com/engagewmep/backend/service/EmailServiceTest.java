package com.engagewmep.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendVerificationEmail() {
        String recipientEmail = "test@example.com";
        String token = "123456";

        emailService.sendVerificationEmail(recipientEmail, token);

        // Capture the SimpleMailMessage passed to mailSender.send
        ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(mailMessageCaptor.capture());
        SimpleMailMessage capturedMessage = mailMessageCaptor.getValue();

        // Verify that the subject is set correctly
        assertEquals("Email Verification", capturedMessage.getSubject());

        // Verify the recipient email address
        assertArrayEquals(new String[] { recipientEmail }, capturedMessage.getTo());

        // Verify that the email text contains the verification URL with the token
        String expectedUrl = "http://localhost:3000/verify?token=" + token;
        assertTrue(capturedMessage.getText().contains(expectedUrl), "Email text should contain the verification URL");
    }

    @Test
    public void testSendPasswordResetEmail() {
        String toEmail = "user@example.com";
        String token = "abcdef";

        emailService.sendPasswordResetEmail(toEmail, token);

        // Capture the SimpleMailMessage passed to mailSender.send
        ArgumentCaptor<SimpleMailMessage> mailMessageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(mailMessageCaptor.capture());
        SimpleMailMessage capturedMessage = mailMessageCaptor.getValue();

        // Verify that the subject is set correctly
        assertEquals("Password Reset Request", capturedMessage.getSubject());

        // Verify the recipient email address
        assertArrayEquals(new String[] { toEmail }, capturedMessage.getTo());

        // Verify that the email text contains the reset URL with the token
        String expectedResetUrl = "http://localhost:3000/reset-password/" + token;
        assertTrue(capturedMessage.getText().contains(expectedResetUrl), "Email text should contain the password reset URL");
    }
}
