package com.engagewmep.querystudentdata;

import com.engagewmep.querystudentdata.controller.EventController;
import com.engagewmep.querystudentdata.controller.UserController;
import com.engagewmep.querystudentdata.repository.UserRepository;
import com.engagewmep.querystudentdata.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.engagewmep.querystudentdata.Services"})
@ComponentScan(basePackages = {"com.engagewmep.querystudentdata"})
@EnableJpaRepositories(basePackages = "com.engagewmep.querystudentdata.repository")
//@EntityScan("com.engagewmep.querystudentdata.model")
public class QuerystudentdataApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuerystudentdataApplication.class, args);
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
	}

//	@Bean
//	public UserController userController(UserRepository userRepository)
//	{
//		return new UserController(userRepository);
//	}

//	@Bean
//	public EventController eventController(StudentService studentService) {
//		return new EventController(studentService);
//	}

}


