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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
@ComponentScan(basePackages = {"com.engagewmep.querystudentdata.Services"})
@EnableJpaRepositories(basePackages = "com.engagewmep.querystudentdata.repository")
@EntityScan("com.engagewmep.querystudentdata.model")
public class QuerystudentdataApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuerystudentdataApplication.class, args);
		testDatabaseConnection();
	}

	public static void testDatabaseConnection() {
		String jdbcURL = "jdbc:mysql://localhost:3306/wmep";
		String username = "root";
		String password = "Vikasbachi@1";

		Connection connection = null;

		try {
			// Create a connection to the database
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connection successful!");
		} catch (SQLException e) {
			System.out.println("Connection failed! Error: " + e.getMessage());
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Error closing connection! Error: " + e.getMessage());
			}
		}
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


