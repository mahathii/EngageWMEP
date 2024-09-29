package com.engagewmep.querystudentdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String recipientEmail, String token) {
        String subject = "Email Verification";
        String verificationUrl = "http://localhost:3000/verify?token=" + token;
        String message = "Please verify your email by clicking the link below:\n" + verificationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
