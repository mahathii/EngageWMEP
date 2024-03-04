package com.engagewmep.querystudentdata.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.engagewmep.querystudentdata.model", "com.engagewmep.querystudentdata.repository"})
public class AppConfig {
    // additional configuration or bean definitions can go here
}