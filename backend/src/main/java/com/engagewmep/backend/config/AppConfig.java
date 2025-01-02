package com.engagewmep.backend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.engagewmep.backend.model", "com.engagewmep.backend.repository"})
public class AppConfig {
    // additional configuration or bean definitions can go here

}