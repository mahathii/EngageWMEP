package com.engagewmep.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class TestMailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // Return a simple instance; it can be further customized or mocked if needed.
        return new JavaMailSenderImpl();
    }
}
