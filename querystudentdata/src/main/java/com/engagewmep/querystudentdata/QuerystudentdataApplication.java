package com.engagewmep.querystudentdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan(basePackages = {"com.engagewmep.querystudentdata.Services"})
@EnableJpaRepositories(basePackages = "com.engagewmep.querystudentdata.repository")
@EntityScan("com.engagewmep.querystudentdata.model")
public class QuerystudentdataApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuerystudentdataApplication.class, args);
	}

	// CommandLineRunner to setup default data or configurations
}

