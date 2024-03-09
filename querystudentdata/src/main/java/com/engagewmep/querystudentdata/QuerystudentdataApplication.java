package com.engagewmep.querystudentdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan(basePackages = {"com.engagewmep.querystudentdata.Services"})
@EnableJpaRepositories(basePackages = "com.engagewmep.querystudentdata.repository")
@EntityScan("com.engagewmep.querystudentdata.model")
public class QuerystudentdataApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuerystudentdataApplication.class, args);
	}

	@Configuration
	public class WebConfig implements WebMvcConfigurer {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**") // This allows all endpoints. You can restrict paths if needed.
					.allowedOrigins("http://localhost:3000") // Allows only your React app origin. Adjust if your React app runs on a different port.
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Adjust allowed methods as necessary.
					.allowCredentials(true);
		}
	}
	// CommandLineRunner to setup default data or configurations
}


