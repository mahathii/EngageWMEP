package com.engagewmep.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.engagewmep.backend.repository")
@EntityScan("com.engagewmep.backend.model")
public class BackendApplication {

	@Autowired
	private Environment env;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


	@Configuration
	public class WebConfig implements WebMvcConfigurer {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**")
					.allowedOrigins("http://localhost:3000")
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
					.allowCredentials(true);
		}

		@EventListener(ApplicationReadyEvent.class)
		public void logActiveDatasource() {
			System.out.println("🔍 Active Database: " + env.getProperty("spring.datasource.url"));
		}
	}

}


